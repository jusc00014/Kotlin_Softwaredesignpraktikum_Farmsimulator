package de.unisaarland.cs.se.selab.clouds

import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.Coordinate
import de.unisaarland.cs.se.selab.board.Direction
import de.unisaarland.cs.se.selab.board.Fertile
import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.board.TileType
import de.unisaarland.cs.se.selab.logger.Logger

/**
 * Logic class for cloud actions
 */
class CloudHandler(private val cloudData: CloudData, private val board: BoardData) {
    //
    /**
     * Constants
     */
    companion object {
        const val RAIN_LIMIT = 5000
        const val STEPS_DEFAULT = 10
        const val SUNLIGHT_PENALTY = 50
    }
    //
    /**
     * public function called by Simulator. Executes main cloud actions
     */
    fun moveClouds() {
        mutableIterate(cloudData.clouds, ::cloudAct)
        postMovement()
    }
    //
    /**
     * Executes the actions for one cloud
     */
    private fun cloudAct(cloud: Cloud) {
        while (cloud.stepsRemaining > 0) {
            cloud.stepsRemaining -= 1
            if (rainIfPossible(cloud) || moveOneIfPossible(cloud)) { break }
        }
        if (cloud in cloudData.clouds) {
            if (cloud.duration == 1) {
                cloudData.dissipate(cloud)
            } else {
                if (cloud.duration != -1) {
                    cloud.duration -= 1
                }
            }
        }
    }
    //
    /**
     * Raining logic / Executes rain for single cloud for single tile
     */
    private fun rainIfPossible(cloud: Cloud): Boolean {
        // Returns true if Dissipate
        var amount = cloud.waterAmount
        if (amount >= RAIN_LIMIT) {
            val tile = board.getTileById(cloud.location) ?: error("Cant happen1") // redundant case
            val pre = amount
            amount = tile.rain(amount)
            val diff = pre - amount
            if (diff > 0) {
                Logger.logCloudRain(cloud.id, tile.id, diff)
            }
            if (amount == 0) {
                cloudData.dissipate(cloud)
                return true
            } else {
                cloud.waterAmount = amount
            }
        }
        return false
    }
    //
    /**
     * Movement logic / Executes single Move for single Cloud
     */
    private fun moveOneIfPossible(cloud: Cloud): Boolean {
        // Returns true if hitsVillage or Merged or CantMove
        var out = false
        val neighbor = getNeighbor(cloud.location)
        if (neighbor != null) {
            Logger.logCloudMovement(cloud.id, cloud.waterAmount, cloud.location, neighbor.id)
            decreaseSunlight(cloud)
            cloud.location = neighbor.id
            if (neighbor.type == TileType.VILLAGE) {
                cloudData.stuckOnVillage(cloud)
                out = true
            }
        } else {
            out = true
        }
        return out
    }
    //
    /**
     * gets neighbor along airflow of a Tile or null if no airflow or no neighbor
     */
    private fun getNeighbor(tileId: Int): Tile? {
        val tile = board.getTileById(tileId) ?: error("Cant happen2") // redundant case
        val coordinate: Coordinate
        val x = tile.coord.x
        val y = tile.coord.y
        val airflow = tile.airflow ?: return null
        coordinate = when (airflow) {
            Direction.NORTH -> Coordinate(x, y - 2)
            Direction.NORTHEAST -> Coordinate(x + 1, y - 1)
            Direction.EAST -> Coordinate(x + 2, y)
            Direction.SOUTHEAST -> Coordinate(x + 1, y + 1)
            Direction.SOUTH -> Coordinate(x, y + 2)
            Direction.SOUTHWEST -> Coordinate(x - 1, y + 1)
            Direction.WEST -> Coordinate(x - 2, y)
            Direction.NORTHWEST -> Coordinate(x - 1, y - 1)
        }
        val neighbor = board.getTileByCoord(coordinate)
        return neighbor
    }
    //
    /**
     * Responsible for sunlight reduction after within movement
     */
    private fun decreaseSunlight(cloud: Cloud) {
        val tile = board.getTileById(cloud.location) ?: error("Cant happen3") // Redundant case
        if (tile is Fertile && tile.type != TileType.VILLAGE) {
            tile.sunhours -= 3
            if (tile.sunhours < 0) { tile.sunhours = 0 }
            Logger.logSunlightOnTile(tile.id, tile.sunhours)
        }
    }
    //
    /**
     * Resets stepsRemaining & applies final sunhour penalties
     */
    private fun postMovement() {
        val loggingList = mutableSetOf<Triple<Int, Int, Int>>()
        for (cloud in cloudData.clouds) {
            cloud.stepsRemaining = STEPS_DEFAULT
            val tile = board.getTileById(cloud.location) ?: error("Cant happen4") // Redundant case
            if (tile is Fertile) {
                tile.sunhours -= SUNLIGHT_PENALTY
                if (tile.sunhours < 0) { tile.sunhours = 0 }
                loggingList.add(Triple(cloud.id, cloud.location, tile.sunhours))
            }
        }
        val finalLoggingList = loggingList.sortedBy { it.second }
        finalLoggingList.forEach { Logger.logCloudPosition(it.first, it.second, it.third) }
    }
    //
    /**
     * My amazing mutable Iterator. Allows for altering of a List within its Iteration
     */
    private fun <T : Any> mutableIterate(list: MutableList<T>, action: (T) -> Unit) {
        val processed = mutableSetOf<T>()
        while (true) {
            val next = list.firstOrNull { it !in processed } ?: break
            processed.add(next)
            action(next)
        }
    }
    //
}
