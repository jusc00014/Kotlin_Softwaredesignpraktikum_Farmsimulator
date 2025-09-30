package de.unisaarland.cs.se.selab.farms

import de.unisaarland.cs.se.selab.Simulator
import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.BoardHandler
import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.clouds.CloudData
import de.unisaarland.cs.se.selab.clouds.CloudHandler
import de.unisaarland.cs.se.selab.incidents.Incident
import de.unisaarland.cs.se.selab.incidents.IncidentHandler
import de.unisaarland.cs.se.selab.parser.FarmParser
import de.unisaarland.cs.se.selab.parser.MapParser
import de.unisaarland.cs.se.selab.parser.ScenarioParser
import de.unisaarland.cs.se.selab.plants.PlantData
import de.unisaarland.cs.se.selab.plants.PlantType
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FarmHandlerTestHarvest {
    val mapJson = "src/systemtest/resources/machineTravelTest/map.json"
    val farmJson = "src/systemtest/resources/machineTravelTest/farms.json"
    val scenarioJson = "src/systemtest/resources/machineTravelTest/scenario.json"
    lateinit var boardData: BoardData
    lateinit var plantMap: Map<PlantType, PlantData>
    lateinit var farmList: List<Farm>
    lateinit var idToMachine: Map<Int, Machine>
    lateinit var incidentList: List<Incident>
    lateinit var cloudData: CloudData

    @BeforeEach
    fun setUp() {
        val a = MapParser(mutableMapOf<Int, Tile>()).parse(mapJson, 21)
        boardData = a.first
        plantMap = a.second
        val x = FarmParser().parse(farmJson, boardData)
        farmList = x.first
        idToMachine = x.second
        val y = ScenarioParser().parse(scenarioJson, boardData, 24, idToMachine, farmList, 21)
        incidentList = y.first
        cloudData = y.second
    }

    @Test
    fun farmActionTest() {
        val sim = Simulator(
            BoardHandler(),
            FarmHandler(
                mapOf(0 to farmList[0], 1 to farmList[1]),
                plantMap,
                idToMachine,
                PathFinder()
            ),
            CloudHandler(cloudData, boardData),
            IncidentHandler(mutableMapOf()),
            boardData,
            21,
            24
        )
        sim.start()
        assertTrue(true)
    }
}
