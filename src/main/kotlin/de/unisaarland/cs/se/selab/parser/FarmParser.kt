package de.unisaarland.cs.se.selab.parser

import com.github.erosb.jsonsKema.JsonParser
import com.github.erosb.jsonsKema.SchemaLoader
import com.github.erosb.jsonsKema.Validator
import de.unisaarland.cs.se.selab.farms.Machine
import de.unisaarland.cs.se.selab.farms.SowingPlan
import de.unisaarland.cs.se.selab.plants.PlantType
import de.unisaarland.cs.se.selab.farms.Farm
import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.Fertile
import de.unisaarland.cs.se.selab.board.TileType
import de.unisaarland.cs.se.selab.farms.Action

import org.json.JSONArray
import org.json.JSONObject

const val ID = "id"
const val NAME = "name"


/**
* Parser class with responsibility to parse and validate farmFile*/
class FarmParser {
    private val machineNames = mutableListOf<String>()
    private val finishedMachines = mutableMapOf<Int, Machine>()

    /**
     * parse called by main with farmFile and result of mapParser. Returns farms and machines or null if invalid file*/
    fun parse(jsonFile: String, board: BoardData, maxTick: Int): Pair<List<Farm>, Map<Int, Machine>> {
        val schema = SchemaLoader.forURL("classpath:///schema/farms.schema").load()
        // loads our farms.schema file as a schema
        val validator = Validator.forSchema(schema)
        // creates validator for this schema
        val instance = JsonParser(jsonFile).parse()
        // creates jsonValue from our farmFile
        val failure = validator.validate(instance)
        // ValidatorFailures after validation on our farmFile
        require(failure == null) {failure.toString()}
        // fine if it is null, otherwise file invalidated by schema
        val json = JSONObject(jsonFile)
        // farmFile as JSONObject
        val farmsJson = json.getJSONArray("farms")
        // gets the array of farms
        val farms = parseFarms(farmsJson, board, maxTick)
        // the farms (at least one)
        return Pair(farms, finishedMachines)
    }

    /**
     * parses the farms*/
    private fun parseFarms(json: JSONArray, board: BoardData, maxTick: Int): List<Farm> {
        val farms = mutableListOf<Farm>()
        val farmNames = mutableListOf<String>()
        val farmIds = mutableListOf<Int>()
        for (farmJson in json) {
            require (farmJson is JSONObject)
            val farm = parseFarm(farmJson, board, farmIds, farmNames, maxTick)
            farms.add(farm)
        }
        require (farms.isNotEmpty())
        return farms
    }

    private fun parseFarm(json: JSONObject,
                          board: BoardData,
                          farmIds: MutableList<Int>,
                          farmNames: MutableList<String>,
                          maxTick: Int
    ): Farm {
        val farmId = json.getInt(ID)
        val farmName = json.getString(NAME)
        validateFarmIdAndName(farmId, farmName, farmIds, farmNames)
        farmIds.add(farmId)
        farmNames.add(farmName)

        val farmsteadsJson = json.getJSONArray("farmsteads")
        val farmsteads = farmsteadsJson.map { it as Int }.toMutableList()
        validateFarmstead(farmsteads, farmId, board)

        val fields = json.getJSONArray("fields").map { it as Int }.toMutableList()
        validateFields(fields, farmId, board)
        val plantations = json.getJSONArray("plantations").map { it as Int }.toMutableList()
        validatePlantations(plantations, farmId, board)
        validateAtLeastOneFieldOrPlantationTile(fields, plantations)

        val sowingPlansToValidate = json.getJSONArray("sowing_plans")
        val sowingPlans = parseSowingPlans(sowingPlansToValidate, board, maxTick)

        val machines = validateMachines(farmId, json.getJSONArray("machines"), board)
        return Farm(farmId, farmsteads, fields, plantations, machines.map { it.id }, sowingPlans)
    }


    private fun validateFarmIdAndName(id: Int,
                                      name: String,
                                      farmIds: MutableList<Int>,
                                      farmNames: MutableList<String>
    ) {
        require(!farmIds.contains(id) && !farmNames.contains(name))
    }

    private fun validateFarmstead(idList: MutableList<Int>, farmId: Int, board: BoardData) {
        require (idList.isNotEmpty())
        for (id in idList) {
            val tile = board.getTileById(id)
            require (tile != null && tile.farmID == farmId && tile.type == TileType.FARMSTEAD)
        }
    }

    private fun validateFields(idList: MutableList<Int>, farmId: Int, boardData: BoardData) {
        for (id in idList) {
            val tile = boardData.getTileById(id)
            require (tile != null && tile.farmID == farmId && tile.type == TileType.FIELD)
        }
    }

    private fun validatePlantations(idList: MutableList<Int>, farmId: Int, board: BoardData) {
        for (id in idList) {
            val tile = board.getTileById(id)
            require (tile != null && tile.farmID == farmId && tile.type == TileType.PLANTATION)
        }
    }

    private fun validateAtLeastOneFieldOrPlantationTile(
        fields: MutableList<Int>,
        plantations: MutableList<Int>
    ) {
        require ((fields + plantations).isNotEmpty())
    }

    private fun parseSowingPlans(sowingPlansJson: JSONArray, board: BoardData, maxTick: Int): MutableList<SowingPlan> {
        val sowingPlanIds = mutableListOf<Int>()
        val sowingPlans = mutableListOf<SowingPlan>()
        for (sowingPlanJson in sowingPlansJson) {
            require (sowingPlanJson is JSONObject)

            val sowingPlanId = sowingPlanJson.getInt(ID)
            val sowingPlanTick = sowingPlanJson.getInt("tick")
            validateSowingPlanIdAndTick(sowingPlanId, sowingPlanTick, sowingPlanIds, maxTick)
            sowingPlanIds.add(sowingPlanId)

            val sowingPlanPlant = sowingPlanJson.getString("plant")
            val plantType = validateSowingPlanPlantTypes(sowingPlanPlant)

            val sowingPlanFields = sowingPlanJson.optJSONArray("fields")
            val fields: MutableList<Int>
            if (sowingPlanFields == null) {
                fields = validateSowingPlanFieldsByRadius(
                    sowingPlanJson.getInt(LOCATION),
                    sowingPlanJson.getInt(RADIUS),
                     board
                )
            } else {
                require(!sowingPlanJson.keySet().contains(LOCATION) && !sowingPlanJson.keySet().contains(RADIUS))
                fields = sowingPlanFields.map { it as Int }.toMutableList()
            }
            require(fields.isNotEmpty())
            sowingPlans.add(SowingPlan(sowingPlanId, sowingPlanTick, plantType, fields))
        }
        return sowingPlans
    }

    private fun validateSowingPlanIdAndTick(id: Int, tick: Int, sowingPlanIds: MutableList<Int>, maxTick: Int) {
        require (!sowingPlanIds.contains(id) && tick <= maxTick)
    }

    private fun validateSowingPlanPlantTypes(plant: String): PlantType {
        var plantType: PlantType = PlantType.POTATO
        when (plant) {
            "POTATO" -> plantType = PlantType.POTATO
            "WHEAT" -> plantType = PlantType.WHEAT
            "OAT" -> plantType = PlantType.OAT
            "PUMPKIN" -> plantType = PlantType.PUMPKIN
        }
        return plantType
    }

    private fun validateSowingPlanFieldsByRadius(tileId: Int, radius: Int, board: BoardData): MutableList<Int> {
        val fieldCenter = board.getTileById(tileId)
        require(fieldCenter != null)
        return board.neighbors(radius, fieldCenter).filter{ it is Fertile }.map { it.id }.toMutableList()
    }

    private fun validateMachines(farmId: Int, machines: JSONArray, board: BoardData): List<Machine> {
        val machinesInstances = mutableListOf<Machine>()
        for (machineJson in machines) {
            if (machineJson is JSONObject) {
                val machineID = machineJson.getInt(ID)
                val machineName = machineJson.getString(NAME)
                validateMachineIdAndName(machineID, machineName)

                val actionsJson = machineJson.getJSONArray("actions").map { it as String }
                val actions = validateMachineActions(actionsJson)

                val plantsJson = machineJson.getJSONArray("plants").map { it as String }
                val  plantTypes = validateMachinePlants(plantsJson)

                val duration = machineJson.getInt("duration")
                val location = validateMachineTile(machineJson.getInt("location"), farmId, board)

                val theMachine = Machine(machineID, actions, plantTypes, duration, location)
                machinesInstances.add(theMachine)
                this.finishedMachines[machineID] = theMachine
            }
        }
        require (machinesInstances.isNotEmpty())
        return machinesInstances
    }

    private fun validateMachineIdAndName(id: Int, name: String) {
        require(!this.machineNames.contains(name) && this.finishedMachines[id] == null)
        machineNames.add(name)
    }

    private fun validateMachineActions(actions: List<String>): List<Action> {
        val actionsToReturn: MutableList<Action> = mutableListOf()
        for (action in actions) {
            when (action) {
                "SOWING" -> actionsToReturn.add(Action.SOWING)
                "CUTTING" -> actionsToReturn.add(Action.CUTTING)
                "MOWING" -> actionsToReturn.add(Action.MOWING)
                "WEEDING" -> actionsToReturn.add(Action.WEEDING)
                "IRRIGATING" -> actionsToReturn.add(Action.IRRIGATING)
                "HARVESTING" -> actionsToReturn.add(Action.HARVESTING)
            }
        }
        require (actions.isNotEmpty() && actions.size == actions.toSet().size)
        return actionsToReturn
    }

    private fun validateMachinePlants(plants: List<String>): List<PlantType> {
        val plantTypes = mutableListOf<PlantType>()
        for (plant in plants) {
            when (plant) {
                "POTATO" -> plantTypes.add(PlantType.POTATO)
                "WHEAT" -> plantTypes.add(PlantType.WHEAT)
                "OAT" -> plantTypes.add(PlantType.OAT)
                "PUMPKIN" -> plantTypes.add(PlantType.PUMPKIN)
                "APPLE" -> plantTypes.add(PlantType.APPLE)
                "GRAPE" -> plantTypes.add(PlantType.GRAPE)
                "ALMOND" -> plantTypes.add(PlantType.ALMOND)
                "CHERRY" -> plantTypes.add(PlantType.CHERRY)
            }
        }
        require (plantTypes.isNotEmpty() && plantTypes.size == plantTypes.toSet().size)
        return plantTypes
    }

    private fun validateMachineTile(tileId: Int, farmId: Int, board: BoardData): Int {
        val tile = board.getTileById(tileId)
        require (tile != null && tile.type == TileType.FARMSTEAD && tile.farmID == farmId)
        return tileId
    }
}