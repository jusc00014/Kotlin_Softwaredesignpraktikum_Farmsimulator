package general

import de.unisaarland.cs.se.selab.Constants
import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.farms.Action
import de.unisaarland.cs.se.selab.farms.Farm
import de.unisaarland.cs.se.selab.farms.Machine
import de.unisaarland.cs.se.selab.farms.SowingPlan
import de.unisaarland.cs.se.selab.parser.FarmParser
import de.unisaarland.cs.se.selab.parser.MapParser
import de.unisaarland.cs.se.selab.plants.PlantType
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock

class FarmParserTest {
    val mapJson = "src/systemtest/resources/extendedexample/map.json"
    val farmJson = "src/systemtest/resources/extendedexample/farms.json"
    lateinit var expectedBoardData: BoardData

    @BeforeEach
    fun setup() {
        val mapParser = MapParser(mutableMapOf())
        expectedBoardData = mapParser.parse(mapJson, Constants.OCT_1).first
    }

    @Test
    fun parse() {
        val farmParser = FarmParser()
        val (listFarms, idToMachine) = farmParser.parse(farmJson, expectedBoardData, 1000)
        val farmstead = expectedBoardData.getTileById(0) ?: mock<Tile>()
        val machineTractor = Machine(
            0,
            listOf(Action.SOWING, Action.IRRIGATING),
            listOf(PlantType.PUMPKIN, PlantType.WHEAT, PlantType.POTATO),
            4,
            farmstead
        )
        val machineBTractor = Machine(
            1,
            listOf(Action.SOWING, Action.IRRIGATING),
            listOf(PlantType.OAT),
            14,
            farmstead
        )
        val sowingPlan = SowingPlan(0, 0, PlantType.POTATO, listOf(5))
        val farm = Farm(
            0,
            listOf(0),
            listOf(5),
            listOf(4),
            listOf(0, 1),
            mutableListOf(sowingPlan)
        )
        assertTrue(listFarms[0] == farm)
        assertTrue(idToMachine[0] == machineTractor)
        assertTrue(idToMachine[1] == machineBTractor)
    }
}
