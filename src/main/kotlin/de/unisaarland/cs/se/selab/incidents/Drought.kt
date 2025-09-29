package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.board.Fertile
import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.logger.Logger

/**
 * Drought inherits from drought incidents and sets moisture 0*/
class Drought(id: Int, tick: Int, val affectedTiles: Set<Tile>) : Incident(id, tick) {

    override fun execute() {
        val actuallyAffectedTiles = mutableListOf<Tile>()
        affectedTiles.forEach { if (it.asFertile()?.drought == false) actuallyAffectedTiles.add(it) }
        actuallyAffectedTiles.forEach { if (it is Fertile) it.drought = true }
        Logger.incidentExecuted(id, this, actuallyAffectedTiles.map { it.id }.sorted())
    }
    override fun toString(): String {
        return "DROUGHT"
    }
}
