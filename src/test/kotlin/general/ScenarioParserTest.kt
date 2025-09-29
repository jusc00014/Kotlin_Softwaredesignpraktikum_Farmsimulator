package general

import de.unisaarland.cs.se.selab.Constants
import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.clouds.Cloud
import de.unisaarland.cs.se.selab.farms.Farm
import de.unisaarland.cs.se.selab.farms.Machine
import de.unisaarland.cs.se.selab.incidents.BrokenMachine
import de.unisaarland.cs.se.selab.parser.FarmParser
import de.unisaarland.cs.se.selab.parser.MapParser
import de.unisaarland.cs.se.selab.parser.ScenarioParser
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock

class ScenarioParserTest {
    val mapJson = "src/systemtest/resources/extendedexample/map.json"
    val farmJson = "src/systemtest/resources/extendedexample/farms.json"
    val scenarioJson = "src/systemtest/resources/extendedexample/scenario.json"
    lateinit var expectedBoardData: BoardData
    lateinit var farmList: List<Farm>
    lateinit var idToMachine: Map<Int, Machine>

    @BeforeEach
    fun setup() {
        val mapParser = MapParser(mutableMapOf<Int, Tile>())
        val farmParser = FarmParser()
        expectedBoardData = mapParser.parse(mapJson, Constants.OCT_1).first
        val (farmsList, idToMachines) = farmParser.parse(farmJson, expectedBoardData)
        farmList = farmsList
        idToMachine = idToMachines
    }

    @Test
    fun parse() {
        val scenarioParser = ScenarioParser()
        val x = scenarioParser.parse(scenarioJson, expectedBoardData, 100, idToMachine, farmList, 1)
        val (incidentList, cloudData) = x
        val brokenMachine = BrokenMachine(0, 0, 15, idToMachine[1] ?: mock<Machine>())
        val cloud1 = Cloud(0, -1, 4, 600000)
        val cloud2 = Cloud(1, 1, 5, 1)
        val cloudBool = cloudData.getMaxId() == 1 && cloudData.clouds == listOf(cloud1, cloud2)
        val parsedIncident = incidentList[0]
        val incidentBool = parsedIncident.id == brokenMachine.id &&
            parsedIncident.tick == brokenMachine.id
        assertTrue(cloudBool == cloudBool)
        assertTrue(incidentBool == incidentBool)
    }
}
