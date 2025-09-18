package de.unisaarland.cs.se.selab.board

import java.util.SortedMap

/**
 * Dataclass holding all Tiles and providing functions operating on these.
 */
class BoardData(idToTile: Map<Int, Tile>) {
    private val idToTile: SortedMap<Int, Tile> = idToTile.toSortedMap()
    private val coordToId: Map<Coordinate, Int> = idToTile.values.associate { Pair(it.coord, it.id) }

    private fun sameDirectNeighbours(coord: Coordinate): Set<Coordinate> {
        return setOf(
            Coordinate(coord.x + 1, coord.y + 1),
            Coordinate(coord.x + 1, coord.y - 1),
            Coordinate(coord.x - 1, coord.y + 1),
            Coordinate(coord.x - 1, coord.y - 1),
        )
    }

    /**
     * Get all neighbouring tiles in $radius around the $tile (tile excluded)
     */
    fun neighbors(radius: Int, tile: Tile): List<Tile> {
        if (radius < 1) {
            return emptyList()
        }

        val evenCoordinates = mutableSetOf<Coordinate>()
        val oddCoordinates = sameDirectNeighbours(tile.coord).toMutableSet()
        val temp = mutableSetOf<Coordinate>()
        repeat(radius) {
            temp.clear()
            evenCoordinates.forEach { temp.addAll(sameDirectNeighbours(it)) }
            evenCoordinates.addAll(temp)
            temp.clear()
            oddCoordinates.forEach { temp.addAll(sameDirectNeighbours(it)) }
            oddCoordinates.addAll(temp)
        }

        return (evenCoordinates + oddCoordinates).mapNotNull { getTileByCoord(it) }.toList()
    }

    /**
     * Getter for tile by its ID
     */
    fun getTileById(id: Int): Tile? {
        return idToTile[id]
    }

    /**
     * Getter for tile by its Coordinate
     */
    fun getTileByCoord(coord: Coordinate): Tile? {
        return idToTile[coordToId[coord] ?: return null]
    }

    /**
     * Returns all Fertile tiles mapped to their IDs
     */
    fun getFertiles(): Map<Int, Fertile> {
        return idToTile.filterValues { it.isFertile() }.mapValues { it as Fertile }
    }
}
