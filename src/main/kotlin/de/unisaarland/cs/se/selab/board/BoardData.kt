package de.unisaarland.cs.se.selab.board

import java.util.SortedMap

/**
 * Dataclass holding all Tiles and providing functions operating on these.
 */
class BoardData(idToTile: Map<Int, Tile>) {
    private val idToTile: SortedMap<Int, Tile> = idToTile.toSortedMap()
    private val coordToId: Map<Coordinate, Int> = idToTile.values.associate { Pair(it.coord, it.id) }

    private fun coordNeighbours(radius: Int, coordinate: Coordinate): Set<Coordinate> {
        // Direction NW, NE, SE, SW
        val coords = mutableSetOf<Coordinate>(
            Coordinate(coordinate.x - 1, coordinate.y - 1),
            Coordinate(coordinate.x + 1, coordinate.y - 1),
            Coordinate(coordinate.x + 1, coordinate.y + 1),
            Coordinate(coordinate.x - 1, coordinate.y + 1),
        )

        // Direction N, E, S, W
        if (coordinate.x % 2 == 0) {
            coords.addAll(
                setOf(
                    Coordinate(coordinate.x, coordinate.y + 2),
                    Coordinate(coordinate.x + 2, coordinate.y),
                    Coordinate(coordinate.x, coordinate.y - 2),
                    Coordinate(coordinate.x - 2, coordinate.y),
                )
            )
        }

        if (radius > 1) {
            val coordsTemp = mutableSetOf<Coordinate>()
            for (coord in coords) {
                coordsTemp.addAll(coordNeighbours(radius - 1, coord))
            }
            coordsTemp.remove(coordinate)
            coords.addAll(coordsTemp)
        }

        return coords
    }

    /**
     * Get all neighbouring tiles in $radius around the $tile (tile excluded)
     */
    fun neighbors(radius: Int, tile: Tile): List<Tile> {
        if (radius < 1) {
            return emptyList()
        }

        return coordNeighbours(radius, tile.coord)
            .filter { it != tile.coord }
            .mapNotNull { getTileByCoord(it) }
            .sortedBy { it.id }
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
}
