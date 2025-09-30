package de.unisaarland.cs.se.selab.parser

import de.unisaarland.cs.se.selab.Constants
import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.Coordinate
import de.unisaarland.cs.se.selab.board.Field
import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.board.TileType
import de.unisaarland.cs.se.selab.farms.Action
import de.unisaarland.cs.se.selab.farms.Farm
import de.unisaarland.cs.se.selab.farms.Machine
import de.unisaarland.cs.se.selab.incidents.CloudCreation
import de.unisaarland.cs.se.selab.incidents.Incident
import de.unisaarland.cs.se.selab.plants.Plant
import de.unisaarland.cs.se.selab.plants.PlantType
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ScenarioParserIncidentsTest {
    lateinit var boardData: BoardData
    lateinit var boardData2: BoardData
    lateinit var boardDataGap: BoardData
    lateinit var farmList: List<Farm>
    lateinit var idToMachines: Map<Int, Machine>
    val scenarioParser = ScenarioParser()
    val tile0 = Tile(
        0,
        Coordinate(-2, 0),
        null,
        false,
        null,
        TileType.FOREST
    )
    val tile1 = Field(
        1,
        Coordinate(0, 0),
        null,
        0,
        TileType.FIELD,
        123456,
        Plant(PlantType.POTATO, Constants.potato, 1),
        setOf(PlantType.POTATO)
    )
    val tile2 = Tile(
        2,
        Coordinate(2, 0),
        null,
        false,
        null,
        TileType.VILLAGE
    )
    val tile3 = Tile(
        3,
        Coordinate(-1, 1),
        null,
        false,
        null,
        TileType.MEADOW
    )
    val tile4 = Tile(
        4,
        Coordinate(1, 1),
        null,
        true,
        0,
        TileType.FARMSTEAD
    )
    val tile5 = Tile(
        5,
        Coordinate(2, 2),
        null,
        false,
        null,
        TileType.ROAD
    )
    val tile6 = Tile(
        6,
        Coordinate(3, 1),
        null,
        false,
        null,
        TileType.ROAD
    )
    val tile7 = Tile(
        7,
        Coordinate(2, 10),
        null,
        false,
        null,
        TileType.ROAD
    )
    val farm = Farm(
        0,
        listOf(0),
        listOf(1),
        emptyList(),
        listOf(333),
        mutableListOf()
    )
    val machine = Machine(
        333,
        listOf(Action.IRRIGATING),
        listOf(PlantType.POTATO),
        8,
        tile4,
    )

    @BeforeEach
    fun setup() {
        boardData = BoardData(
            mapOf(
                0 to tile0,
                1 to tile1,
                2 to tile2,
                3 to tile3,
                4 to tile4,
                5 to tile5,
                6 to tile6
            )
        )
        boardData2 = BoardData(
            mapOf(
                1 to tile1,
                2 to tile2,
                3 to tile3,
                4 to tile4,
                5 to tile5
            )
        )
        boardDataGap = BoardData(
            mapOf(
                1 to tile1,
                2 to tile2,
                4 to tile4,
                7 to tile7
            )
        )
        farmList = listOf(farm)
        idToMachines = mapOf(333 to machine)
    }

    @Test
    fun parseInvalidCloudCreation() {
        val scenarioJson = "src/systemtest/resources/scenarioParser/invalidCloudScenario.json"
        var failedParse = false
        try {
            scenarioParser.parse(scenarioJson, boardData, 17, idToMachines, farmList, 1)
        } catch (_: IllegalArgumentException) {
            failedParse = true
        }
        assertTrue(failedParse, "Parsing worked. Not good. There is no non Village tile in the affected tiles.")
    }

    @Test
    fun parseValidCloudCreation() {
        val scenarioJson = "src/systemtest/resources/scenarioParser/validCloudScenario.json"
        var parseWorked = true
        try {
            scenarioParser.parse(scenarioJson, boardData, 17, idToMachines, farmList, 1)
        } catch (_: IllegalArgumentException) {
            parseWorked = false
        }
        assertTrue(parseWorked, "Cloud Creation parsing didn't work. Not good.")
    }

    @Test
    fun parseValidCloudCreationOverlap() {
        val scenarioJson = "src/systemtest/resources/scenarioParser/validCloudOverlapScenario.json"
        var parseWorked = true
        try {
            scenarioParser.parse(scenarioJson, boardData, 17, idToMachines, farmList, 1)
        } catch (_: IllegalArgumentException) {
            parseWorked = false
        }
        assertTrue(parseWorked, "Cloud Creation parsing didn't work. Not good.")
    }

    @Test
    fun parseValidCloudCreationCorrectSet() {
        val scenarioJson = "src/systemtest/resources/scenarioParser/validCloudGapScenario.json"
        var parseWorked = true
        var incidentList = emptyList<Incident>()
        try {
            incidentList = scenarioParser.parse(
                scenarioJson,
                boardDataGap,
                17,
                idToMachines,
                farmList,
                1
            ).first
        } catch (_: IllegalArgumentException) {
            parseWorked = false
        }
        assertTrue(parseWorked, "Cloud Creation parsing didn't work. Not good.")
        val incident = incidentList.first()
        if (incident is CloudCreation) {
            assertTrue(incident.tiles == setOf(tile1, tile2, tile4, tile7))
        }
    }

    @Test
    fun parseInvalidAnimalAttack() {
        val scenarioJson = "src/systemtest/resources/scenarioParser/invalidAnimalScenario.json"
        var failedParse = false
        try {
            scenarioParser.parse(scenarioJson, boardData, 17, idToMachines, farmList, 1)
        } catch (_: IllegalArgumentException) {
            failedParse = true
        }
        assertTrue(failedParse, "Parsing worked. Not good. There is no forest in the affected tiles.")
    }

    @Test
    fun parseValidAnimalAttack() {
        val scenarioJson = "src/systemtest/resources/scenarioParser/validAnimalScenario.json"
        var parseWorked = true
        try {
            scenarioParser.parse(scenarioJson, boardData, 17, idToMachines, farmList, 1)
        } catch (_: IllegalArgumentException) {
            parseWorked = false
        }
        assertTrue(parseWorked, "Animal Attack parsing didn't work. Not good.")
    }

    @Test
    fun parseInvalidBeeHappy() {
        val scenarioJson = "src/systemtest/resources/scenarioParser/invalidBeeScenario.json"
        var failedParse = false
        try {
            scenarioParser.parse(scenarioJson, boardData, 17, idToMachines, farmList, 1)
        } catch (_: IllegalArgumentException) {
            failedParse = true
        }
        assertTrue(failedParse, "Parsing worked. Not good. There is no meadow tile in the affected tiles.")
    }

    @Test
    fun parseValidBeeHappy() {
        val scenarioJson = "src/systemtest/resources/scenarioParser/validBeeScenario.json"
        var parseWorked = true
        try {
            scenarioParser.parse(scenarioJson, boardData, 17, idToMachines, farmList, 1)
        } catch (_: IllegalArgumentException) {
            parseWorked = false
        }
        assertTrue(parseWorked, "Bee Happy parsing didn't work. Not good.")
    }

    @Test
    fun parseInvalidDrought() {
        val scenarioJson = "src/systemtest/resources/scenarioParser/invalidDroughtScenario.json"
        var failedParse = false
        try {
            scenarioParser.parse(scenarioJson, boardData, 17, idToMachines, farmList, 1)
        } catch (_: IllegalArgumentException) {
            failedParse = true
        }
        assertTrue(failedParse, "Parsing worked. Not good. There is no field/plantation tile in the affected tiles.")
    }

    @Test
    fun parseValidDrought() {
        val scenarioJson = "src/systemtest/resources/scenarioParser/validDroughtScenario.json"
        var parseWorked = true
        try {
            scenarioParser.parse(scenarioJson, boardData, 17, idToMachines, farmList, 1)
        } catch (_: IllegalArgumentException) {
            parseWorked = false
        }
        assertTrue(parseWorked, "Drought parsing didn't work. Not good.")
    }

    @Test
    fun parseInvalidBrokenMachine() {
        val scenarioJson = "src/systemtest/resources/scenarioParser/invalidBrokenScenario.json"
        var failedParse = false
        try {
            scenarioParser.parse(scenarioJson, boardData, 17, idToMachines, farmList, 1)
        } catch (_: IllegalArgumentException) {
            failedParse = true
        }
        assertTrue(failedParse, "Parsing worked. Not good. There is no machine with that id.")
    }

    @Test
    fun parseValidBrokenMachine() {
        val scenarioJson = "src/systemtest/resources/scenarioParser/validBrokenScenario.json"
        var parseWorked = true
        try {
            scenarioParser.parse(scenarioJson, boardData, 17, idToMachines, farmList, 1)
        } catch (_: IllegalArgumentException) {
            parseWorked = false
        }
        assertTrue(parseWorked, "Broken machine parsing didn't work. Not good.")
    }

    @Test
    fun parseInvalidCityExpansionForest() {
        val scenarioJson = "src/systemtest/resources/scenarioParser/invalidCityScenario.json"
        var failedParse = false
        try {
            scenarioParser.parse(scenarioJson, boardData, 17, idToMachines, farmList, 1)
        } catch (_: IllegalArgumentException) {
            failedParse = true
        }
        assertTrue(failedParse, "Parsing worked. Not good. There is an adjoining forest.")
    }

    @Test
    fun parseValidCityExpansion() {
        val scenarioJson = "src/systemtest/resources/scenarioParser/validCityScenario.json"
        var parseWorked = true
        try {
            scenarioParser.parse(scenarioJson, boardData, 17, idToMachines, farmList, 1)
        } catch (_: IllegalArgumentException) {
            parseWorked = false
        }
        assertTrue(parseWorked, "City Expansion parsing didn't work. Not good.")
    }
}
