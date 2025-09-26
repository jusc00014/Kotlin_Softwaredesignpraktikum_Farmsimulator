package de.unisaarland.cs.se.selab.farms

import de.unisaarland.cs.se.selab.board.Coordinate
import de.unisaarland.cs.se.selab.board.Field
import de.unisaarland.cs.se.selab.board.Plantation
import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.board.TileType
import de.unisaarland.cs.se.selab.parser.MapParser
import de.unisaarland.cs.se.selab.plants.PlantType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertTrue

class FarmHandlerTest {

    private lateinit var farmHandler: FarmHandler
    private lateinit var pathFinder: PathFinder

    /*
    private val tile1 = Plantation(
        1,
        Coordinate(0, 2),
        null,
        1,
        TileType.FIELD,
        3000,
        Plant(PlantType.APPLE, Constants.apple, 1)
    )
    private val tile2 = Plantation(
        2,
        Coordinate(2, 4),
        null,
        1,
        TileType.FIELD,
        3000,
        Plant(PlantType.APPLE, Constants.apple, 1)
    )
    private val tile8 = Field(
        8,
        Coordinate(0, 0),
        null,
        1,
        TileType.FIELD,
        3000,
        Plant(PlantType.POTATO, Constants.potato, 1),
        setOf(PlantType.POTATO, PlantType.PUMPKIN)
    )
    private val tile5 = Field(
        5,
        Coordinate(0, 4),
        null,
        2,
        TileType.FIELD,
        3000,
        Plant(PlantType.POTATO, Constants.potato, 1),
        setOf(PlantType.POTATO, PlantType.PUMPKIN)
    )
    private val tile3 = Field(
        3,
        Coordinate(0, 6),
        null,
        1,
        TileType.FIELD,
        3000,
        Plant(PlantType.POTATO, Constants.potato, 1),
        emptySet()
    )
    private val tile4 = Field(
        4,
        Coordinate(2, 0),
        null,
        1,
        TileType.FIELD,
        3000,
        Plant(PlantType.POTATO, Constants.potato, 1),
        setOf(PlantType.POTATO)
    )
    private val tile6 = Field(
        6,
        Coordinate(2, 6),
        null,
        1,
        TileType.FIELD,
        3000,
        Plant(PlantType.POTATO, Constants.potato, 1),
        setOf(PlantType.POTATO)
    )
    private val tile7 = Field(
        7,
        Coordinate(2, 2),
        null,
        1,
        TileType.FIELD,
        3000,
        Plant(PlantType.POTATO, Constants.potato, 1),
        setOf(PlantType.PUMPKIN)
    ) */
    private val tile9 = Tile(
        9,
        Coordinate(1, 3),
        null,
        true,
        1,
        TileType.FARMSTEAD
    )
    private val mapParser = MapParser(mutableMapOf())
    private val plantData = mapParser.parse("src/systemtest/resources/example/map.json", 0).second

    private val tile00: Field = mock()
    private val tile04: Field = mock()
    private val tile06: Field = mock()
    private val tile20: Field = mock()
    private val tile22: Field = mock()
    private val tile26: Field = mock()
    private val tile02: Plantation = mock()
    private val tile24: Plantation = mock()
    private val machines =
        mapOf(
            Pair(1, Machine(1, listOf(Action.SOWING), listOf(PlantType.POTATO), duration = 13, tile9, 0)),
            Pair(2, Machine(2, listOf(Action.SOWING), listOf(PlantType.PUMPKIN), duration = 13, tile9, 0))
        )
    private val farm = Farm(
        1,
        listOf(9),
        listOf(3, 4, 5, 6, 7, 8),
        listOf(1, 2),
        listOf(1, 2),
        mutableListOf(SowingPlan(1, 8, PlantType.POTATO, listOf(4, 5, 6)))
    )

    @BeforeEach
    fun setUp() {
        pathFinder = mock()
        whenever(tile00.sowablePlants(9, plantData))
            .thenReturn(listOf(PlantType.POTATO, PlantType.PUMPKIN))
        whenever(tile04.sowablePlants(9, plantData))
            .thenReturn(listOf(PlantType.POTATO, PlantType.PUMPKIN))
        whenever(tile06.sowablePlants(9, plantData))
            .thenReturn(emptyList())
        whenever(tile20.sowablePlants(9, plantData))
            .thenReturn(listOf(PlantType.POTATO))
        whenever(tile22.sowablePlants(9, plantData))
            .thenReturn(listOf(PlantType.PUMPKIN))
        whenever(tile26.sowablePlants(9, plantData))
            .thenReturn(listOf(PlantType.POTATO))
        farmHandler = FarmHandler(mapOf(Pair(1, farm)), plantData, machines, pathFinder)
    }

    @Test
    fun assembleSowableFields() {
        val sowFields = farmHandler.assembleSowableFields(
            listOf(3, 4, 6, 7, 8),
            mapOf(
                Pair(1, tile02),
                Pair(2, tile24),
                Pair(3, tile06),
                Pair(4, tile20),
                Pair(5, tile04),
                Pair(6, tile26),
                Pair(7, tile22),
                Pair(8, tile00)
            ),
            9
        )
        assertTrue {
            sowFields == mapOf(
                Pair(PlantType.POTATO, mutableListOf(tile20, tile26, tile00)),
                Pair(PlantType.WHEAT, mutableListOf()),
                Pair(PlantType.OAT, mutableListOf()),
                Pair(PlantType.PUMPKIN, mutableListOf(tile22, tile00)),
                Pair(PlantType.APPLE, mutableListOf()),
                Pair(PlantType.ALMOND, mutableListOf()),
                Pair(PlantType.CHERRY, mutableListOf()),
                Pair(PlantType.GRAPE, mutableListOf())
            )
        }
    }
}
