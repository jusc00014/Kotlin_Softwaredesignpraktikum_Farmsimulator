package de.unisaarland.cs.se.selab.farms

import de.unisaarland.cs.se.selab.Constants
import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.Coordinate
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
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PathFinderTest {
    private lateinit var pathFinder: PathFinder
    private lateinit var board: BoardData
    private val tile1 = Field(
        1,
        Coordinate(2, 0),
        null,
        1,
        TileType.FIELD,
        3000,
        Plant(PlantType.POTATO, Constants.potato, 1),
        emptySet()
    )
    private val tile4 = Field(
        4,
        Coordinate(6, 0),
        null,
        1,
        TileType.FIELD,
        2500,
        Plant(PlantType.WHEAT, Constants.wheat, 1),
        emptySet()
    )
    private val tile11 = Plantation(
        11,
        Coordinate(8, 2),
        null,
        1,
        TileType.PLANTATION,
        1000,
        Plant(PlantType.APPLE, Constants.apple, 1)
    )
    private val tile5 = Tile(
        5,
        Coordinate(4, 0),
        null,
        false,
        null,
        TileType.VILLAGE
    )
    private val tile7 = Tile(
        7,
        Coordinate(1, 1),
        null,
        true,
        1,
        TileType.FARMSTEAD
    )
    private val tile10 = Tile(
        10,
        Coordinate(7, 1),
        null,
        true,
        2,
        TileType.FARMSTEAD
    )

    @BeforeEach
    fun setUp() {
        pathFinder = PathFinder()

        board = mock()

        whenever(board.neighbors(1, tile1))
            .thenReturn(listOf(tile5, tile7))

        whenever(board.neighbors(1, tile5))
            .thenReturn(listOf(tile1, tile4))

        whenever(board.neighbors(1, tile7))
            .thenReturn(listOf(tile1))

        whenever(board.neighbors(1, tile4))
            .thenReturn(listOf(tile5, tile10))

        whenever(board.neighbors(1, tile10))
            .thenReturn(listOf(tile4, tile11))

        whenever(board.neighbors(1, tile11))
            .thenReturn(listOf(tile10))
    }

    @Test
    fun reachable() {
        assertTrue(pathFinder.reachable(tile7, tile4, 1, board))
        assertFalse(pathFinder.reachable(tile7, tile11, 1, board))
    }

    @Test
    fun reachableWithHarvest() {
        assertFalse(pathFinder.reachableWithHarvest(tile7, tile4, 1, board))
    }

    @Test
    fun canContinue() {
        assertTrue(pathFinder.canContinue(tile1, tile4, 1, board, false))
        assertFalse(pathFinder.canContinue(tile1, tile4, 1, board, true))
        assertTrue(pathFinder.canContinue(tile7, tile4, 1, board, false))
        assertFalse(pathFinder.canContinue(tile7, tile10, 1, board, false))
        assertFalse(pathFinder.canContinue(tile5, tile11, 1, board, false))
    }

    @Test
    fun findNearestShed() {
        assertTrue(true)
    }
}
