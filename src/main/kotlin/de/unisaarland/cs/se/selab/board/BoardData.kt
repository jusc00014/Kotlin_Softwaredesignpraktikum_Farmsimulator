package board

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
