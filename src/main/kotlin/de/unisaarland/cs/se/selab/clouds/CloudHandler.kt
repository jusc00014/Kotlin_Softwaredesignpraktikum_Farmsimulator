package de.unisaarland.cs.se.selab.clouds

import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.Coordinate
import de.unisaarland.cs.se.selab.board.Direction
import de.unisaarland.cs.se.selab.board.Fertile
import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.board.TileType
import de.unisaarland.cs.se.selab.logger.Logger

class CloudHandler (private val cloudData: CloudData, private val board: BoardData) {
    //
    fun moveClouds() {
        mutableIterate(cloudData.clouds, ::cloudAct)
        postMovement()
    }
    //
    private fun cloudAct(cloud: Cloud) {
        if (cloud.duration == 0) {
            cloudData.dissipate(cloud)
        }
        else {
            cloud.duration -= 1
            while (cloud.stepsRemaining > 0) {
                cloud.stepsRemaining -= 1
                if (rainIfPossible(cloud) || moveOneIfPossible(cloud)) {break}
            }
        }

    }
    //
    private fun rainIfPossible(cloud: Cloud) : Boolean {
        // Returns true if Dissipate
        var amount = cloud.waterAmount
        if (amount >= 5000) {
            val tile = board.getTileById(cloud.location) ?: error("Cant happen") // redundant case
            amount = tile.rain(amount)
            if (amount <= 0) {
                cloudData.dissipate(cloud)
                return true
            }
            else {
                cloud.waterAmount = amount
            }
        }
        return false
    }
    //
    private fun moveOneIfPossible(cloud: Cloud) : Boolean {
        // Returns true if hitsVillage or Merged or CantMove
        var out = false
        val neighbor = getNeighbor(cloud.location)
        if (neighbor != null) {
            Logger.logCloudMovement(cloud.id, cloud.waterAmount,cloud.location, neighbor.id)
            decreaseSunlight(cloud)
            cloud.location = neighbor.id
            if (neighbor.type == TileType.VILLAGE) {
                cloudData.stuckOnVillage(cloud)
                out = true
            }
        }
        else {
            out = true
        }
        return out
    }
    //
    private fun getNeighbor(tileId: Int) : Tile? {
        val tile = board.getTileById(tileId) ?: error("Cant happen") // redundant case
        val coordinate : Coordinate
        val x = tile.coord.x
        val y = tile.coord.y
        val airflow = tile.airflow ?: return null
        coordinate = when (airflow) {
            Direction.NORTH -> Coordinate(x, y-2)
            Direction.NORTHEAST -> Coordinate(x+1, y-1)
            Direction.EAST -> Coordinate(x+2, y)
            Direction.SOUTHEAST -> Coordinate(x+1, y+1)
            Direction.SOUTH -> Coordinate(x,y+2)
            Direction.SOUTHWEST -> Coordinate(x-1,y+1)
            Direction.WEST -> Coordinate(x-2,y)
            Direction.NORTHWEST -> Coordinate(x-1,y-1)
        }
        val neighbor = board.getTileByCoord(coordinate)
        return neighbor
    }
    //
    private fun decreaseSunlight(cloud: Cloud) {
        val tile = board.getTileById(cloud.location) ?: error("Cant happen") // Redundant case
        if (tile is Fertile && tile.type != TileType.VILLAGE) {
            tile.sunhours -= 3
            if (tile.sunhours < 0) {tile.sunhours = 0}
            Logger.logSunlightOnTile(tile.id, tile.sunhours)
        }
    }
    //
    private fun postMovement() {
        TODO()
    }
    //
    private fun <T: Any> mutableIterate(list: MutableList<T>, action: (T) -> Unit) {
        // Necessary for List altering within their very own Iteration
        val processed = mutableSetOf<T>()
        while (true) {
            val next = list.firstOrNull { it !in processed } ?: break
            processed.add(next)
            action(next)
        }
    }
    //
}