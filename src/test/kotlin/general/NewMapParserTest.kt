package general

import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.Coordinate
import de.unisaarland.cs.se.selab.board.Direction
import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.board.TileType
import de.unisaarland.cs.se.selab.parser.MapParser
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class NewMapParserTest {
    var mapJson = "src/systemtest/resources/bigCloudTest/bigCloudTestMap.json"
    lateinit var board: BoardData
    val tile1 = Tile(1, Coordinate(0, 0), Direction.EAST, false, null, TileType.VILLAGE)
    val tile2 = Tile(2, Coordinate(2, 0), Direction.SOUTHEAST, false, null, TileType.VILLAGE)
    val tile3 = Tile(3, Coordinate(1, 1), Direction.NORTHWEST, false, 1, TileType.PLANTATION)
    val tile4 = Tile(4, Coordinate(0, 2), Direction.SOUTH, false, 1, TileType.PLANTATION)
    val tile5 = Tile(5, Coordinate(2, 2), Direction.NORTH, false, 1, TileType.PLANTATION)
    val tile6 = Tile(6, Coordinate(1, 3), Direction.NORTHEAST, false, null, TileType.MEADOW)
    val tile7 = Tile(7, Coordinate(0, 4), Direction.EAST, false, null, TileType.FOREST)
    val tile8 = Tile(8, Coordinate(2, 4), Direction.SOUTH, false, null, TileType.FOREST)
    val tile9 = Tile(9, Coordinate(1, 5), Direction.NORTHWEST, true, 1, TileType.FARMSTEAD)
    val tile10 = Tile(10, Coordinate(0, 6), Direction.NORTH, false, null, TileType.FOREST)
    val tile11 = Tile(11, Coordinate(2, 6), Direction.WEST, false, 1, TileType.PLANTATION)
    val tile12 = Tile(12, Coordinate(3, 3), Direction.NORTHEAST, false, null, TileType.ROAD)
    val tile13 = Tile(13, Coordinate(4, 2), Direction.WEST, false, 1, TileType.FIELD)

    @BeforeEach
    fun setUp() {
        board = BoardData(
            mapOf(
                1 to tile1, 2 to tile2, 3 to tile3,
                4 to tile4, 5 to tile5, 6 to tile6,
                7 to tile7, 8 to tile8, 9 to tile9,
                10 to tile10, 11 to tile11, 12 to tile12,
                13 to tile13
            )
        )
    }

    @Test
    fun cloudMapTest() {
        val mapParser = MapParser(mutableMapOf())
        val (data, plantMap) = mapParser.parse(mapJson, 1)
    }
}
