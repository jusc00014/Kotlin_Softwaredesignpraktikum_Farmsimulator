package de.unisaarland.cs.se.selab.board
import de.unisaarland.cs.se.selab.farms.Action
import de.unisaarland.cs.se.selab.plants.Plant

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

    fun resetForNextTick() {
        TODO()
    }

    fun loseMoisture(): Boolean {
        TODO()
    }

    fun irrigatable(yearTick: Int): Boolean {
        TODO()
    }

    override fun rain(amount: Int): Int {
        TODO()
    }

    /**
     * Set the moisture to moistureCapacity for Machine Action
     */
    fun irrigate() {
        moisture = moistureCapacity
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
