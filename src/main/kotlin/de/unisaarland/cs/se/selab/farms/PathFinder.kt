package de.unisaarland.cs.se.selab.farms
import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.TileType

class PathFinder {
    private fun maybeReachable(
        src: Tile,
        dest: Tile,
        farmId: Int,
        board: BoardData,
        harvest: Boolean
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
                if (((neigbor.type in listOf(TileType.ROAD, TileType.MEADOW)) || (!harvest && neigbor.type == TileType.VILLAGE)) && neigbor !in explored) {
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

    fun reachable(
        src: Tile,
        dest: Tile,
        farmId: Int,
        board: BoardData,
    ): Boolean {
        return maybeReachable(src, dest, farmId, board, false)
    }

    fun reachableWithHarvest(
        src: Tile,
        dest: Tile,
        farmId: Int,
        board: BoardData,
    ): Boolean {
        return maybeReachable(src, dest, farmId, board, true)
    }

    private fun maybeCanContinue(
        src: Tile,
        dest: Tile,
        farmId: Int,
        board: BoardData,
        harvest: Boolean
    ): Boolean {
        val stack = mutableListOf<Tile>()
        var neigbors = board.neighbors(1, src)
        for (neigbor in neigbors) {
            if (neigbor == dest) {
                return true
            }
            if ((neigbor.type in listOf(TileType.ROAD, TileType.MEADOW)) || (!harvest && neigbor.type == TileType.VILLAGE)) {
                stack.add(neigbor)
            } else if (neigbor.type in listOf(TileType.FIELD, TileType.PLANTAGE, TileType.FARMSTEAD) && neigbor.farmID == farmId) {
                stack.add(neigbor)
            }
        }
        for (neigbor in stack) {
            neigbors = board.neighbors(1, neigbor)
            if (dest in neigbors) {
                return true
            }
        }
        return false
    }
}
