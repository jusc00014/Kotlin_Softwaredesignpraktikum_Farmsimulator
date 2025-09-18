package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.clouds.CloudData

class CloudCreation(id: Int, tick: Int, private val duration: Int, private val amount: Int, private val tiles: Set<Tile>, private val clouds: CloudData) : Incident(id, tick) {
    //
    override fun execute() {
        TODO()
    }
    //
}