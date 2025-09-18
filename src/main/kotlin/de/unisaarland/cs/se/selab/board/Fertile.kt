package de.unisaarland.cs.se.selab.board
import de.unisaarland.cs.se.selab.farms.Action
import de.unisaarland.cs.se.selab.plants.Plant
import de.unisaarland.cs.se.selab.plants.PlantData
import de.unisaarland.cs.se.selab.plants.PlantType

abstract class Fertile(
    id: Int,
    coord: Coordinate,
    airflow: Direction?,
    farmID: Int,
    type: TileType,
    private val moistureCapacity: Int,
    var plant: Plant,
): Tile(id, coord, airflow, false, farmID, type)
{
    var sunhours: Int = -1
    private val moisture: Int = moistureCapacity
    var drought: Boolean = false

    fun resetForNextTick() {
        TODO()
    }

    fun loseMoisture(): Boolean {
        TODO()
    }

    fun irrigatable(yearTick: Int): Boolean {
        TODO()
    }

    fun increaseMoisture(amount: Int): Int {
        TODO()
    }

    fun updateHarvestEstimate(yearTick: Int): Int {
        TODO()
    }

    fun performAction(action: Action, yearTick: Int): Int? {
        TODO()
    }

    abstract fun performableActions(yearTick: Int): List<Action>

    abstract fun stampede(): Boolean

    override fun isFertile(): Boolean {
        return type == TileType.PLANTATION || type == TileType.FIELD
    }
}
