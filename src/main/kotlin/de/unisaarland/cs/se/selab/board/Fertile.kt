package de.unisaarland.cs.se.selab.board
import de.unisaarland.cs.se.selab.farms.Action
import de.unisaarland.cs.se.selab.plants.Plant
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
    var plant: Plant,
) : Tile(id, coord, airflow, false, farmID, type) {
    var sunhours: Int = -1
    private var moisture: Int = moistureCapacity
    var drought: Boolean = false

    /**
     * */
    fun resetForNextTick() {
        if (type == TileType.FIELD) {
            drought = false
        }
    }

    /**
     * loses moisture in reduceSoil phase. Returns if our moisture is now below min moisture of the plant*/
    fun loseMoisture(): Boolean {
        val minAllowedMoisture = plant.getMinMoisture()
        if (plant.getHarvestEstimate() > 0) {
            moisture -= MOISTURE_TO_LOSE_PLANT_IS_GROWING
        } else {
            moisture -= MOISTURE_TO_LOSE_PLANT_IS_NOT_GROWING
        }
        if (type == TileType.FIELD) {
            return false
        }
        return minAllowedMoisture > moisture
    }

    /**
     * checks if farm should irrigate on this tile*/
    fun irrigatable(): Boolean {
        return !drought || moisture < plant.getMinMoisture() || plant.getHarvestEstimate() > 0
    }

    override fun rain(amount: Int): Int {
        val moistureToAdd = min(amount, moistureCapacity - moisture)
        return amount - moistureToAdd
    }

    /**
     * Set the moisture to moistureCapacity for Machine Action
     */
    fun irrigate() {
        moisture = moistureCapacity
    }

    /**
     * called by boardHandler to update harvest estimates on all Fertiles*/
    fun updateHarvestEstimate(yearTick: Int) {
        plant.updateHarvestEstimate(yearTick, drought, sunhours, moisture, id)
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
    fun stampede(): Boolean {
        return plant.addStampede()
    }

    override fun asFertile(): Fertile? {
        if (type != TileType.PLANTATION && type != TileType.FIELD) {
            return null
        }

        return this
    }
}
