package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.board.TileType
import de.unisaarland.cs.se.selab.clouds.CloudData
import de.unisaarland.cs.se.selab.logger.Logger

/**
 * CityExpansion inherits from Incident and changes road or fjeld tile on location to village*/
class CityExpansion(
    id: Int,
    tick: Int,
    val affectedTile: Tile,
    private val clouds: CloudData
) : Incident(id, tick) {

    override fun execute() {
        affectedTile.type = TileType.VILLAGE
        Logger.incidentExecuted(id, this, listOf(affectedTile.id))
        affectedTile.type = TileType.VILLAGE
        clouds.checkIfCloudOnNewVillage(affectedTile.id)
    }
    override fun toString(): String {
        return "CITY_EXPANSION"
    }
}
