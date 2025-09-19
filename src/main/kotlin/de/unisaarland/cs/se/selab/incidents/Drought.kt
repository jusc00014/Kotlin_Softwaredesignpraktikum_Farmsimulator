package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.board.Fertile
import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.logger.Logger

/**
 * Drought inherits from drought incidents and sets moisture 0*/
class Drought(id: Int, tick: Int, private val affectedTiles: Set<Tile>): Incident(id, tick) {

    override fun execute() {
        val tiles = mutableListOf<Fertile>()
        for (tile in affectedTiles) {
            val tileToAdd = tile.asFertile() ?: continue
            tiles.add(tileToAdd)
        }
        tiles.forEach { tile -> tile.drought = true }
        Logger.incidentExecuted(id, this, tiles.map { it.id }.sorted())
    }
}