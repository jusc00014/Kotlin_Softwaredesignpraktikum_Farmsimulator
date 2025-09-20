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
        }
        val tileIds = mutableListOf<Int>()
        for (tile in tiles) {
            if (tile.stampede(this)) {
                tileIds.add(tile.id)
            }
        }
        tileIds.sort()
        Logger.incidentExecuted(id, this, tileIds)
    }
    override fun toString(): String {
        return "ANIMAL_ATTACK"
    }
}
