package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.board.TileType
import de.unisaarland.cs.se.selab.clouds.CloudData

const val STEP_DEFAULT = 10

class CloudCreation(id: Int, tick: Int, private val duration: Int, private val amount: Int,
                    private val tiles: Set<Tile>, private val clouds: CloudData) : Incident(id, tick) {
    //
    override fun execute() {
        for(tile in tiles) {
            if (tile.type != TileType.VILLAGE) {
                val newCloud = clouds.createCloud(duration, amount, tile.id, STEP_DEFAULT)
                clouds.mergeIfNecessary(newCloud)
            }
        }
    }
    //
}