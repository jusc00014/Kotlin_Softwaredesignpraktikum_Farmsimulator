package de.unisaarland.cs.se.selab.farms
import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.Fertile
import de.unisaarland.cs.se.selab.board.Field
import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.farms.Machine
import de.unisaarland.cs.se.selab.plants.PlantData
import de.unisaarland.cs.se.selab.plants.PlantType
const val TICKTIME = 14

/**
 * Handles the farm action*/
class FarmHandler(
    val idToFarm: Map<Int, Farm>,
    val plantData: Map<PlantType, PlantData>,
    val machines: Map<Int, Machine>,
    val pathFinder: PathFinder
) {

    /**
     * Started by simulator*/
    fun farmAction(yearTick: Int, board: BoardData) {
        val fertiles = board.getFertiles()
        for (farm in idToFarm.values) {
            val remainingMachines = assembleMachines(farm)
            val sowFields = assembleSowableFields(farm.fields, fertiles, yearTick)
            sow(sowFields, farm, remainingMachines, mutableMapOf(), fertiles, board, yearTick)
        }
    }

    /**
     * Assemble usable machines*/
    private fun assembleMachines(farm: Farm): MutableList<Machine> {
        val remainingMachines = mutableListOf<Machine>()
        for (machId in farm.machines) {
            val machine = machines[machId] ?: continue
            if (machine.isUsable()) {
                remainingMachines.add(machine)
            } else {
                machine.decreaseBrokenFor()
            }
        }
        return remainingMachines
    }

    /**
     * For all field plants assemble the fields that can currently sow this plant*/
    private fun assembleSowableFields(
        fields: List<Int>,
        fertiles: Map<Int, Fertile>,
        yearTick: Int
    ): Map<PlantType, MutableList<Fertile>> {
        val sowFields = mutableMapOf<PlantType, MutableList<Fertile>>()
        for (plantType in PlantType.entries) {
            sowFields[plantType] = mutableListOf()
        }
        for (fieldId in fields) {
            val fieldFound = fertiles[fieldId]
            if (fieldFound == null || fieldFound !is Field) {
                continue
            }
            val sowablePlants = fieldFound.sowablePlants(yearTick, plantData)
            for (plantType in sowablePlants) {
                val swo = sowFields[plantType] ?: continue
                swo.add(fieldFound)
            }
        }
        return sowFields
    }

    /**
     * Start executing active sowing plans*/
    private fun sow(
        sowableFields: Map<PlantType, MutableList<Fertile>>,
        farm: Farm,
        remainingMachines: MutableList<Machine>,
        finishedFields: MutableMap<Int, Fertile>,
        fertiles: Map<Int, Fertile>,
        board: BoardData,
        yearTick: Int
    ) {
        val plans  = farm.plans
        //logger
        for (plan in plans) {
            executeSowingPlan(farm, plan, sowableFields, remainingMachines, finishedFields, fertiles, board, yearTick)
        }
    }

    /**
     * Try to execute one sowing plan*/
    private fun executeSowingPlan(
        farm: Farm,
        plan: SowingPlan,
        sowableFields: Map<PlantType, MutableList<Fertile>>,
        remainingMachines: MutableList<Machine>,
        finishedFields: MutableMap<Int, Fertile>,
        fertiles: Map<Int, Fertile>,
        board: BoardData,
        yearTick: Int
    ) {
        val toSow = plan.plant
        val sowFieldIds = plan.fields
        val sowFields = mutableListOf<Field>()
        for (sid in sowFieldIds) {
            val fert = finishedFields[sid]
            if (fert is Field) {
                sowFields.add(fert)
            }
        }
        val commonFields = sowFields.intersect(sowableFields[toSow].orEmpty()).toMutableSet()
        for (field in commonFields) {
            if (field.id in finishedFields) {
                continue
            }
            val machine = findBestMachine(
                field, remainingMachines, Action.SOWING, toSow, board, farm) ?: continue
            //logger
            val plant = field.plant
            plant.sow(toSow, plantData[toSow]!!, yearTick)
            farm.removeSowingPlan(plan)
            finishedFields[field.id] = field
            remainingMachines.remove(machine)
            var remainingTime = TICKTIME - machine.duration
            var currentField = field
            while (remainingTime > 0 && commonFields.isNotEmpty()) {
                commonFields.remove(currentField)
                currentField = nextField(
                    Action.SOWING,
                    toSow,
                    commonFields,
                    finishedFields,
                    machine,
                    currentField,
                    farm,
                    board,
                    yearTick)
                finishedFields[currentField.id] = currentField
                remainingTime -= machine.duration
            }
            // logger machineFinished
        }
    }

    /**
     * Assemble fields and the actions they can perform in this tick*/
    private fun createActionMap(
        fields: List<Int>,
        fertiles: Map<Int, Fertile>
    ): Map<Action, Fertile> {
        TODO()
    }

    /**
     * Perform actions  that are sorted by field id*/
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

    /**
     * Find machine with best duration for this field and action*/
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

    /**
     * Continue with next field if machine still has time*/
    private fun nextField(
        action: Action,
        currentPlantType: PlantType,
        plantsToActOn: MutableSet<Fertile>,
        finishedFertiles: Map<Int, Fertile>,
        bestMachine: Machine,
        currentLocation: Tile,
        farm: Farm,
        board: BoardData,
        yearTick: Int
    ): Fertile {
        TODO()
    }

    /**
     * Perform actions that are sorted after machine id*/
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
