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
    private var sowTime: Int = if (data.tileType == PlantTile.FIELD) SOW_TIME_NO_PLANT else SOW_TIME_PLANTATION
    private var harvestTime: Int = HARVEST_TIME_NOT_PERFORMED
    private var actionPerformed: Action? = null
    private val incidents: MutableList<Incident> = mutableListOf()
    private var mowedFor: Int = 0
    private var cutPerformed: Boolean = false

    /**
     * Constants used in Plant
     */
    companion object PlantConstants {
        const val SOW_TIME_NO_PLANT = -1
        const val SOW_TIME_PLANTATION = 0
        const val HARVEST_TIME_NOT_PERFORMED = 0
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

        /**
         * Generates a set from `start` to `endInclusive` wrapping around to 1 if exceeding 24
         * @return Set of yearTicks
         */
        fun yearTickRangeSet(start: Int, endInclusive: Int): Set<Int> {
            require(start in YEAR_TICK_MIN..YEAR_TICK_MAX)
            val end = if (endInclusive > YEAR_TICK_MAX) endInclusive % YEAR_TICK_MAX else endInclusive
            return if (end < start) {
                IntRange(start - 1, end + YEAR_TICK_MAX - 1).map { it % YEAR_TICK_MAX + 1 }
            } else {
                IntRange(start, end)
            }.toSet()
        }

        /**
         * Corrects the yearTick to be in Range 1..24
         */
        fun yearTickFix(yearTick: Int): Int = if (yearTick > YEAR_TICK_MAX) yearTick % YEAR_TICK_MAX else yearTick
    }

    private fun initHarvestEstimate(yearTick: Int): Int {
        var initHE: Int
        when (data.tileType) {
            PlantTile.PLANTATION -> {
                initHE = (data.initialHarvestEstimate * harvestPenalty(yearTick - 1)).toInt()
                val x = harvestPenalty(yearTick - 2)
                val y = harvestPenalty(yearTick - 3)
                initHE = (initHE * x * y).toInt()
            }
            PlantTile.FIELD -> {
                initHE = 0
            }
        }
        return initHE
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
        return yearTickRangeSet(data.harvestingRange.last + 1, sowingTimeStart - 1)
    }

    private fun harvestPenalty(yearTick: Int): Double {
        val lateFor = harvestPenaltyTimeFrame().indexOf(yearTick + 1) + 1 // Shift index to start with 0
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
    fun getHarvestEstimate(): Int = harvestEstimate

    /**
     * Getter for data.moistureMin
     */
    fun getMinMoisture(): Int = data.moistureMin

    private fun blooming(yearTick: Int): Boolean {
        if (sowTime == SOW_TIME_NO_PLANT) return false

        return when (type) {
            PlantType.POTATO, PlantType.PUMPKIN -> {
                data.bloomRange.map { yearTickFix(it + sowTime) }.contains(yearTick)
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
        sunlight: Int,
        moisture: Int,
        tileID: Int
    ) {
        if (actionPerformed == Action.HARVESTING || !exists()) {
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
        val irrigationMissedInfluence = applyMoisturePenaltyIfNeeded(moisture, yearTick)

        val actionsNotPerformed = missed(yearTick, irrigationMissedInfluence)
        if (actionsNotPerformed.isNotEmpty()) Logger.actionNotPerformed(tileID, actionsNotPerformed)

        applyIncidentsToHarvestEstimate(yearTick)

        if (oldHarvestEstimate != harvestEstimate) {
            Logger.changedHarvestEstimate(tileID, harvestEstimate, type)

            if (harvestEstimate < 1 && data.tileType == PlantTile.FIELD) {
                sowTime = SOW_TIME_NO_PLANT
                harvestTime = yearTick
            }
        }

        resetForNextTick()
    }

    private fun applyMoisturePenaltyIfNeeded(moistureFertile: Int, yearTick: Int): Boolean {
        val estimate = harvestEstimate
        if (moistureFertile > 0) {
            repeat((data.moistureMin - moistureFertile) / MOISTURE_LOW_DIVISOR) {
                harvestEstimate = max(harvestEstimate - MOISTURE_LOW_PENALTY, 0)
            }
        } else {
            harvestEstimate = 0
            if (harvestTime <= 0) harvestTime = yearTick
        }
        return estimate != harvestEstimate
    }

    private fun missed(yearTick: Int, irrigationMissedInfluence: Boolean): List<Action> {
        if (sowTime == SOW_TIME_NO_PLANT) return emptyList()
        return listOfNotNull(
            weedingMissed(yearTick),
            cuttingMissed(yearTick),
            mowingMissed(yearTick),
            if (irrigationMissedInfluence) Action.IRRIGATING else null,
            harvestMissed(yearTick),
        )
    }

    private fun weedingMissed(yearTick: Int): Action? {
        if (data.tileType == PlantTile.PLANTATION || !weedable(yearTick)) return null

        val harvestEstimateTemp = harvestEstimate
        harvestEstimate = (harvestEstimate * WEEDING_MISSED_PENALTY_FACTOR).toInt()
        return if (harvestEstimateTemp != harvestEstimate) Action.WEEDING else null
    }

    private fun cuttingMissed(yearTick: Int): Action? {
        if (data.tileType == PlantTile.FIELD) return null
        if (data.cuttingTimes.last() != yearTick || cutPerformed) return null

        val harvestEstimateTemp = harvestEstimate
        harvestEstimate = (harvestEstimate * CUTTING_MISSED_PENALTY_FACTOR).toInt()
        return if (harvestEstimateTemp != harvestEstimate) Action.CUTTING else null
    }

    private fun mowingMissed(yearTick: Int): Action? {
        if (data.tileType == PlantTile.FIELD || !mowable(yearTick)) return null

        val harvestEstimateTemp = harvestEstimate
        harvestEstimate = (harvestEstimate * MOWING_MISSED_PENALTY_FACTOR).toInt()
        return if (harvestEstimateTemp != harvestEstimate) Action.MOWING else null
    }

    private fun harvestMissed(yearTick: Int): Action? {
        if (!harvestable(yearTick)) return null

        val harvestEstimateTemp = harvestEstimate
        harvestEstimate = (harvestEstimate * harvestPenalty(yearTick)).toInt()
        return if (harvestEstimateTemp != harvestEstimate) Action.HARVESTING else null
    }

    private fun applyIncidentsToHarvestEstimate(yearTick: Int) {
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
                    if (harvestTime <= 0) harvestTime = yearTick
                    sowTime = SOW_TIME_NO_PLANT
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
            yearTick in yearTickRangeSet((harvestTime % YEAR_TICK_MAX) + 1, harvestTime + FALLOW_TICKS)
    }

    /**
     * Checks if the plant can be harvested
     */
    fun harvestable(yearTick: Int): Boolean {
        val fullHarvestRange = yearTickRangeSet(
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
        return !cutPerformed &&
            harvestEstimate > 0 &&
            data.cuttingTimes.contains(yearTick)
    }

    /**
     * Checks if the plant can be mowed
     */
    fun mowable(yearTick: Int): Boolean {
        return actionPerformed != Action.MOWING &&
            harvestEstimate > 0 &&
            mowedFor <= 0 &&
            data.mowingTimes.contains(yearTick)
    }

    /**
     * Checks if the plant can be weeded
     */
    fun weedable(yearTick: Int): Boolean {
        return actionPerformed != Action.WEEDING &&
            sowTime > 0 &&
            harvestEstimate > 0 &&
            data.weedingTimes.map { yearTickFix(it + sowTime) }
                .filterNot { it == sowTime }
                .contains(yearTick)
    }

    private fun harvest(yearTick: Int): Int {
        harvestTime = yearTick
        sowTime = SOW_TIME_NO_PLANT
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
        if (action == Action.CUTTING) cutPerformed = true
        return if (action == Action.HARVESTING) harvest(yearTick) else null
    }

    /**
     * Checks if the plant is sown or if it is a plantation plant
     */
    fun isSown(): Boolean {
        return data.tileType == PlantTile.PLANTATION || sowTime > 0
    }

    /**
     * Returns if the plant exists
     */
    fun exists(): Boolean = sowTime != SOW_TIME_NO_PLANT

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
        if (yearTick == PLANTATION_HARVEST_RESET &&
            data.tileType == PlantTile.PLANTATION &&
            sowTime != SOW_TIME_NO_PLANT
        ) {
            oldHarvestEstimate = data.initialHarvestEstimate
            harvestEstimate = data.initialHarvestEstimate
            harvestTime = 0
            cutPerformed = false
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
