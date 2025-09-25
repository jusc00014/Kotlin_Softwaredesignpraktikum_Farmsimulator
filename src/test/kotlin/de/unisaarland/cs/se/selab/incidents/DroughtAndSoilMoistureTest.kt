package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.BoardHandler
import de.unisaarland.cs.se.selab.board.Fertile
import de.unisaarland.cs.se.selab.farms.Action
import de.unisaarland.cs.se.selab.farms.FarmHandler
import de.unisaarland.cs.se.selab.farms.PathFinder
import de.unisaarland.cs.se.selab.parser.FarmParser
import de.unisaarland.cs.se.selab.parser.MapParser
import de.unisaarland.cs.se.selab.parser.ScenarioParser
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class DroughtAndSoilMoistureTest {
    var mapJson: String = "src/systemtest/resources/onefieldtest/map.json"
    var farmJson: String = "src/systemtest/resources/onefieldtest/farms.json"
    var scenarioJson: String = "src/systemtest/resources/onefieldtest/scenario.json"

    @Test
    fun drought() {
        val mapParser = MapParser(mutableMapOf())
        val (boardData, plantMap) = mapParser.parse(mapJson, 19)
        val farmParser = FarmParser()
        val (farms, machines) = farmParser.parse(farmJson, boardData, 2)
        val farmHandler = FarmHandler(mapOf(Pair(1, farms[0])), plantMap, machines, PathFinder())
        val boardHandler = BoardHandler()
        val scenarioParser = ScenarioParser()
        val (incidentss, cloudData) = scenarioParser.parse(scenarioJson, boardData, 2, machines, farms, 19)
        boardHandler.reduceSoil(19, boardData)
        farmHandler.farmAction(19, boardData)
        val incidentMap = mutableMapOf<Int, MutableList<Incident>>()
        for (inc in incidentss) {
            if (incidentMap[inc.tick] == null) {
                incidentMap[inc.tick] = mutableListOf(inc)
            } else {
                incidentMap[inc.tick]?.add(inc)
            }
        }
        val incidents = incidentMap.mapValues { it.value.toList() }.toMutableMap()
        val incidentHandler = IncidentHandler(incidents)
        incidentHandler.executeIncidents(0)
        boardHandler.computeEstimate(19, boardData)
        boardHandler.reduceSoil(20, boardData)
        val fert = (boardData.getTileById(38) ?: error("null assertion message")) as Fertile
        assertTrue { fert.irrigatable(20) }
        farmAction(boardData, farmHandler)
    }

    fun farmAction(boardData: BoardData, farmHandler: FarmHandler) {
        val fertiles = boardData.getFertiles()
        for (farm in farmHandler.idToFarm.values) {
            val remainingMachines = farmHandler.assembleMachines(farm)
            val sowFields = farmHandler.assembleSowableFields(farm.fields, fertiles, 9)
            val finishedFields = mutableMapOf<Int, Fertile>()
            farmHandler.sow(
                sowFields,
                farm,
                remainingMachines,
                finishedFields = finishedFields,
                board = boardData,
                yearTick = 20,
                fertiles = fertiles
            )
            val fieldMap = farmHandler.createActionMap(farm.fields, fertiles, 20)
            assertTrue { fieldMap[Action.IRRIGATING]?.contains(fertiles[38]) ?: false }
            val plantationMap = farmHandler.createActionMap(farm.plantages, fertiles, 20)
            for ((action, fertileType) in listOf(
                Action.HARVESTING to plantationMap[Action.HARVESTING],
                Action.HARVESTING to fieldMap[Action.HARVESTING],
                Action.CUTTING to plantationMap[Action.CUTTING]
            )) {
                farmHandler.performPrioritizedAction(
                    action,
                    remainingMachines,
                    fertileType ?: error("Set not Initialized for $action"),
                    finishedFields,
                    boardData,
                    farm,
                    20
                )
            }
            for (machine in remainingMachines) {
                farmHandler.performNonPrioritizedAction(
                    machine,
                    remainingMachines,
                    fieldMap,
                    plantationMap,
                    finishedFields,
                    boardData,
                    farm,
                    20
                )
            }
            assertTrue { finishedFields.contains(38) }
            assertTrue { !remainingMachines.contains(farmHandler.machines[1]) }
        }
    }
}
