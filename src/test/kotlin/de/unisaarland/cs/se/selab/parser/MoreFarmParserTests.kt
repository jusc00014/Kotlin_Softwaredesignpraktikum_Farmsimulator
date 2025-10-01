package de.unisaarland.cs.se.selab.parser

import org.junit.jupiter.api.Test
import kotlin.test.assertFails

class MoreFarmParserTests {

    @Test
    fun missingfarmstead() {
        val mapJson = "src/systemtest/resources/farmActionTests/MachineTravel/map.json"
        val farmJson = "src/systemtest/resources/farmParser/missingfarmsteadfarm.json"
        val mapParser = MapParser(mutableMapOf())
        val (boardData, plantMap) = mapParser.parse(mapJson, 9)
        val farmParser = FarmParser()
        assertFails { farmParser.parse(farmJson, boardData) }
    }

    @Test
    fun missingplantation() {
        val mapJson = "src/systemtest/resources/farmActionTests/MachineTravel/map.json"
        val farmJson = "src/systemtest/resources/farmParser/missingplantationfarm.json"
        val mapParser = MapParser(mutableMapOf())
        val (boardData, plantMap) = mapParser.parse(mapJson, 9)
        val farmParser = FarmParser()
        assertFails { farmParser.parse(farmJson, boardData) }
    }

    @Test
    fun MachineCanTReach() {
        val mapJson = "src/systemtest/resources/alternativeParserTest/map1.json"
        val farmJson = "src/systemtest/resources/alternativeParserTest/farm1.json"
        val mapParser = MapParser(mutableMapOf())
        val (boardData, plantMap) = mapParser.parse(mapJson, 9)
        val farmParser = FarmParser()
        farmParser.parse(farmJson, boardData)
    }
}
