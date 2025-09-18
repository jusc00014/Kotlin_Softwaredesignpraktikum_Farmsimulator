package board

class BoardData(idToTile: Map<Int, Tile>) {
    fun getTileById(id: Int): Tile? {
        return idToTile[id]
    }
    fun getFertiles(): Map<Int, Fertile> {
        TODO()
    }
}
