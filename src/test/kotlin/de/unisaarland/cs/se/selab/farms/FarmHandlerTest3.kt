package de.unisaarland.cs.se.selab.farms

import de.unisaarland.cs.se.selab.board.BoardHandler
import de.unisaarland.cs.se.selab.board.Fertile
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.parser.FarmParser
import de.unisaarland.cs.se.selab.parser.MapParser
import kotlin.test.Test
import kotlin.test.assertTrue

class FarmHandlerTest3 {
    var mapJson: String = "src/systemtest/resources/onefieldtest/map.json"
    var farmJson: String = "src/systemtest/resources/onefieldtest/farms.json"

    @Test
    fun farmActionNothingToDo() {
        val mapParser = MapParser(mutableMapOf())
        val (boardData, plantMap) = mapParser.parse(mapJson, 9)
        val farmParser = FarmParser()
        val (farms, machines) = farmParser.parse(farmJson, boardData, 2)
        val farmHandler = FarmHandler(mapOf(Pair(1, farms[0])), plantMap, machines, PathFinder())
        val boardHandler = BoardHandler()
        boardHandler.reduceSoil(9, boardData)
        /*
        farmHandler.farmAction(9, boardData)
        val finishedFields = mutableMapOf<Int, Fertile>()
        val remainingMachines: MutableList<Machine> = mutableListOf<Machine>()
        remainingMachines.addAll(machines.values)
        farmHandler.sow(
            farmHandler.assembleSowableFields(listOf(38), boardData.getFertiles(), 9),
            farms.get(0),
            remainingMachines,
            finishedFields,
            boardData,
            9,
            boardData.getFertiles()
        )*/

        val fertiles = boardData.getFertiles()
        for (farm in farmHandler.idToFarm.values) {
            Logger.farmStartAction(farm.id)
            val remainingMachines = farmHandler.assembleMachines(farm)
            val sowFields = farmHandler.assembleSowableFields(farm.fields, fertiles, 9)
            val finishedFields = mutableMapOf<Int, Fertile>()
            farmHandler.sow(
                sowFields,
                farm,
                remainingMachines,
                finishedFields = finishedFields,
                board = boardData,
                yearTick = 9,
                fertiles = fertiles
            )
            val fieldMap = farmHandler.createActionMap(farm.fields, fertiles, 9)
            val plantationMap = farmHandler.createActionMap(farm.plantages, fertiles, 9)
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
                    9
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
                    9
                )
            }
            assertTrue { finishedFields.isEmpty() }
            assertTrue { farm.plans.isNotEmpty() }
            assertTrue { farm.plans[0].isActive(1) }
        }
    }

    @Test
    fun farmActionSowingToDo() {
        val mapParser = MapParser(mutableMapOf())
        val (boardData, plantMap) = mapParser.parse(mapJson, 19)
        val farmParser = FarmParser()
        val (farms, machines) = farmParser.parse(farmJson, boardData, 2)
        val farmHandler = FarmHandler(mapOf(Pair(1, farms[0])), plantMap, machines, PathFinder())
        val boardHandler = BoardHandler()
        boardHandler.reduceSoil(19, boardData)
        val fertiles = boardData.getFertiles()
        for (farm in farmHandler.idToFarm.values) {
            Logger.farmStartAction(farm.id)
            val remainingMachines = farmHandler.assembleMachines(farm)
            val sowFields = farmHandler.assembleSowableFields(farm.fields, fertiles, 19)
            val finishedFields = mutableMapOf<Int, Fertile>()
            farmHandler.sow(
                sowFields,
                farm,
                remainingMachines,
                finishedFields = finishedFields,
                board = boardData,
                yearTick = 19,
                fertiles = fertiles
            )
            val fieldMap = farmHandler.createActionMap(farm.fields, fertiles, 19)
            val plantationMap = farmHandler.createActionMap(farm.plantages, fertiles, 19)
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
                    19
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
                    19
                )
            }
            assertTrue { finishedFields.isNotEmpty() }
            assertTrue { farm.plans.isEmpty() }
        }
    }
}
