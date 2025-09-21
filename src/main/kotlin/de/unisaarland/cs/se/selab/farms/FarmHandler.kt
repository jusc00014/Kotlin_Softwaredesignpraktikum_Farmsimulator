package de.unisaarland.cs.se.selab.farms
import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.Fertile
import de.unisaarland.cs.se.selab.board.Field
import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.plants.PlantData
import de.unisaarland.cs.se.selab.plants.PlantType
//import java.util.concurrent.TimeUnit
import kotlin.collections.orEmpty
import de.unisaarland.cs.se.selab.logger.Logger
import kotlin.collections.set

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
            Logger.farmStartAction(farm.id)
            val remainingMachines = assembleMachines(farm)
            val sowFields = assembleSowableFields(farm.fields, fertiles, yearTick)
            val finishedFields = mutableMapOf<Int, Fertile>()
            sow(sowFields, farm, remainingMachines, finishedFields, board, yearTick)
            val fieldMap = createActionMap(farm.fields, fertiles, yearTick)
            val plantationMap = createActionMap(farm.plantages, fertiles, yearTick)
            for ((action, fertileType) in listOf(
                Action.HARVESTING to plantationMap[Action.HARVESTING],
                Action.HARVESTING to fieldMap[Action.HARVESTING],
                Action.CUTTING to plantationMap[Action.CUTTING]
            )) {
                performPrioritizedAction(action, remainingMachines, fertileType!!, finishedFields,board, farm, yearTick)
            }
            for (machine in remainingMachines) {
                performNonPrioritizedAction(
                    machine,
                    remainingMachines,
                    fieldMap,
                    plantationMap,
                    finishedFields,
                    board,
                    farm,
                    yearTick
                )
            }
            Logger.farmFinishedAction(farm.id)
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
        board: BoardData,
        yearTick: Int
    ) {
        val planIds = mutableListOf<Int>()
        for (plan in farm.plans) {
            if (plan.isActive(yearTick)) {
                planIds.add(plan.id)
            }
        }
        Logger.farmSowingPlan(farm.id, planIds)
        for (plan in farm.plans) {
            executeSowingPlan(farm, plan, sowableFields, remainingMachines, finishedFields, board, yearTick)
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
        //fertiles: Map<Int, Fertile>,
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
        val commonFields = sowFields.intersect(sowableFields[toSow].orEmpty().toSet()).toMutableSet()
        for (field in commonFields) {
            if (field.id in finishedFields) {
                continue
            }
            val machine = findBestMachine(
                field, remainingMachines, Action.SOWING, toSow, board, farm) ?: continue
            Logger.machinePerformedAction(machine.id, Action.SOWING, field.id, machine.duration)
            Logger.machineSowed(machine.id, toSow, plan.id)
            finishedFields[field.id] = field
            remainingMachines.remove(machine)
            farm.removeSowingPlan(plan)
            var remainingTime = TICKTIME - 2 * machine.duration
            var currentField: Fertile? = field
            while (remainingTime >= 0 && commonFields.isNotEmpty()) {
                currentField = nextField(
                    Action.SOWING,
                    toSow,
                    commonFields,
                    finishedFields,
                    machine,
                    currentField!!,
                    farm,
                    board,
                    yearTick) ?: break
                currentField.plant.sow(toSow, plantData[toSow]!!, yearTick)
                Logger.machinePerformedAction(machine.id, Action.SOWING, currentField.id, machine.duration)
                Logger.machineSowed(machine.id, toSow, plan.id)
                finishedFields[currentField.id] = currentField
                remainingTime -= machine.duration
            }
            Logger.machineFinished(machine.id, machine.location!!.id)
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
            performAction(action, field, machine, remainingMachines, finishedFields, farm.id, yearTick)
            var remainingTime = TICKTIME - 2 * machine.duration
            var currentField: Fertile? = field
            plantsToActOn.remove(field)
            if (action == Action.HARVESTING) {
                continueWithHarvesting(currentField!!, plantsToActOn, finishedFields, machine, board, farm, yearTick)
            } else {
                while (remainingTime >= 0 && currentField != null) {
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
                    remainingTime -= machine.duration
                }
                Logger.machineFinished(machine.id, machine.location!!.id)
            }
        }
    }

    /**
     * Find machine with the best duration for this field and action*/
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
                if (pathFinder.reachable(machine.location!!, fertile, farm.id, board)){
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
                if (action != Action.SOWING) {
                    performAction(action, fertile, machine, null, finishedFertiles, farm.id, yearTick)
                }
                plantsToActOn.remove(fertile)
                return fertile
            }
        }
        return null
    }

    /**
     * Perform actions that are sorted after machine id*/
    private fun performNonPrioritizedAction(
        machine: Machine,
        remainingMachines: MutableList<Machine>,
        fieldMap: Map<Action, MutableSet<Fertile>>,
        plantationMap: Map<Action, MutableSet<Fertile>>,
        finishedFields: MutableMap<Int, Fertile>,
        board: BoardData,
        farm: Farm,
        yearTick: Int
    ) {
        for ((action, fertileType) in listOf(
            Action.IRRIGATING to fieldMap[Action.IRRIGATING],
            Action.WEEDING to fieldMap[Action.WEEDING],
            Action.IRRIGATING to plantationMap[Action.IRRIGATING],
            Action.MOWING to plantationMap[Action.MOWING]
        )) {
            if (action !in machine.actions || fertileType!!.isEmpty()) {
                continue
            }
            for (fertile in fertileType) {
                if (fertile.id in finishedFields || fertile.plant.type !in machine.plants ||
                    pathFinder.reachable(machine.location!!, fertile, farm.id, board)) {
                    continue
                }
                performAction(action, fertile, machine, remainingMachines, finishedFields, farm.id, yearTick)
                var remainingTime = TICKTIME - 2* machine.duration
                var currentField: Fertile? = fertile
                val plantsToActOn =
                        (fieldMap[action].orEmpty() + plantationMap[action].orEmpty())
                            .toSortedSet(compareBy { it.id })
                while (remainingTime >= 0 && currentField != null) {
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
                    remainingTime -= machine.duration
                }
                Logger.machineFinished(machine.id, machine.location!!.id)
                break
            }
        }
    }

    private fun performAction(
        action: Action,
        fertile: Fertile,
        machine: Machine,
        remainingMachines: MutableList<Machine>?,
        finishedFields: MutableMap<Int, Fertile>,
        farmId: Int,
        yearTick: Int
    ) {
        val amount: Int? = fertile.plant.performAction(action, yearTick)
        Logger.machinePerformedAction(machine.id, action, fertile.id, machine.duration)
        finishedFields[fertile.id] = fertile
        remainingMachines?.remove(machine)
        if (action == Action.HARVESTING) {
            Logger.machineCollected(farmId, machine.id, amount!!, fertile.plant.type)
        }
    }

    private fun continueWithHarvesting(
        field: Fertile,
        plantsToActOn: MutableSet<Fertile>,
        finishedFields: MutableMap<Int, Fertile>,
        machine: Machine,
        board: BoardData,
        farm: Farm,
        yearTick: Int) {
        var remainingTime = TICKTIME - 2 * machine.duration
        var lastField = field
        var currentField: Fertile? = field
        while (remainingTime >= 0 && currentField != null) {
            lastField = currentField
            currentField = nextField(
                Action.HARVESTING,
                currentField.plant.type,
                plantsToActOn,
                finishedFields,
                machine,
                currentField,
                farm,
                board,
                yearTick
            )
            remainingTime -= machine.duration
        }
        if (!pathFinder.reachableWithHarvest(lastField, machine.location!!, farm.id, board)) {
            val loc = pathFinder.findNearestShed(lastField, farm, board)
            if (loc != null) {
                machine.location = loc
                Logger.machineFinished(machine.id, machine.location!!.id)
            } else {
                machine.setStuck()
                Logger.machineFinishedNoReturn(machine.id)
                }
        }
        // logger machineUnloads
    }
}
