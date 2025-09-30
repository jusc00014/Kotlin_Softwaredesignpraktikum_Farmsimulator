package de.unisaarland.cs.se.selab.board

import java.util.LinkedList
import java.util.Queue
import java.util.SortedMap

/**
 * Dataclass holding all Tiles and providing functions operating on these.
 */
class BoardData(idToTile: Map<Int, Tile>) {
    private val idToTile: SortedMap<Int, Tile> = idToTile.toSortedMap()
    private val coordToId: Map<Coordinate, Int> = idToTile.values.associate { Pair(it.coord, it.id) }

    private fun getCoordNeighbours(radius: Int, coordinate: Coordinate): Set<Pair<Int, Coordinate>> {
        // Direction NW, NE, SE, SW
        val coords = mutableSetOf(
            radius to coordinate,
            radius to Coordinate(coordinate.x - 1, coordinate.y - 1),
            radius to Coordinate(coordinate.x + 1, coordinate.y - 1),
            radius to Coordinate(coordinate.x + 1, coordinate.y + 1),
            radius to Coordinate(coordinate.x - 1, coordinate.y + 1),
        )

        // Direction N, E, S, W
        if (coordinate.x % 2 == 0) {
            coords.addAll(
                setOf(
                    radius to Coordinate(coordinate.x, coordinate.y + 2),
                    radius to Coordinate(coordinate.x + 2, coordinate.y),
                    radius to Coordinate(coordinate.x, coordinate.y - 2),
                    radius to Coordinate(coordinate.x - 2, coordinate.y),
                )
            )
        }
        return coords
    }
    private fun coordNeighbours(radius: Int, coordinate: Coordinate): Set<Coordinate> {
        val toHandle: Queue<Pair<Int, Coordinate>> = LinkedList()
        val handled = mutableSetOf<Coordinate>()
        toHandle.addAll(getCoordNeighbours(radius - 1, coordinate))
        while (toHandle.isNotEmpty()) {
            val radCoord = toHandle.remove()
            if (handled.contains(radCoord.second)) {
                continue
            }
            if (radCoord.first < 1) {
                handled.add(radCoord.second)
                continue
            }
            handled.add(radCoord.second)
            toHandle.addAll(getCoordNeighbours(radCoord.first - 1, radCoord.second))
        }
        return handled
    }

    /**
     * Get all neighbouring tiles in $radius around the $tile (tile excluded)
     */
    fun neighbors(radius: Int, tile: Tile, excludeSelf: Boolean = false): List<Tile> {
        if (radius < 1) {
            return if (excludeSelf) emptyList() else listOf(tile)
        }
        return coordNeighbours(radius, tile.coord)
            .mapNotNull { getTileByCoord(it) }
            .sortedBy { it.id }
            .filterNot { excludeSelf && it == tile }
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
        return idToTile.values.mapNotNull { it.asFertile() }.associateBy { it.id }
    }

    /**
     * Returns all Tiles
     */
    fun getTiles(): List<Tile> {
        return idToTile.values.toList()
    }
}
