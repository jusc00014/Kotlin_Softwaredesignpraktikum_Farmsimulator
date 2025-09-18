package de.unisaarland.cs.se.selab.board

import de.unisaarland.cs.se.selab.board.Fertile
import de.unisaarland.cs.se.selab.board.Tile

class BoardData(val idToTile: Map<Int, Tile>) {
    fun getTileById(id: Int): Tile? {
        return idToTile[id]
    }
    fun getFertiles(): Map<Int, Fertile> {
        TODO()
    }
    fun neighbors(radius: Int, tile: Tile): List<Tile> {
        TODO()
    }
}
