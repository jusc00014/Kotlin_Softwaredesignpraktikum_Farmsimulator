package de.unisaarland.cs.se.selab.parser

import de.unisaarland.cs.se.selab.Constants
import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.Coordinate
import de.unisaarland.cs.se.selab.board.Field
import de.unisaarland.cs.se.selab.board.Plantation
import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.board.TileType
import de.unisaarland.cs.se.selab.plants.Plant
import de.unisaarland.cs.se.selab.plants.PlantType
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class FarmParserTest {
    lateinit var boardData: BoardData
    val tile0 = Field(
        0,
        Coordinate(0, -2),
        null,
        1,
        TileType.FIELD,
        10000,
        Plant(PlantType.POTATO, Constants.potato, 1),
        setOf(PlantType.OAT, PlantType.POTATO)
    )
    val tile1 = Field(
        1,
        Coordinate(-2, 0),
        null,
        0,
        TileType.FIELD,
        10000,
        Plant(PlantType.WHEAT, Constants.wheat, 1),
        setOf(PlantType.PUMPKIN, PlantType.WHEAT, PlantType.POTATO)
    )
    val tile2 = Tile(
        2,
        Coordinate(0, 0),
        null,
        false,
        null,
        TileType.ROAD
    )
    val tile3 = Plantation(
        3,
        Coordinate(2, 0),
        null,
        1,
        TileType.PLANTATION,
        8000,
        Plant(PlantType.APPLE, Constants.apple, 1)
    )
    val tile4 = Tile(
        4,
        Coordinate(-1, 1),
        null,
        true,
        0,
        TileType.FARMSTEAD
    )
    val tile5 = Tile(
        5,
        Coordinate(1, 1),
        null,
        true,
        1,
        TileType.FARMSTEAD
    )
    val tile6 = Tile(
        6,
        Coordinate(0, 2),
        null,
        false,
        null,
        TileType.ROAD
    )
    val tile7 = Plantation(
        7,
        Coordinate(-2, 4),
        null,
        0,
        TileType.PLANTATION,
        8000,
        Plant(PlantType.APPLE, Constants.apple, 1)
    )

    @BeforeEach
    fun setUp() {
        boardData = BoardData(
            mapOf(
                0 to tile0,
                1 to tile1,
                2 to tile2,
                3 to tile3,
                4 to tile4,
                5 to tile5,
                6 to tile6,
                7 to tile7
            )
        )
    }

    @Test
    fun parseValidFarm() {
        val farmJson = "src/systemtest/resources/farmParser/farms.json"
        val farmParser = FarmParser()
        var parseWorked = true
        try {
            farmParser.parse(farmJson, boardData)
        } catch (_: IllegalArgumentException) {
            parseWorked = false
        }
        assertTrue(parseWorked, "Parsing failed. Not good.")
    }
}
