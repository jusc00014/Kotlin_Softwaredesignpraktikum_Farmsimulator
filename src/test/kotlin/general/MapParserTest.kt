package general

import de.unisaarland.cs.se.selab.Constants
import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.Coordinate
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
    var mapJson: String = "src/systemtest/resources/example/map.json"
    lateinit var expectedBoardData: BoardData

    @BeforeEach
    fun setup() {
        val tile1 =
            Tile(id = 0, coord = Coordinate(1, 1), airflow = null, shed = true, farmID = 0, type = TileType.FARMSTEAD)
        val plantation1 =
            Plantation(
                id = 1,
                coord = Coordinate(0, 2),
                airflow = null,
                farmID = 0,
                type = TileType.PLANTATION,
                moistureCapacity = 8000,
                plant = Plant(PlantType.APPLE, Constants.apple, Constants.OCT_1)
            )
        val field1 =
            Field(
                id = 2,
                coord = Coordinate(2, 2),
                null,
                farmID = 0,
                type = TileType.FIELD,
                moistureCapacity = 10000,
                plant = Plant(PlantType.POTATO, Constants.potato, Constants.OCT_1),
                possiblePlants = mutableSetOf(PlantType.PUMPKIN, PlantType.WHEAT)
            )
        expectedBoardData = BoardData(mutableMapOf(0 to tile1, 1 to plantation1, 2 to field1))
    }

    @Test
    fun parseTest() {
        val mapParser = MapParser(mutableMapOf<Int, Tile>())
        val (boardData, plantMap) = mapParser.parse(mapJson, Constants.OCT_1)
        assertTrue(expectedBoardData.getTileById(0) == boardData.getTileById(0), "first")
        assertTrue(expectedBoardData.getTileById(1) == boardData.getTileById(1), "second")
        assertTrue(expectedBoardData.getTileById(2) == boardData.getTileById(2), "third")
    }
}
