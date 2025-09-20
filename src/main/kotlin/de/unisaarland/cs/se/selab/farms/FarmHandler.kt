package de.unisaarland.cs.se.selab.farms
import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.Fertile
import de.unisaarland.cs.se.selab.board.Field
import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.farms.Machine
import de.unisaarland.cs.se.selab.plants.PlantData
import de.unisaarland.cs.se.selab.plants.PlantType
import java.util.concurrent.TimeUnit
import kotlin.collections.orEmpty

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
            val finishedFields = mutableMapOf<Int, Fertile>()
            sow(sowFields, farm, remainingMachines, finishedFields, fertiles, board, yearTick)
            val fieldMap = createActionMap(farm.fields, fertiles, yearTick)
            val plantationMap = createActionMap(farm.plantages, fertiles, yearTick)
            for ((action, fertileType) in listOf(
                Action.HARVESTING to plantationMap[Action.HARVESTING],
                Action.HARVESTING to fieldMap[Action.HARVESTING],
                Action.CUTTING to plantationMap[Action.CUTTING],
            )) {
                performPrioritizedAction(action, remainingMachines, fertileType!!, finishedFields,board, farm, yearTick)
            }
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
            var remainingTime = TICKTIME - 2 * machine.duration
            var currentField: Fertile? = field
            while (remainingTime >= 0 && commonFields.isNotEmpty() && currentField != null) {
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
                remainingTime -= machine.duration
            }
            // logger machineFinished
        }
    }

    /**
     * Assemble fields and the actions they can perform in this tick*/
    private fun createActionMap(
        fields: List<Int>,
        fertiles: Map<Int, Fertile>,
        yearTick: Int
    ): MutableMap<Action, MutableSet<Fertile>> {
        val actionMap = mutableMapOf<Action, MutableSet<Fertile>>()
        val acts = listOf(Action.CUTTING, Action.MOWING, Action.WEEDING, Action.IRRIGATING, Action.HARVESTING)
        for (currentAct in acts) {
            actionMap[currentAct] = mutableSetOf()
        }
        for (fieldId in fields) {
            val fieldFound = fertiles[fieldId] ?: continue
            val performableActions = fieldFound.performableActions(yearTick)
            for (currentAct in acts) {
                if (currentAct in performableActions) {
                    actionMap[currentAct]!!.add(fieldFound)
                }
            }
        }
        return actionMap
    }

    /**
     * Perform actions  that are sorted by field id*/
    private fun performPrioritizedAction(
        action: Action,
        remainingMachines: MutableList<Machine>,
        plantsToActOn: MutableSet<Fertile>,
        finishedFields: MutableMap<Int, Fertile>,
        board: BoardData,
        farm: Farm,
        yearTick: Int
    ) {
        for (field in plantsToActOn) {
            if (field.id in finishedFields) {
                continue
            }
            val plant = field.plant
            val machine = findBestMachine(field, remainingMachines, action, plant.type, board, farm) ?: continue
            plant.performAction(action, yearTick)
            finishedFields[field.id] = field
            remainingMachines.remove(machine)
            var remainingTime = TICKTIME - machine.duration
            var currentField: Fertile? = field
            plantsToActOn.remove(field)
            while (remainingTime -  machine.duration >= 0 && currentField != null) {
                if (action == Action.HARVESTING) {
                    currentField = nextField(
                        action,
                        plant.type,
                        plantsToActOn,
                        finishedFields,
                        machine,
                        currentField,
                        farm,
                        board,
                        yearTick)
                } else {
                    currentField = nextField(
                        action,
                        null,
                        plantsToActOn,
                        finishedFields,
                        machine,
                        currentField,
                        farm,
                        board,
                        yearTick)
                }
                remainingTime -= machine.duration
            }
        }
    }

    /**
     * Find machine with best duration for this field and action*/
    private fun findBestMachine(
        fertile: Fertile,
        remainingMachines: MutableList<Machine>,
        action: Action,
        plantType: PlantType?,
        board: BoardData,
        farm: Farm
    ): Machine? {
        var bestMachine: Machine? = null
        if (remainingMachines.isEmpty()) {
            return null
        }
        var bestDuration = TICKTIME-1
        for (machine in remainingMachines) {
            if (machine.duration < bestDuration && action in machine.actions && plantType in machine.plants) {
                if (pathFinder.reachable(machine.location, fertile, farm.id, board)){
                    bestMachine = machine
                    bestDuration = machine.duration
                }
            }
        }
        return bestMachine
    }

    /**
     * Continue with next field if machine still has time*/
    private fun nextField(
        action: Action,
        currentPlantType: PlantType?,
        plantsToActOn: MutableSet<Fertile>,
        finishedFertiles: MutableMap<Int, Fertile>,
        machine: Machine,
        currentLocation: Tile,
        farm: Farm,
        board: BoardData,
        yearTick: Int
    ): Fertile? {
        for (fertile in plantsToActOn) {
            if (fertile.id in finishedFertiles) {
                continue
            }
            if (fertile.plant.type !in machine.plants ||
                (action in setOf(Action.HARVESTING, Action.SOWING) && fertile.plant.type != currentPlantType)) {
                continue
            }
            val harvest = action == Action.HARVESTING
            if (pathFinder.canContinue(currentLocation, fertile, farm.id, board, harvest)) {
                val plant = fertile.plant
                plant.performAction(action, yearTick)
                finishedFertiles[fertile.id] = fertile
                plantsToActOn.remove(fertile)
                return fertile
            }
        }
        return null
    }

    /**
     * Perform actions that are sorted after machine id*/
    private fun performNonPrioritizedAction(
        action: Action,
        remainingMachines: MutableList<Machine>,
        plantsToActOn: MutableSet<Fertile>,
        alternativePlants: MutableList<Fertile>,
        finishedFields: MutableMap<Int, Fertile>,
        board: BoardData,
        farm: Farm,
        yearTick: Int
    ) {
        for (machine in remainingMachines) {
            if (action !in machine.actions) {
                continue
            }
            for (fertile in plantsToActOn) {
                if (fertile.id in finishedFields || fertile.plant.type !in machine.plants ||
                    !pathFinder.reachable(machine.location, fertile, farm.id, board)) {
                    continue
                }
                fertile.plant.performAction(action, yearTick)
                finishedFields[fertile.id] = fertile
                remainingMachines.remove(machine)
                var remainingTime = TICKTIME - machine.duration
                var currentField: Fertile? = fertile
                val allPlants = plantsToActOn.addAll(alternativePlants)
                while (remainingTime - machine.duration >= 0 && currentField != null) {
                    currentField = nextField(action,
                        null,
                        plantsToActOn,
                        finishedFields,
                        machine,
                        currentField,
                        farm,
                        board,
                        yearTick)
                    remainingTime -= machine.duration
                }
                break
            }
        }
    }
}
