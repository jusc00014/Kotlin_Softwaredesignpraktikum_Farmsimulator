package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.board.TileType
import de.unisaarland.cs.se.selab.clouds.CloudData
import de.unisaarland.cs.se.selab.logger.Logger

const val STEP_DEFAULT = 10

/**
 * CLoudCreation inherits from Incident and overrides execute to implement its own logic
 */
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
        val affected = tiles.filter { it.type != TileType.VILLAGE }.sortedBy { it.id }
        Logger.incidentExecuted(id, this, affected.map { it.id })
        for (tile in affected) {
            val newCloud = cloudData.createCloud(duration, amount, tile.id, STEP_DEFAULT)
            cloudData.mergeIfNecessary(newCloud)
        }
    }

    //
    override fun toString(): String {
        return "CLOUD_CREATION"
    }
    //
}
