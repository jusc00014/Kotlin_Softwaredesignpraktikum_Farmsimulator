package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.board.Fertile
import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.logger.Logger

/**
 * class AnimalAttack inheriting from Incident
 * implements execute and calls stampede on all tiles that are Fertile and either Field or Plantation*/
class AnimalAttack(id: Int, tick: Int, val affectedTiles: Set<Tile>) : Incident(id, tick) {

    override fun execute() {
        val tiles: MutableList<Fertile> = mutableListOf()
        for (tile in affectedTiles) {
            val fertile = tile.asFertile() ?: continue
            tiles.add(fertile)
            fertile.stampede(this)
        }
        Logger.incidentExecuted(id, this, tiles.map { it.id }.sorted())
    }
    override fun toString(): String {
        return "ANIMAL_ATTACK"
    }
}
