package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.board.Fertile
import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.logger.Logger

/**
 * BeeHappy incident inherits from Incident
 * overrides execute for itself*/
class BeeHappy(
    id: Int,
    tick: Int,
    val affectedTiles: Set<Tile>,
    val effect: Double,
    private val yearTick: Int
) : Incident(id, tick) {

    override fun execute() {
        val beeHappyTiles: MutableSet<Fertile> = mutableSetOf()
        for (tile in affectedTiles) {
            val maybeTile = tile.asFertile() ?: continue
            beeHappyTiles.add(maybeTile)
        }
        val pollinatableTiles = beeHappyTiles.filter { it.plant.pollinateable(yearTick) }
        pollinatableTiles.forEach { it.plant.addPollination(this) }
        Logger.incidentExecuted(id, this, pollinatableTiles.map { it.id }.sortedBy { it })
    }
    override fun toString(): String {
        return "BEE_HAPPY"
    }
}
