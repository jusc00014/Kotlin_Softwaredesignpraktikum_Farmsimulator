package de.unisaarland.cs.se.selab.farms
import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.TileType

/**
 * For machine*/
class PathFinder {

    /**
     * BFS*/
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

                when {
                    neigbor in explored -> {continue}
                    neigbor.type in listOf(TileType.ROAD, TileType.MEADOW) ||
                            (!harvest && neigbor.type == TileType.VILLAGE) -> {
                        stack.add(neigbor)
                    }

                    neigbor.type in listOf(TileType.FIELD, TileType.PLANTATION, TileType.FARMSTEAD) &&
                            neigbor.farmID == farmId -> {
                        stack.add(neigbor)
                    }
                    else -> {}
                }
            }
            stack.removeAt(0)
            explored.add(current)
        }
        return found
    }

    /**
     * If there is no harvest*/
    fun reachable(
        src: Tile,
        dest: Tile,
        farmId: Int,
        board: BoardData,
    ): Boolean {
        return maybeReachable(src, dest, farmId, board, false)
    }

    /**
     * After harvesting*/
    fun reachableWithHarvest(
        src: Tile,
        dest: Tile,
        farmId: Int,
        board: BoardData,
    ): Boolean {
        return maybeReachable(src, dest, farmId, board, true)
    }

    /**
     * If only two tiles can be traversed*/
    fun canContinue(
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
            if (
                neigbor.type in listOf(TileType.ROAD, TileType.MEADOW) ||
                (!harvest && neigbor.type == TileType.VILLAGE)) {
                stack.add(neigbor)
            } else if (
                neigbor.type in listOf(TileType.FIELD, TileType.PLANTATION, TileType.FARMSTEAD) &&
                neigbor.farmID == farmId) {
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

    /**
     * If machine can't go back to its original shed after harvesting*/
    fun findNearestShed(src: Tile, farm: Farm, board: BoardData): Tile? {
        val shedIds = farm.farmsteads
        for (shedId in shedIds) {
            val farmstead = board.getTileById(shedId) ?: continue
            if (farmstead.shed) {
                if (reachableWithHarvest(src, farmstead, farm.id, board)) {
                    return farmstead
                }
            }
        }
        return null
    }
}
