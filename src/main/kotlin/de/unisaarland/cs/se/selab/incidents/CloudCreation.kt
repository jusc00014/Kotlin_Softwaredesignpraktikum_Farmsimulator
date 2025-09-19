package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.board.TileType
import de.unisaarland.cs.se.selab.clouds.CloudData
import de.unisaarland.cs.se.selab.logger.Logger

const val STEP_DEFAULT = 10

/**
 * CLoudCreation inherits from Incident and overrides execute to implement its own logic*/
class CloudCreation(
    id: Int,
    tick: Int,
    private val duration: Int,
    private val amount: Int,
    val tiles: Set<Tile>,
    private val cloudData: CloudData
) : Incident(id, tick) {
    //
    override fun execute() {
        val tileIds = mutableListOf<Int>()
        for (tile in tiles) {
            if (tile.type != TileType.VILLAGE) {
                val newCloud = cloudData.createCloud(duration, amount, tile.id, STEP_DEFAULT)
                cloudData.mergeIfNecessary(newCloud)
                tileIds.add(tile.id)
            }
        }
        Logger.incidentExecuted(id, this, tileIds)
    }
    //
}
