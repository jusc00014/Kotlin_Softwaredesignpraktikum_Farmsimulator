package de.unisaarland.cs.se.selab.board

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class BoardDataTest {
    private lateinit var board: BoardData

    //
    @BeforeEach
    fun setUp() {
        val idToTiles = mapOf(
            10 to Tile(10, Coordinate(0, 0), null, false, null, TileType.VILLAGE),
            11 to Tile(11, Coordinate(2, 0), null, false, null, TileType.VILLAGE),
            12 to Tile(12, Coordinate(0, -2), null, false, null, TileType.VILLAGE),
            13 to Tile(13, Coordinate(2, -2), null, false, null, TileType.VILLAGE),
            14 to Tile(14, Coordinate(1, 1), null, false, null, TileType.VILLAGE),
            15 to Tile(15, Coordinate(1, -1), null, false, null, TileType.VILLAGE),
            16 to Tile(16, Coordinate(3, -1), null, false, null, TileType.VILLAGE)
        )
        val board = BoardData(idToTiles)
        this.board = board
    }

    //
    @Test
    fun getTileByIdPositiveExists() {
        val actual = board.getTileById(10)
        assertEquals(10, actual?.id)
    }

    //
    @Test
    fun getTileByIdPositiveNotExists() {
        val actual = board.getTileById(19)
        assertNull(actual)
    }

    //
    @Test
    fun getTileByIdZero() {
        val actual = board.getTileById(0)
        assertNull(actual)
    }

    //
    @Test
    fun getTileByIdNeg() {
        val actual = board.getTileById(-5)
        assertNull(actual)
    }

    //
    @Test
    fun getTileByCoordExists() {
        val actual = board.getTileByCoord(Coordinate(0, 0))
        assertEquals(0, actual?.coord?.x)
        assertEquals(0, actual?.coord?.y)
    }

    //
    @Test
    fun getTileByCoordExists2() {
        val actual = board.getTileByCoord(Coordinate(3, -1))
        assertEquals(3, actual?.coord?.x)
        assertEquals(-1, actual?.coord?.y)
    }

    //
    @Test
    fun getTileByCoordNotExistsButPossible() {
        val actual = board.getTileByCoord(Coordinate(-1, 1))
        assertNull(actual)
    }

    //
    @Test
    fun getTileByCoordNotExistsAndNotPossible() {
        val actual = board.getTileByCoord(Coordinate(1, 2))
        assertNull(actual)
    }

    //
    @Test
    fun neighbors1() {
        val tile = board.getTileById(10)
        if (tile != null) {
            val expected = listOf(
                board.getTileById(11),
                board.getTileById(12),
                board.getTileById(14),
                board.getTileById(15)
            )
            val actual = board.neighbors(1, tile)
            assertEquals(expected, actual)
        }
        assertNotNull(tile)
    }

    //
    @Test
    fun neighbors2() {
        val tile = board.getTileById(15)
        if (tile != null) {
            val expected = listOf(
                board.getTileById(10),
                board.getTileById(11),
                board.getTileById(12),
                board.getTileById(13)
            )
            val actual = board.neighbors(1, tile)
            assertEquals(expected, actual)
        }
        assertNotNull(tile)
    }

    //
    @Test
    fun neighborsRadius2() {
        val tile = board.getTileById(11)
        if (tile != null) {
            val expected = listOf(
                board.getTileById(10),
                tile,
                board.getTileById(12),
                board.getTileById(13),
                board.getTileById(14),
                board.getTileById(15),
                board.getTileById(16)
            )
            val actual = board.neighbors(2, tile)
            assertEquals(expected, actual)
        }
        assertNotNull(tile)
    }

    /**
     *
     */
    @Test
    fun neighborsRadius5() {
        fun toRoad(id: Int, coord: Coordinate) = Tile(id, coord, null, type = TileType.ROAD)
        val expected = arrayOf(
            Coordinate(0, 0),
            Coordinate(1, -1),
            Coordinate(2, 0),
            Coordinate(1, 1),
            Coordinate(0, 2),
            Coordinate(-1, 1),
            Coordinate(-2, 0),
            Coordinate(-1, -1),
            Coordinate(0, -2),
            Coordinate(6, -4)
        ).mapIndexed { id, coord -> toRoad(id, coord) }
        val notExpected = arrayOf(
            Coordinate(10, 10),
            Coordinate(25, 11),
            Coordinate(12, 0)
        ).mapIndexed { id, coord -> toRoad(expected.size + id, coord) }
        val boardBig = BoardData((expected + notExpected).associateBy { it.id })
        val tile = boardBig.getTileById(0)
        if (tile != null) {
            val actual = boardBig.neighbors(5, tile)
            assertEquals(expected, actual)
        }
        assertNotNull(tile)
    }
}
