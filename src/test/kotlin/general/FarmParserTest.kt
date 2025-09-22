package general

import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.Coordinate
import de.unisaarland.cs.se.selab.board.Field
import de.unisaarland.cs.se.selab.board.Plantation
import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.board.TileType
import de.unisaarland.cs.se.selab.farms.Action
import de.unisaarland.cs.se.selab.farms.Farm
import de.unisaarland.cs.se.selab.farms.Machine
import de.unisaarland.cs.se.selab.parser.FarmParser
import de.unisaarland.cs.se.selab.plants.Plant
import de.unisaarland.cs.se.selab.plants.PlantData
import de.unisaarland.cs.se.selab.plants.PlantTile
import de.unisaarland.cs.se.selab.plants.PlantType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FarmParserTest {
    val farmJson = "src/systemtest/resources/example/farms.json"
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
        val farmParser = FarmParser()
        val (farmList, idToMachine) = farmParser.parse(farmJson, expectedBoardData, 100)
        val expectedFarm = Farm(0, listOf(0), listOf(2), listOf(1), listOf(0), mutableListOf())
        assertTrue(farmList[0] == expectedFarm)
        val expectedAction = listOf(Action.SOWING, Action.IRRIGATING)
        val expectedPlants = listOf(PlantType.PUMPKIN, PlantType.WHEAT)
        val expectedMachine = Machine(0, expectedAction, expectedPlants, 4, expectedBoardData.getTileById(0))
        assertTrue(idToMachine[0] == expectedMachine)
    }

}