package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.Constants
import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.Coordinate
import de.unisaarland.cs.se.selab.board.Field
import de.unisaarland.cs.se.selab.board.Plantation
import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.board.TileType
import de.unisaarland.cs.se.selab.clouds.CloudData
import de.unisaarland.cs.se.selab.plants.Plant
import de.unisaarland.cs.se.selab.plants.PlantType
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CloudCreationTest {
    val tile0 = Plantation(
        0,
        Coordinate(0, 0),
        null,
        0,
        TileType.PLANTATION,
        1234,
        Plant(PlantType.GRAPE, Constants.grape, 1)
    )
    val tile1 = Field(
        1,
        Coordinate(2, 0),
        null,
        0,
        TileType.FIELD,
        1234,
        Plant(PlantType.POTATO, Constants.potato, 1),
        setOf(PlantType.POTATO)
    )
    val tile2 = Tile(
        2,
        Coordinate(4, 0),
        null,
        false,
        null,
        TileType.FOREST
    )
    val tile3 = Tile(
        3,
        Coordinate(-1, 1),
        null,
        false,
        null,
        TileType.MEADOW
    )
    val tile4 = Tile(
        4,
        Coordinate(1, 1),
        null,
        false,
        null,
        TileType.VILLAGE
    )
    val tile5 = Tile(
        5,
        Coordinate(1, 1),
        null,
        false,
        null,
        TileType.ROAD
    )
    val tile6 = Tile(
        6,
        Coordinate(0, 14),
        null,
        false,
        null,
        TileType.VILLAGE
    )
    val tile7 = Tile(
        7,
        Coordinate(1, 15),
        null,
        true,
        0,
        TileType.FARMSTEAD
    )
    lateinit var boardData: BoardData

    @BeforeEach
    fun setup() {
        boardData = BoardData(
            mapOf(
                0 to tile0, 1 to tile1, 2 to tile2, 3 to tile3, 4 to tile4,
                5 to tile5, 6 to tile6, 7 to tile7
            )
        )
    }

    @Test
    fun execute() {
        val cloudData = CloudData(0, mutableListOf())
        val cloudCreation = CloudCreation(
            0,
            0,
            -1,
            100000,
            setOf(tile0, tile1, tile2, tile3, tile4, tile5, tile6, tile7),
            cloudData
        )
        val incidentHandler = IncidentHandler(mutableMapOf(0 to listOf(cloudCreation)))
        incidentHandler.executeIncidents(0)
        val actualCloud0 = cloudData.clouds[0]
        val actualCloud1 = cloudData.clouds[1]
        val actualCloud2 = cloudData.clouds[2]
        val actualCloud3 = cloudData.clouds[3]
        val actualCloud4 = cloudData.clouds[4]
        val actualCloud5 = cloudData.clouds[5]
        assertTrue(actualCloud0.location == 0 && actualCloud0.id == 0)
        assertTrue(actualCloud1.location == 1 && actualCloud1.id == 1)
        assertTrue(actualCloud2.location == 2 && actualCloud2.id == 2)
        assertTrue(actualCloud3.location == 3 && actualCloud3.id == 3)
        assertTrue(actualCloud4.location == 5 && actualCloud4.id == 4)
        assertTrue(actualCloud5.location == 7 && actualCloud5.id == 5)
    }
}
