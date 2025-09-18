package de.unisaarland.cs.se.selab.farms
import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.Fertile
import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.plants.PlantData
import de.unisaarland.cs.se.selab.plants.PlantType

class FarmHandler(
    val idToFarm: Map<Int, Farm>,
    val plantData: Map<PlantType, PlantData>,
    val machines: Map<Int, Machine>,
    val pathFinder: PathFinder
) {
    fun farmAction(yearTick: Int, board: BoardData) {
        val fertiles = board.getFertiles()
        for (farm in idToFarm.values) {
            val remainingMachines = assembleMachines(farm)
            var sowFields = assembleSowableFields(farm.fields, fertiles, yearTick)
        }
    }

    private fun assembleMachines(farm: Farm): MutableList<Machine> {
        var remainingMachines = mutableListOf<Machine>()
        for (machId in farm.machines) {
            var machine = machines[machId]
            if (machine != null && machine.brokenFor == 0 && machine.stuck == false) {
                remainingMachines.add(machine)
            }
        }
        return remainingMachines
    }

    private fun assembleSowableFields(
        fields: List<Int>,
        fertiles: Map<Int, Fertile>,
        yearTick: Int
    ): Map<PlantType, MutableList<Fertile>> {
        val sowFields = mutableMapOf<PlantType, MutableList<Fertile>>()
        for (plantType in listOf(PlantType.POTATO, PlantType.WHEAT, PlantType.OAT, PlantType.PUMPKIN)) {
            sowFields.put(plantType, mutableListOf<Fertile>())
        }
        for (fieldId in fields) {
            var fieldFound = fertiles[fieldId]
            if (fieldFound == null) {
                continue
            }
            var sowablePlants = fieldFound.sowablePlants(yearTick, plantData)
            for (plantType in sowablePlants) {
                var swo = sowFields[plantType]
                if (swo == null) {
                    continue
                }
                var fieldsThatCanSow = swo.add(fieldFound)
            }
        }
        return sowFields
    }

    private fun sow(
        sowableFields: Map<PlantType, MutableList<Fertile>>,
        farm: Farm,
        remainingMachines: MutableList<Machine>,
        finishedFields: Map<Int, Fertile>,
        fertiles: Map<Int, Fertile>,
        board: BoardData
    ) {
        TODO()
    }

    private fun executeSowingPlan(
        farm: Farm,
        plan: SowingPlan,
        sowableFields: Map<PlantType, List<Fertile>>,
        remainingMachines: List<Machine>,
        finishedFields: Map<Int, Fertile>,
        fertiles: Map<Int, Fertile>,
        board: BoardData
    ) {
        TODO()
    }

    private fun createActionMap(
        tiles: List<Int>,
        fertiles: Map<Int, Fertile>
    ): Map<Action, Fertile> {
        TODO()
    }

    private fun performPrioritizedAction(
        action: Action,
        remainingMachines: List<Machine>,
        actionMap: Map<Action, List<Fertile>>,
        finishedFields: Map<Int, Fertile>,
        board: BoardData,
        farm: Farm
    ) {
        TODO()
    }

    private fun findBestMachine(
        fertile: Fertile,
        remainingMachines: List<Machine>,
        action: Action,
        plantType: PlantType?,
        board: BoardData,
        farm: Farm
    ): Machine? {
        TODO()
    }

    private fun nextField(
        action: Action,
        currentPlantType: PlantType,
        plantsToActOn: List<Fertile>,
        finishedFertiles: Map<Int, Fertile>,
        bestMachine: Machine,
        currentLocation: Tile,
        farm: Farm,
        board: BoardData
    ) {
        TODO()
    }

    private fun performNonPrioritizedAction(
        action: Action,
        fertile: Fertile,
        remainingMachines: List<Machine>,
        actionMap: Map<Action, List<Fertile>>,
        finishedFields: Map<Int, Fertile>,
        board: BoardData,
        farm: Farm
    ) {
        TODO()
    }
}
