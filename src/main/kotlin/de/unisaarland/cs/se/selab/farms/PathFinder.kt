package de.unisaarland.cs.se.selab.farms
import board.Tile
import board.BoardData
import board.TileType

class PathFinder {
    fun reachable(
        src: Tile,
        dest: Tile,
        farmId: Int,
        board: BoardData
    ): Boolean {
        val stack = mutableListOf(src)
        var found = false
        val explored = mutableListOf<Tile>()
        while (stack.isNotEmpty() &&  !found) {
            val current = stack[0]
            val neigbors = board.neighbors(1, current)
            for (neigbor in neigbors) {
                if (neigbor == dest) {
                    found = true
                    break
                }
                if (neigbor.type in listOf(TileType.ROAD, TileType.VILLAGE, TileType.MEADOW) && neigbor !in explored) {
                    stack.add(neigbor)
                } else if (neigbor.type in listOf(TileType.FIELD, TileType.PLANTAGE, TileType.FARMSTEAD) && neigbor.farmID == farmId) {
                    stack.add(neigbor)
                }
            }
            stack.removeAt(0)
            explored.add(current)
        }
        return found
    }
}
