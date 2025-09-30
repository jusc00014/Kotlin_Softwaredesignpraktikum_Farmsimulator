package de.unisaarland.cs.se.selab.board
import de.unisaarland.cs.se.selab.farms.Action
import de.unisaarland.cs.se.selab.incidents.AnimalAttack
import de.unisaarland.cs.se.selab.incidents.Drought
import de.unisaarland.cs.se.selab.plants.Plant
import kotlin.math.max
import kotlin.math.min

const val MOISTURE_TO_LOSE_PLANT_IS_NOT_GROWING = 70
const val MOISTURE_TO_LOSE_PLANT_IS_GROWING = 100

/**
 * abstract class implemented by Fields and Plantations.
 * Used for anything that grows plants and needs to interact with them*/
abstract class Fertile(
    id: Int,
    coord: Coordinate,
    airflow: Direction?,
    farmID: Int,
    type: TileType,
    private val moistureCapacity: Int,
    val plant: Plant,
) : Tile(id, coord, airflow, false, farmID, type) {
    var sunhours: Int = -1
    private var moisture: Int = moistureCapacity
    protected var drought: Boolean = false

    private fun resetForNextTick() {
        if (type == TileType.FIELD) {
            drought = false
        }
    }

    /**
     * Prepares the fertile and plant its holding for the current Tick
     */
    fun prepareCurrentTick(sunHours: Int, yearTick: Int) {
        sunhours = sunHours
        plant.prepareCurrentTick(yearTick)
    }

    /**
     * loses moisture in reduceSoil phase. Returns if our moisture is now below min moisture of the plant*/
    fun loseMoisture(): Boolean {
        val minAllowedMoisture = plant.getMinMoisture()
        moisture -= when {
            (type == TileType.FIELD && plant.getHarvestEstimate() > 0) || (type == TileType.PLANTATION && !drought)
            -> MOISTURE_TO_LOSE_PLANT_IS_GROWING
            else -> MOISTURE_TO_LOSE_PLANT_IS_NOT_GROWING
        }
        moisture = max(0, moisture)
        if ((type == TileType.FIELD && plant.getHarvestEstimate() == 0) || drought) {
            return false
        }
        return minAllowedMoisture > moisture
    }

    /**
     * checks if farm should irrigate on this tile*/
    fun irrigatable(): Boolean {
        if (drought || !plant.isSown()) return false

        return moisture < plant.getMinMoisture()
    }

    override fun rain(amount: Int): Int {
        val moistureToAdd = min(amount, moistureCapacity - moisture)
        moisture += moistureToAdd
        return amount - moistureToAdd
    }

    /**
     * Set the moisture to moistureCapacity for Machine Action
     */
    private fun irrigate() {
        moisture = moistureCapacity
    }

    /**
     * called by boardHandler to update harvest estimates on all Fertile*/
    fun updateHarvestEstimate(yearTick: Int) {
        plant.updateHarvestEstimate(yearTick, sunhours, moisture, id)
        resetForNextTick()
    }

    /**
     * called anytime any action is performed on this tile by machines. Returns harvest*/
    fun performAction(action: Action, yearTick: Int): Int? {
        if (action == Action.IRRIGATING) {
            irrigate()
            return null
        } else {
            return plant.performAction(action, yearTick)
        }
    }

    /**
     * FarmHandler uses this to check if it can do the action it want to perform on this tile
     * @return actions that may be performed on this tile*/
    abstract fun performableActions(yearTick: Int): List<Action>

    /**
     * called by animalAttack on all Fertiles*/
    fun stampede(animalAttack: AnimalAttack): Boolean {
        return plant.addStampede(animalAttack)
    }

    /**
     * called by Drought on all Fertiles
     */
    fun addDrought(drought: Drought) {
        moisture = 0
        this.drought = true
        plant.addDrought(drought)
    }

    /**
     * Getter for drought
     */
    fun hasDrought(): Boolean = drought

    override fun asFertile(): Fertile? {
        if (type != TileType.PLANTATION && type != TileType.FIELD) {
            return null
        }

        return this
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is Fertile) {
            val bool = this.moistureCapacity == other.moistureCapacity && this.plant == other.plant
            return bool && super.equals(other)
        } else {
            return false
        }
    }
}
