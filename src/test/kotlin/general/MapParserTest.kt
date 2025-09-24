package general

import de.unisaarland.cs.se.selab.Constants
import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.Coordinate
import de.unisaarland.cs.se.selab.board.Direction
import de.unisaarland.cs.se.selab.board.Field
import de.unisaarland.cs.se.selab.board.Plantation
import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.board.TileType
import de.unisaarland.cs.se.selab.parser.MapParser
import de.unisaarland.cs.se.selab.plants.Plant
import de.unisaarland.cs.se.selab.plants.PlantType
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MapParserTest {
    var mapJson: String = "src/systemtest/resources/extendedexample/map.json"
    lateinit var expectedBoardData: BoardData
    val farmstead =
        Tile(id = 0, coord = Coordinate(1, 1), airflow = null, shed = true, farmID = 0, type = TileType.FARMSTEAD)
    val village =
        Tile(
            id = 1,
            coord = Coordinate(-1, 1),
            airflow = null,
            shed = false,
            farmID = null,
            type = TileType.VILLAGE
        )
    val meadow =
        Tile(
            id = 2,
            coord = Coordinate(3, 1),
            airflow = null,
            shed = false,
            farmID = null,
            type = TileType.MEADOW
        )
    val forest =
        Tile(
            id = 3,
            coord = Coordinate(2, 0),
            airflow = Direction.NORTH,
            shed = false,
            farmID = null,
            type = TileType.FOREST
        )
    val plantation =
        Plantation(
            id = 4,
            coord = Coordinate(0, 2),
            airflow = null,
            farmID = 0,
            type = TileType.PLANTATION,
            moistureCapacity = 8000,
            plant = Plant(PlantType.APPLE, Constants.apple, Constants.OCT_1)
        )
    val field =
        Field(
            id = 5,
            coord = Coordinate(2, 2),
            null,
            farmID = 0,
            type = TileType.FIELD,
            moistureCapacity = 10000,
            plant = Plant(PlantType.POTATO, Constants.potato, Constants.OCT_1),
            possiblePlants = mutableSetOf(PlantType.PUMPKIN, PlantType.WHEAT, PlantType.POTATO)
        )

    @BeforeEach
    fun setup() {
        expectedBoardData = BoardData(
            mutableMapOf(
                0 to farmstead,
                1 to village,
                2 to meadow,
                3 to forest,
                4 to plantation,
                5 to field
            )
        )
    }

    @Test
    fun parseTest() {
        val mapParser = MapParser(mutableMapOf<Int, Tile>())
        val (boardData, plantMap) = mapParser.parse(mapJson, Constants.OCT_1)
        assertTrue(expectedBoardData.getTileById(0) == boardData.getTileById(0))
        assertTrue(expectedBoardData.getTileById(1) == boardData.getTileById(1))
        assertTrue(expectedBoardData.getTileById(2) == boardData.getTileById(2))
        assertTrue(expectedBoardData.getTileById(3) == boardData.getTileById(3))
        assertTrue(expectedBoardData.getTileById(4) == boardData.getTileById(4))
        assertTrue(expectedBoardData.getTileById(5) == boardData.getTileById(5))
    }
}
