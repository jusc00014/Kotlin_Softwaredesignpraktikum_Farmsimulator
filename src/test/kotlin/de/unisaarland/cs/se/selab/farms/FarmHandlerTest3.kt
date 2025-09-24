package de.unisaarland.cs.se.selab.farms

import de.unisaarland.cs.se.selab.board.BoardHandler
import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.parser.FarmParser
import de.unisaarland.cs.se.selab.parser.MapParser
import de.unisaarland.cs.se.selab.parser.ScenarioParser
import kotlin.test.Test

class FarmHandlerTest3 {
    var mapJson: String = "src/systemtest/resources/onefieldtest/map.json"
    var farmJson: String = "src/systemtest/resources/onefieldtest/farms.json"
    var scenarioJson: String = "src/systemtest/resources/onefieldtest/scenario.json"

    @Test
    fun farmAction() {
        val mapParser = MapParser(mutableMapOf<Int, Tile>())
        val (boardData, plantMap) = mapParser.parse(mapJson, 9)
        val farmParser = FarmParser()
        val (farms, machines) = farmParser.parse(farmJson, boardData, 2)
        val scenarioParser = ScenarioParser()
        val (incidents, clouds) = scenarioParser.parse(scenarioJson, boardData, 2, machines, farms, 9)
        val farmHandler = FarmHandler(mapOf(Pair(1, farms[0])), plantMap, machines, PathFinder())
        val boardHandler = BoardHandler()
        boardHandler.reduceSoil(9, boardData)
        farmHandler.farmAction(9, boardData)
    }
}
