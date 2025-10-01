package de.unisaarland.cs.se.selab.farms
import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.Tile
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
        val stack = ArrayDeque<Tile>()
        stack.add(src)
        val explored = HashSet<Int>()
        explored.add(src.id)
        while (stack.isNotEmpty()) {
            val current = stack.removeFirst()
            val neigbors = board.neighbors(1, current, true).filter { !explored.contains(it.id) }
            for (neigbor in neigbors) {
                if (neigbor == dest) {
                    return true
                }
                val canEnter = when (neigbor.type) {
                    TileType.ROAD, TileType.MEADOW -> true
                    TileType.VILLAGE -> !harvest
                    TileType.FIELD, TileType.PLANTATION, TileType.FARMSTEAD -> neigbor.farmID == farmId
                    else -> false
                }
                if (canEnter) {
                    stack.add(neigbor)
                    explored.add(neigbor.id)
                }
            }
        }
        return false
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
        val stack = reachableWithinOne(
            src,
            dest,
            farmId,
            board,
            harvest
        )
        if (dest in stack) return true
        for (neigbor in stack) {
            val neigbors = reachableWithinOne(
                neigbor,
                dest,
                farmId,
                board,
                harvest
            )
            if (dest in neigbors) return true
            for (srrc in neigbors) {
                val dests = reachableWithinOne(
                    srrc,
                    dest,
                    farmId,
                    board,
                    harvest
                )
                if (dest in dests) return true
            }
        }
        return false
    }

    /**
     * Travel only one*/
    fun reachableWithinOne(
        src: Tile,
        dest: Tile,
        farmId: Int,
        board: BoardData,
        harvest: Boolean
    ): MutableList<Tile> {
        val stack = mutableListOf<Tile>()
        val neigbors = board.neighbors(1, src, true)
        for (neigbor in neigbors) {
            if (neigbor == dest) {
                return mutableListOf(neigbor)
            }
            if (
                neigbor.type in listOf(TileType.ROAD, TileType.MEADOW) ||
                (!harvest && neigbor.type == TileType.VILLAGE)
            ) {
                stack.add(neigbor)
            } else if (
                neigbor.type in listOf(TileType.FIELD, TileType.PLANTATION, TileType.FARMSTEAD) &&
                neigbor.farmID == farmId
            ) {
                stack.add(neigbor)
            }
        }
        return stack
    }

    /**
     * If machine can't go back to its original shed after harvesting*/
    fun findNearestShed(src: Tile, farm: Farm, board: BoardData): Tile? {
        val shedIds = farm.farmsteads.sorted()
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
