package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.logger.Logger

/**
 * Drought inherits from drought incidents and sets moisture 0*/
class Drought(id: Int, tick: Int, val affectedTiles: Set<Tile>) : Incident(id, tick) {

    override fun execute() {
        val tiles = affectedTiles.mapNotNull { it.asFertile() }
        tiles.forEach {
            it.drought = true
            it.plant.addDrought(this)
        }
        Logger.incidentExecuted(id, this, tiles.map { it.id }.sorted())
    }
    override fun toString(): String {
        return "DROUGHT"
    }
}
