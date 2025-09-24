package de.unisaarland.cs.se.selab.farms

import de.unisaarland.cs.se.selab.Constants
import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.Coordinate
import de.unisaarland.cs.se.selab.board.Fertile
import de.unisaarland.cs.se.selab.board.Field
import de.unisaarland.cs.se.selab.board.Plantation
import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.board.TileType
import de.unisaarland.cs.se.selab.plants.Plant
import de.unisaarland.cs.se.selab.plants.PlantType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertTrue

class FarmHandlerTest2 {
    private val pathfinder: PathFinder = mock()
    private val tile1 = Plantation(
        1,
        Coordinate(0, 2),
        null,
        1,
        TileType.FIELD,
        3,
        Plant(PlantType.APPLE, Constants.apple, 1)
    )
    private val tile2 = Plantation(
        2,
        Coordinate(2, 4),
        null,
        1,
        TileType.FIELD,
        3,
        Plant(PlantType.APPLE, Constants.apple, 1)
    )
    private val tile8 = Field(
        8,
        Coordinate(0, 0),
        null,
        1,
        TileType.FIELD,
        3,
        Plant(PlantType.POTATO, Constants.potato, 1),
        setOf(PlantType.POTATO, PlantType.PUMPKIN)
    )
    private val tile5 = Field(
        5,
        Coordinate(0, 4),
        null,
        1,
        TileType.FIELD,
        3,
        Plant(PlantType.POTATO, Constants.potato, 1),
        setOf(PlantType.POTATO, PlantType.PUMPKIN)
    )
    private val tile3 = Field(
        3,
        Coordinate(0, 6),
        null,
        1,
        TileType.FIELD,
        3,
        Plant(PlantType.POTATO, Constants.potato, 1),
        emptySet()
    )

    private val tile4 = Field(
        4,
        Coordinate(2, 0),
        null,
        1,
        TileType.FIELD,
        30000,
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
        30000,
        Plant(PlantType.POTATO, Constants.potato, 1),
        setOf(PlantType.PUMPKIN)
    )
    private val tile9 = Tile(
        9,
        Coordinate(1, 3),
        null,
        true,
        1,
        TileType.FARMSTEAD
    )
    private val plantData = mapOf(
        Pair(PlantType.POTATO, Constants.potato),
        Pair(PlantType.PUMPKIN, Constants.pumpkin),
        Pair(PlantType.APPLE, Constants.apple)
    )
    private val machines =
        mapOf(
            Pair(1, Machine(1, listOf(Action.SOWING), listOf(PlantType.POTATO), duration = 13, tile9, 0)),
            Pair(
                2,
                Machine(
                    2,
                    listOf(Action.IRRIGATING),
                    listOf(PlantType.PUMPKIN, PlantType.APPLE, PlantType.POTATO),
                    duration = 1,
                    tile9,
                    0
                )
            )
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
        val board: BoardData = mock()
        whenever(pathfinder.reachable(tile9, tile3, 1, board))
            .thenReturn(true)
        whenever(pathfinder.canContinue(tile3, tile5, 1, board, false))
            .thenReturn(true)
        whenever(pathfinder.canContinue(tile5, tile6, 1, board, false))
            .thenReturn(true)
        whenever(pathfinder.canContinue(tile6, tile8, 1, board, false))
            .thenReturn(false)
        whenever(pathfinder.canContinue(tile6, tile1, 1, board, false))
            .thenReturn(true)
        whenever(pathfinder.canContinue(tile6, tile7, 1, board, false))
            .thenReturn(true)
        whenever(pathfinder.canContinue(tile1, tile2, 1, board, false))
            .thenReturn(true)
    }

    @Test
    fun irrigate() {
        val pathFinder = PathFinder()
        val farmHandler = FarmHandler(mapOf(Pair(1, farm)), plantData, machines, pathFinder)
        val machine = machines[2] ?: error("Detekt sucks")
        val remainingMachines = mutableListOf(
            machines[1] ?: error("FUCK DETEKT"),
            machines[2] ?: error("FUCK DETEKT")
        )
        val fieldMap = mutableMapOf<Action, MutableSet<Fertile>>(
            Pair(Action.IRRIGATING, mutableSetOf(tile3, tile5, tile6, tile7, tile8)),
            Pair(Action.WEEDING, mutableSetOf()),
            Pair(Action.MOWING, mutableSetOf())
        )
        val plantationMap = mutableMapOf<Action, MutableSet<Fertile>>(
            Pair(Action.IRRIGATING, mutableSetOf(tile1, tile2)),
            Pair(Action.WEEDING, mutableSetOf()),
            Pair(Action.MOWING, mutableSetOf())
        )
        val boardData =
            BoardData(
                mapOf(
                    Pair(
                        1,
                        tile1
                    ),
                    Pair(
                        2,
                        tile2
                    ),
                    Pair(
                        3,
                        tile3
                    ),
                    Pair(4, tile4), Pair(5, tile5), Pair(6, tile6), Pair(7, tile7), Pair(8, tile8), Pair(9, tile9)
                )
            )
        val finishedFields = mutableMapOf<Int, Fertile>(Pair(7, tile7))
        farmHandler.performNonPrioritizedAction(
            machine,
            remainingMachines,
            fieldMap,
            plantationMap,
            finishedFields,
            boardData,
            farm,
            9
        )
        assertTrue {
            finishedFields.contains(1) && finishedFields.contains(2) && finishedFields.contains(3) &&
                finishedFields.contains(5) && finishedFields.contains(6) && finishedFields.contains(7) &&
                !finishedFields.contains(4)
        }
    }
}
