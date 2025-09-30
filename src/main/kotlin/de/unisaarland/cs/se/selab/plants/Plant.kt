package de.unisaarland.cs.se.selab.plants

import de.unisaarland.cs.se.selab.YEAR_TICK_MAX
import de.unisaarland.cs.se.selab.YEAR_TICK_MIN
import de.unisaarland.cs.se.selab.farms.Action
import de.unisaarland.cs.se.selab.incidents.AnimalAttack
import de.unisaarland.cs.se.selab.incidents.BeeHappy
import de.unisaarland.cs.se.selab.incidents.Drought
import de.unisaarland.cs.se.selab.incidents.Incident
import de.unisaarland.cs.se.selab.logger.Logger
import kotlin.math.max

/**
 * The plant with all its parameter needed for the Simulation.
 */
class Plant(var type: PlantType, var data: PlantData, yearTick: Int) {
    private var oldHarvestEstimate: Int? = null
    private var harvestEstimate: Int = initHarvestEstimate(yearTick)
    private var sowTime: Int = 0
    private var harvestTime: Int = 0
    private var actionPerformed: Action? = null
    private val incidents: MutableList<Incident> = mutableListOf()
    private var mowedFor: Int = 0
    private var cutted: Boolean = false

    /**
     * Constants used in Plant
     */
    companion object PlantConstants {
        const val FALLOW_TICKS = 4
        const val PLANTATION_HARVEST_RESET = 21

        const val HARVEST_LATE_PENALTY_GRAPE_FACTOR = 0.95
        const val HARVEST_LATE_PENALTY_ALMOND_FACTOR = 0.9
        const val HARVEST_LATE_PENALTY_WHEAT_OAT_FACTOR = 0.8
        const val HARVEST_LATE_PENALTY_APPLE_FACTOR = 0.5
        const val HARVEST_LATE_PENALTY_CHERRY_FACTOR = 0.3
        const val SOWING_LATE_PENALTY_FACTOR = 0.8
        const val SUNLIGHT_HIGH_DIVISOR = 25
        const val SUNLIGHT_HIGH_PENALTY_FACTOR = 0.9
        const val MOISTURE_LOW_DIVISOR = 100
        const val MOISTURE_LOW_PENALTY = 50
        const val WEEDING_MISSED_PENALTY_FACTOR = 0.9
        const val CUTTING_MISSED_PENALTY_FACTOR = 0.5
        const val MOWING_MISSED_PENALTY_FACTOR = 0.9
        const val ANIMAL_ATTACK_FIELD_GRAPE_PENALTY_FACTOR = 0.5
        const val ANIMAL_ATTACK_PLANTATION_PENALTY_FACTOR = 0.9
    }
    private fun initHarvestEstimate(yearTick: Int): Int {
        return when (data.tileType) {
            PlantTile.PLANTATION -> (data.initialHarvestEstimate * harvestPenalty(yearTick)).toInt()
            PlantTile.FIELD -> 0
        }
    }

    private fun yearTickRange(start: Int, endInclusive: Int): Set<Int> {
        require(start in YEAR_TICK_MIN..YEAR_TICK_MAX)
        val end = if (endInclusive > YEAR_TICK_MAX) endInclusive % YEAR_TICK_MAX else endInclusive
        return if (end < start) {
            IntRange(start - 1, end + YEAR_TICK_MAX - 1).map { it % YEAR_TICK_MAX + 1 }
        } else {
            IntRange(start, end)
        }.toSet()
    }

    /**
     * Calculates the time frame in which harvest penalty should be applied.
     */
    private fun harvestPenaltyTimeFrame(): Set<Int> {
        val sowingTimeStart = if (data.tileType == PlantTile.PLANTATION) {
            PLANTATION_HARVEST_RESET
        } else {
            data.sowRange.first
        }
        return yearTickRange(data.harvestingRange.last + 1, sowingTimeStart - 1)
    }

    private fun harvestPenalty(yearTick: Int): Double {
        val lateFor = harvestPenaltyTimeFrame().indexOf(yearTick) + 1 // Shift index to start with 0
        if (lateFor == 0) return 1.0
        return when {
            (type == PlantType.WHEAT || type == PlantType.OAT) && lateFor <= 2
            -> HARVEST_LATE_PENALTY_WHEAT_OAT_FACTOR
            type == PlantType.APPLE && lateFor == 1 -> HARVEST_LATE_PENALTY_APPLE_FACTOR
            type == PlantType.ALMOND && lateFor == 1 -> HARVEST_LATE_PENALTY_ALMOND_FACTOR
            type == PlantType.CHERRY && lateFor == 1 -> HARVEST_LATE_PENALTY_CHERRY_FACTOR
            type == PlantType.GRAPE && lateFor <= 3 -> HARVEST_LATE_PENALTY_GRAPE_FACTOR
            else -> 0.0
        }
    }

    /**
     * Getter for Harvest Estimate
     */
    fun getHarvestEstimate(): Int {
        return harvestEstimate
    }

    /**
     * Getter for data.moistureMin
     */
    fun getMinMoisture(): Int {
        return data.moistureMin
    }

    private fun blooming(yearTick: Int): Boolean {
        if (data.tileType == PlantTile.FIELD && sowTime < 1) return false

        return when (type) {
            PlantType.POTATO, PlantType.PUMPKIN -> {
                data.bloomRange.map { it + sowTime }.contains(yearTick)
            }
            else -> {
                data.bloomRange.contains(yearTick)
            }
        }
    }

    /**
     * Checks if the plant can be pollinated in the current yearTick by Bees
     */
    fun pollinateable(yearTick: Int): Boolean {
        return data.pollinationByInsects && blooming(yearTick)
    }

    /**
     * Add the pollination effect to the plant
     */
    fun addPollination(beeHappy: BeeHappy) {
        incidents.add(beeHappy)
    }

    /**
     * Add the stampede effect to the plant
     */
    fun addStampede(animalAttack: AnimalAttack): Boolean {
        if (harvestEstimate <= 0) return false
        incidents.add(animalAttack)
        when (type) {
            PlantType.APPLE, PlantType.ALMOND, PlantType.CHERRY -> {
                mowedFor = 2
            }
            else -> {}
        }
        return true
    }

    /**
     * Add the drought effect to the plant
     */
    fun addDrought(drought: Drought) {
        incidents.add(drought)
    }

    /**
     * Calculates, logs, updates the harvest estimate and prepares plant for next tick
     */
    fun updateHarvestEstimate(
        yearTick: Int,
        drought: Boolean,
        sunlight: Int,
        moisture: Int,
        tileID: Int
    ) {
        val droughtNotInThisTick = drought && incidents.none { it is Drought }
        if (actionPerformed == Action.HARVESTING || !isSown() || droughtNotInThisTick) {
            resetForNextTick()
            return
        }
        oldHarvestEstimate = oldHarvestEstimate ?: harvestEstimate

        // Sowed too late
        if (data.tileType == PlantTile.FIELD && sowTime == yearTick) {
            val sowedLate = max(0, sowTime - data.sowRange.last)
            repeat(sowedLate) { harvestEstimate = (harvestEstimate * SOWING_LATE_PENALTY_FACTOR).toInt() }
        }

        // Sunlight too high, yes this works!
        repeat((sunlight - data.sunlightMax) / SUNLIGHT_HIGH_DIVISOR) {
            harvestEstimate = (harvestEstimate * SUNLIGHT_HIGH_PENALTY_FACTOR).toInt()
        }

        // Moisture too low
        val irrigationNeeded = (data.tileType == PlantTile.PLANTATION || sowTime > 0) && moisture < data.moistureMin
        if (irrigationNeeded) {
            if (moisture > 0) {
                repeat(
                    (data.moistureMin - moisture) / MOISTURE_LOW_DIVISOR
                ) { harvestEstimate = max(harvestEstimate - MOISTURE_LOW_PENALTY, 0) }
            } else {
                harvestEstimate = 0
                sowTime = 0
                harvestTime = yearTick
            }
        }
        val actionsNotPerformed = applyAndGetMissed(yearTick, drought, irrigationNeeded)
        if (actionsNotPerformed.isNotEmpty()) {
            Logger.actionNotPerformed(tileID, actionsNotPerformed)
        }

        applyIncidents(yearTick)

        if (oldHarvestEstimate != harvestEstimate) {
            Logger.changedHarvestEstimate(tileID, harvestEstimate, type)
        }
        resetForNextTick()
    }

    private fun applyAndGetMissed(yearTick: Int, drought: Boolean, irrigationNeeded: Boolean): List<Action> {
        if (drought) return emptyList()
        val actionsMissed = mutableListOf<Action>()

        if (weedable(yearTick)) {
            harvestEstimate = (harvestEstimate * WEEDING_MISSED_PENALTY_FACTOR).toInt()
            actionsMissed.add(Action.WEEDING)
        }
        if (cuttable(yearTick) && data.cuttingTimes.last() == yearTick) {
            harvestEstimate = (harvestEstimate * CUTTING_MISSED_PENALTY_FACTOR).toInt()
            actionsMissed.add(Action.CUTTING)
        }
        if (mowable(yearTick)) {
            harvestEstimate = (harvestEstimate * MOWING_MISSED_PENALTY_FACTOR).toInt()
            actionsMissed.add(Action.MOWING)
        }
        if (irrigationNeeded) actionsMissed.add(Action.IRRIGATING)
        val harvestPenalty = harvestPenalty(yearTick)
        if (harvestEstimate != 0 && harvestPenalty != 1.0) {
            harvestEstimate = (harvestEstimate * harvestPenalty).toInt()
            actionsMissed.add(Action.HARVESTING)
        }
        return actionsMissed
    }

    private fun applyIncidents(yearTick: Int) {
        incidents.forEach {
            when (it) {
                is BeeHappy -> {
                    harvestEstimate += (it.effect * harvestEstimate).toInt()
                }
                is AnimalAttack -> {
                    harvestEstimate = if (type == PlantType.GRAPE || data.tileType == PlantTile.FIELD) {
                        (harvestEstimate * ANIMAL_ATTACK_FIELD_GRAPE_PENALTY_FACTOR).toInt()
                    } else {
                        (harvestEstimate * ANIMAL_ATTACK_PLANTATION_PENALTY_FACTOR).toInt()
                    }
                }
                is Drought -> {
                    harvestEstimate = 0
                    if (harvestTime == 0) harvestTime = yearTick
                    sowTime = 0
                }
            }
        }
    }

    /**
     * Checks if the field plant is fallow (4 ticks after harvest)
     */
    fun isFallow(yearTick: Int): Boolean {
        return data.tileType == PlantTile.FIELD &&
            harvestTime > 0 &&
            yearTick in yearTickRange(harvestTime + 1, harvestTime + FALLOW_TICKS)
    }

    /**
     * Checks if the plant can be harvested
     */
    fun harvestable(yearTick: Int): Boolean {
        val fullHarvestRange = yearTickRange(
            data.harvestingRange.first,
            data.harvestingRange.last + data.lateHarvestTimeFrame
        )
        return actionPerformed != Action.HARVESTING &&
            harvestEstimate > 0 &&
            harvestTime <= 0 &&
            fullHarvestRange.contains(yearTick)
    }

    /**
     * Checks if the plant can be cut
     */
    fun cuttable(yearTick: Int): Boolean {
        return !cutted &&
            harvestEstimate > 0 &&
            harvestTime <= 0 &&
            data.cuttingTimes.contains(yearTick)
    }

    /**
     * Checks if the plant can be mowed
     */
    fun mowable(yearTick: Int): Boolean {
        return actionPerformed != Action.MOWING &&
            harvestEstimate > 0 &&
            mowedFor <= 0 &&
            harvestTime <= 0 &&
            data.mowingTimes.contains(yearTick)
    }

    /**
     * Checks if the plant can be weeded
     */
    fun weedable(yearTick: Int): Boolean {
        return actionPerformed != Action.WEEDING &&
            sowTime > 0 &&
            data.weedingTimes.map { it + sowTime }.contains(yearTick)
    }

    private fun harvest(yearTick: Int): Int {
        harvestTime = yearTick
        sowTime = 0
        this.oldHarvestEstimate = null
        val harvestEstimateOld = harvestEstimate
        harvestEstimate = 0
        return harvestEstimateOld
    }

    /**
     * Perform the given action on the plant.
     * Except IRRIGATING
     */
    fun performAction(action: Action, yearTick: Int): Int? {
        actionPerformed = action
        if (action == Action.CUTTING) cutted = true
        return if (action == Action.HARVESTING) harvest(yearTick) else null
    }

    /**
     * Checks if the plant is sown or if it is a plantation plant
     */
    fun isSown(): Boolean {
        return data.tileType == PlantTile.PLANTATION || sowTime > 0
    }

    /**
     * Sows the field plant
     */
    fun sow(plantType: PlantType, plantData: PlantData, yearTick: Int) {
        this.type = plantType
        this.data = plantData
        this.sowTime = yearTick
        this.harvestTime = 0
        this.oldHarvestEstimate = data.initialHarvestEstimate
        this.harvestEstimate = data.initialHarvestEstimate
    }

    private fun resetForNextTick() {
        oldHarvestEstimate = null
        incidents.clear()
        actionPerformed = null
        if (mowedFor > 0) mowedFor--
    }

    /**
     * Prepares the plant for the current tick
     */
    fun prepareCurrentTick(yearTick: Int) {
        if (yearTick == PLANTATION_HARVEST_RESET && data.tileType == PlantTile.PLANTATION) {
            oldHarvestEstimate = data.initialHarvestEstimate
            harvestEstimate = data.initialHarvestEstimate
            cutted = false
        }
    }

    override fun hashCode(): Int {
        return when (type) {
            PlantType.POTATO -> 1
            PlantType.OAT -> 2
            PlantType.WHEAT -> 3
            PlantType.PUMPKIN -> 4
            PlantType.APPLE -> 5
            PlantType.ALMOND -> 6
            PlantType.CHERRY -> 7
            PlantType.GRAPE -> 8
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other is Plant) {
            val bool1 = this.harvestEstimate == other.harvestEstimate && this.sowTime == other.sowTime
            val bool2 = this.harvestTime == other.harvestTime && this.actionPerformed == other.actionPerformed
            val bool3 = this.incidents == other.incidents && this.mowedFor == other.mowedFor
            return bool1 && bool2 && bool3
        } else {
            return false
        }
    }
}
