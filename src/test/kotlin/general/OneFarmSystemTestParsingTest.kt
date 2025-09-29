package general

import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.parser.FarmParser
import de.unisaarland.cs.se.selab.parser.MapParser
import de.unisaarland.cs.se.selab.parser.ScenarioParser
import kotlin.test.Test
import kotlin.test.assertTrue

class OneFarmSystemTestParsingTest {
    var mapJson: String = "src/systemtest/resources/onefieldtest/map.json"
    var farmJson: String = "src/systemtest/resources/onefieldtest/farms.json"
    var scenarioJson: String = "src/systemtest/resources/onefieldtest/scenario.json"

    @Test
    fun parseOneFarmSystemTest() {
        val mapParser = MapParser(mutableMapOf<Int, Tile>())
        val (boardData, plantMap) = mapParser.parse(mapJson, 9)
        val farmParser = FarmParser()
        val (farms, machines) = farmParser.parse(farmJson, boardData)
        val scenarioParser = ScenarioParser()
        val (incidents, clouds) = scenarioParser.parse(scenarioJson, boardData, 2, machines, farms, 9)
        assertTrue { true }
    }
}
