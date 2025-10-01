package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.board.Coordinate
import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.board.TileType
import de.unisaarland.cs.se.selab.clouds.Cloud
import de.unisaarland.cs.se.selab.clouds.CloudData
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class VillageCreationTest {

    @Test
    fun test1() {
        val clouds = CloudData(0, mutableListOf())
        val coord = Coordinate(0, 0)
        val tile = Tile(0, coord, null, false, null, TileType.ROAD)
        val incident = CityExpansion(0, 0, tile, clouds)
        incident.execute()
        assertEquals(TileType.VILLAGE, tile.type)
    }

    @Test
    fun test2() {
        val clouds = CloudData(1, mutableListOf(Cloud(0, -1, 0, 1000, 10)))
        val coord = Coordinate(0, 0)
        val tile = Tile(0, coord, null, false, null, TileType.ROAD)
        val incident = CityExpansion(0, 0, tile, clouds)
        incident.execute()
        assertEquals(TileType.VILLAGE, tile.type)
        assertTrue(clouds.clouds.isEmpty())
    }
}
