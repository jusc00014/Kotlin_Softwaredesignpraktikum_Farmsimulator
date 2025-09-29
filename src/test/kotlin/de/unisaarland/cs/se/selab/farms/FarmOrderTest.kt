package de.unisaarland.cs.se.selab.farms

import de.unisaarland.cs.se.selab.Constants
import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.Coordinate
import de.unisaarland.cs.se.selab.board.Fertile
import de.unisaarland.cs.se.selab.board.Field
import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.board.TileType
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.parser.MapParser
import de.unisaarland.cs.se.selab.plants.Plant
import de.unisaarland.cs.se.selab.plants.PlantType
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class FarmOrderTest {
    val tile1 = Field(
        1,
        Coordinate(4, 0),
        null,
        1,
        TileType.FIELD,
        3000,
        Plant(PlantType.POTATO, Constants.potato, 0),
        setOf(PlantType.POTATO)
    )
    val tile2 = Field(
        2,
        Coordinate(6, 2),
        null,
        1,
        TileType.FIELD,
        3000,
        Plant(PlantType.POTATO, Constants.potato, 0),
        setOf(PlantType.OAT)
    )
    val tile3 = Field(
        3,
        Coordinate(8, 2),
        null,
        1,
        TileType.FIELD,
        3000,
        Plant(PlantType.POTATO, Constants.potato, 0),
        setOf(PlantType.OAT)
    )
    val tile4 = Field(
        4,
        Coordinate(6, 4),
        null,
        1,
        TileType.FIELD,
        3000,
        Plant(PlantType.POTATO, Constants.potato, 0),
        setOf(PlantType.OAT)
    )
    private val tile5 = Tile(
        5,
        Coordinate(5, 3),
        null,
        true,
        1,
        TileType.FARMSTEAD
    )
    private val tile6 = Tile(
        6,
        Coordinate(9, 1),
        null,
        true,
        1,
        TileType.FARMSTEAD
    )
    private val tile7 = Tile(
        7,
        Coordinate(1, 1),
        null,
        true,
        1,
        TileType.FARMSTEAD
    )
    private val tile8 = Tile(
        8,
        Coordinate(2, 2),
        null,
        false,
        null,
        TileType.ROAD
    )
    private val tile9 = Tile(
        9,
        Coordinate(3, 1),
        null,
        false,
        null,
        TileType.ROAD
    )
    private val tile10 = Tile(
        10,
        Coordinate(3, 3),
        null,
        false,
        null,
        TileType.ROAD
    )
    private val tile11 = Tile(
        11,
        Coordinate(4, 4),
        null,
        false,
        null,
        TileType.ROAD
    )
    private val tile12 = Tile(
        12,
        Coordinate(5, 1),
        null,
        false,
        null,
        TileType.ROAD
    )
    private val tile13 = Tile(
        13,
        Coordinate(6, 0),
        null,
        false,
        null,
        TileType.ROAD
    )
    val machine1 = Machine(
        1,
        Action.entries,
        PlantType.entries,
        6,
        tile7
    )
    val machine2 = Machine(
        2,
        Action.entries,
        PlantType.entries,
        8,
        tile5
    )
    val machine3 = Machine(
        3,
        Action.entries,
        PlantType.entries,
        6,
        tile6
    )
    val plan1 = SowingPlan(
        1,
        1,
        PlantType.POTATO,
        listOf(1)
    )
    val plan2 = SowingPlan(
        2,
        0,
        PlantType.OAT,
        listOf(2, 3, 4)
    )
    val farm = Farm(
        1,
        listOf(5, 6, 7),
        listOf(1, 2, 3, 4),
        emptyList(),
        listOf(1, 2, 3),
        mutableListOf(plan1, plan2)
    )
    private val mapParser = MapParser(mutableMapOf())
    private val plantData = mapParser.parse("src/systemtest/resources/example/map.json", 0).second
    val pathFinder = PathFinder()
    val board =
        BoardData(
            mapOf(
                1 to tile1, 2 to tile2, 3 to tile3, 4 to tile4,
                5 to tile5, 6 to tile6, 7 to tile7, 8 to tile8, 9 to tile9,
                10 to tile10, 11 to tile11, 12 to tile12, 13 to tile13
            )
        )

    @Test
    fun sowingInRightOrder() {
        val yearTick = 7
        val farmer = FarmHandler(
            mapOf(1 to farm),
            plantData,
            mapOf(1 to machine1, 2 to machine2, 3 to machine3),
            pathFinder
        )
        val fertiles = board.getFertiles()
        Logger.farmStartAction(farm.id)
        val remainingMachines = farmer.assembleMachines(farm).sortedBy { it.duration }.toMutableList()
        val sowFields = farmer.assembleSowableFields(farm.fields, fertiles, yearTick)
        val finishedFields = mutableMapOf<Int, Fertile>()
        farmer.sow(
            sowFields,
            farm,
            remainingMachines,
            finishedFields = finishedFields,
            board = board,
            yearTick = yearTick,
            fertiles = fertiles
        )
        assertTrue { finishedFields.isNotEmpty() }
    }
}
