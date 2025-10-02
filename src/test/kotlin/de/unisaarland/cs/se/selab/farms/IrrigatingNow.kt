package de.unisaarland.cs.se.selab.farms

import de.unisaarland.cs.se.selab.board.BoardHandler
import de.unisaarland.cs.se.selab.parser.FarmParser
import de.unisaarland.cs.se.selab.parser.MapParser
import org.junit.jupiter.api.Test

class IrrigatingNow {
    val mapJson = "src/systemtest/resources/farmActionTests/AnotherIrrigation/map.json"
    val farmJson = "src/systemtest/resources/farmActionTests/AnotherIrrigation/farm.json"
    val scenarioJson = "src/systemtest/resources/onefieldtest/noscenario.json"

    @Test
    fun test() {
        val mapParser = MapParser(mutableMapOf())
        val (boardData, plantMap) = mapParser.parse(mapJson, 9)
        val farmParser = FarmParser()
        val (farms, machines) = farmParser.parse(farmJson, boardData)
        val farmHandler = FarmHandler(mapOf(Pair(1, farms[0])), plantMap, machines, PathFinder())
        val boardHandler = BoardHandler()
        boardHandler.reduceSoil(6, boardData)
        farmHandler.farmAction(0, 6, boardData)
        boardHandler.reduceSoil(7, boardData)
        farmHandler.farmAction(1, 7, boardData)
    }
}
