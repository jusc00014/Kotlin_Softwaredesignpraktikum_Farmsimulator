package general

import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.Coordinate
import de.unisaarland.cs.se.selab.board.Field
import de.unisaarland.cs.se.selab.board.Plantation
import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.board.TileType
import de.unisaarland.cs.se.selab.parser.MapParser
import de.unisaarland.cs.se.selab.plants.Plant
import de.unisaarland.cs.se.selab.plants.PlantData
import de.unisaarland.cs.se.selab.plants.PlantTile
import de.unisaarland.cs.se.selab.plants.PlantType
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MapParserTest {
    var mapJson: String = "src/systemtest/resources/example/map.json"
    companion object Constants {

        const val POTATO_MOISTURE = 500
        const val WHEAT_MOISTURE = 450
        const val OAT_MOISTURE = 300
        const val PUMPKIN_MOISTURE = 600
        const val APPLE_MOISTURE = 100
        const val ALMOND_MOISTURE = 400
        const val CHERRY_MOISTURE = 150
        const val GRAPE_MOISTURE = 250

        const val POTATO_SUNLIGHT = 130
        const val WHEAT_SUNLIGHT = 90
        const val OAT_SUNLIGHT = 90
        const val PUMPKIN_SUNLIGHT = 120
        const val APPLE_SUNLIGHT = 50
        const val ALMOND_SUNLIGHT = 130
        const val CHERRY_SUNLIGHT = 120
        const val GRAPE_SUNLIGHT = 150

        const val POTATO_HE = 1_000_000
        const val WHEAT_HE = 1_500_000
        const val OAT_HE = 1_200_000
        const val PUMPKIN_HE = 500_000
        const val APPLE_HE = 1_700_000
        const val ALMOND_HE = 800_000
        const val CHERRY_HE = 1_200_000
        const val GRAPE_HE = 1_200_000

        const val JAN_1 = 1
        const val JAN_2 = 2
        const val FEB_1 = 3
        const val FEB_2 = 4
        const val MAR_1 = 5
        const val MAR_2 = 6
        const val APR_1 = 7
        const val APR_2 = 8
        const val MAY_1 = 9
        const val MAY_2 = 10
        const val JUN_1 = 11
        const val JUN_2 = 12
        const val JUL_1 = 13
        const val JUL_2 = 14
        const val AUG_1 = 15
        const val AUG_2 = 16
        const val SEP_1 = 17
        const val SEP_2 = 18
        const val OCT_1 = 19
        const val OCT_2 = 20
        const val NOV_1 = 21
        const val NOV_2 = 22
        const val DEC_1 = 23
        const val DEC_2 = 24
    }
    val potato = PlantData(
        POTATO_MOISTURE,
        POTATO_SUNLIGHT,
        POTATO_HE,
        3..3,
        true,
        SEP_1..OCT_2,
        0,
        APR_1..MAY_2,
        (JAN_2..DEC_2 step 2).toList(),
        listOf(),
        listOf(),
        PlantTile.FIELD
    )
    val wheat = PlantData(
        WHEAT_MOISTURE,
        WHEAT_SUNLIGHT,
        WHEAT_HE,
        MAY_1..MAY_1,
        false,
        JUN_1..JUL_1,
        2,
        OCT_1..OCT_2,
        listOf(FEB_1, MAY_1),
        listOf(),
        listOf(),
        PlantTile.FIELD
    )
    val oat = PlantData(
        OAT_MOISTURE,
        OAT_SUNLIGHT,
        OAT_HE,
        0..0,
        false,
        JUL_1..AUG_2,
        2,
        MAR_2..MAR_2,
        listOf(1, 2, 3),
        listOf(),
        listOf(),
        PlantTile.FIELD
    )
    val pumpkin = PlantData(
        PUMPKIN_MOISTURE,
        PUMPKIN_SUNLIGHT,
        PUMPKIN_HE,
        2..3,
        true,
        SEP_1..OCT_2,
        0,
        MAY_1..JUN_2,
        (JAN_2..DEC_2 step 2).toList(),
        listOf(),
        listOf(),
        PlantTile.FIELD
    )
    val apple = PlantData(
        APPLE_MOISTURE,
        APPLE_SUNLIGHT,
        APPLE_HE,
        APR_2..MAY_1,
        true,
        SEP_1..OCT_2,
        1,
        0..0,
        listOf(),
        listOf(NOV_1, NOV_2, FEB_1, FEB_2),
        listOf(JUN_1, SEP_1),
        PlantTile.PLANTATION
    )
    val almond = PlantData(
        ALMOND_MOISTURE,
        ALMOND_SUNLIGHT,
        ALMOND_HE,
        FEB_2..MAR_1,
        true,
        AUG_2..OCT_1,
        1,
        0..0,
        listOf(),
        listOf(NOV_1, NOV_2, FEB_1, FEB_2),
        listOf(JUN_1, SEP_1),
        PlantTile.PLANTATION
    )
    val cherry = PlantData(
        CHERRY_MOISTURE,
        CHERRY_SUNLIGHT,
        CHERRY_HE,
        APR_2..MAY_1,
        true,
        JUL_1..JUL_2,
        1,
        0..0,
        listOf(),
        listOf(NOV_1, NOV_2, FEB_1, FEB_2),
        listOf(JUN_1),
        PlantTile.PLANTATION
    )
    val grape = PlantData(
        GRAPE_MOISTURE,
        GRAPE_SUNLIGHT,
        GRAPE_HE,
        JUN_2..AUG_2,
        false,
        SEP_2..SEP_2,
        3,
        0..0,
        listOf(),
        listOf(JUL_2, AUG_1, AUG_2),
        listOf(APR_1, JUL_1),
        PlantTile.PLANTATION
    )
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
                plant = Plant(PlantType.APPLE, apple)
            )
        val field1 =
            Field(
                id = 2,
                coord = Coordinate(2, 2),
                null,
                farmID = 0,
                type = TileType.FIELD,
                moistureCapacity = 10000,
                plant = Plant(PlantType.POTATO, potato),
                possiblePlants = mutableSetOf(PlantType.PUMPKIN, PlantType.WHEAT)
            )
        expectedBoardData = BoardData(mutableMapOf(0 to tile1, 1 to plantation1, 2 to field1))
    }

    @Test
    fun parse() {
        val mapParser = MapParser(mutableMapOf<Int, Tile>())
        val (boardData, plantMap) = mapParser.parse(mapJson)
        assertTrue(expectedBoardData.getTileById(0) == boardData.getTileById(0), "first")
        assertTrue(expectedBoardData.getTileById(1) == boardData.getTileById(1), "second")
        assertTrue(expectedBoardData.getTileById(2) == boardData.getTileById(2), "third")
    }
}
