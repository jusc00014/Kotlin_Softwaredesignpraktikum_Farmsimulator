package de.unisaarland.cs.se.selab.parser

import com.github.erosb.jsonsKema.JsonParser
import com.github.erosb.jsonsKema.SchemaLoader
import com.github.erosb.jsonsKema.Validator
import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.Fertile
import de.unisaarland.cs.se.selab.board.Field
import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.board.TileType
import de.unisaarland.cs.se.selab.farms.Action
import de.unisaarland.cs.se.selab.farms.Farm
import de.unisaarland.cs.se.selab.farms.Machine
import de.unisaarland.cs.se.selab.farms.SowingPlan
import de.unisaarland.cs.se.selab.plants.PlantType
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

const val ID = "id"
const val NAME = "name"

/**
* Parser class with responsibility to parse and validate farmFile*/
class FarmParser {
    private val machineNames = mutableListOf<String>()
    private val finishedMachines = mutableMapOf<Int, Machine>()
    private val sowingPlanIds = mutableListOf<Int>()

    /**
     * parse called by main with farmFile and result of mapParser. Returns farms and machines or null if invalid file*/
    fun parse(jsonFile: String, board: BoardData, maxTick: Int): Pair<List<Farm>, Map<Int, Machine>> {
        val schema = SchemaLoader.forURL("classpath://schema/farms.schema").load()
        // loads our farms.schema file as a schema
        val validator = Validator.forSchema(schema)
        // creates validator for this schema
        val instance = JsonParser(File(jsonFile).readText()).parse()
        // creates jsonValue from our farmFile
        val failure = validator.validate(instance)
        // ValidatorFailures after validation on our farmFile
        require(failure == null) { "${failure ?: "NULL"}" }
        // fine if it is null, otherwise file invalidated by schema
        val jsonString = File(jsonFile).readText()
        val json = JSONObject(jsonString)
        // farmFile as JSONObject
        val farmsJson = json.getJSONArray("farms")
        // gets the array of farms
        val farms = parseFarms(farmsJson, board, maxTick)
        // the farms (at least one)
        val (fieldsByFarms, plantationsByFarm, farmsteadsByFarms) = createFieldMapByFarms(farms)
        validateFarmsOpinions(fieldsByFarms, plantationsByFarm, farmsteadsByFarms, board)
        return Pair(farms, finishedMachines)
    }

    /**
     * parses the farms*/
    private fun parseFarms(json: JSONArray, board: BoardData, maxTick: Int): List<Farm> {
        val farms = mutableListOf<Farm>()
        val farmNames = mutableListOf<String>()
        val farmIds = mutableListOf<Int>()
        for (farmJson in json) {
            require(farmJson is JSONObject)
            val farm = parseFarm(farmJson, board, farmIds, farmNames, maxTick)
            farms.add(farm)
        }
        require(farms.isNotEmpty())
        return farms
    }

    private fun parseFarm(
        json: JSONObject,
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
        val farmsteads = farmsteadsJson.map { (it ?: error("farmSteadsJSON null")) as Int }.toMutableList()
        validateFarmstead(farmsteads, farmId, board)

        val fields = json.getJSONArray("fields").map { (it ?: error("fields null")) as Int }.toMutableList()
        validateFields(fields, farmId, board)
        val plantations = json.getJSONArray("plantations")
            .map { (it ?: error("plantations null")) as Int }.toMutableList()
        validatePlantations(plantations, farmId, board)
        validateAtLeastOneFieldOrPlantationTile(fields, plantations)

        val machines = validateMachines(farmId, json.getJSONArray("machines"), board)

        val sowingPlansToValidate = json.getJSONArray("sowingPlans")
        val sowingPlans = parseSowingPlans(sowingPlansToValidate, board, maxTick, machines, farmId)

        return Farm(farmId, farmsteads, fields, plantations, machines.map { it.id }, sowingPlans)
    }

    private fun validateFarmIdAndName(
        id: Int,
        name: String,
        farmIds: MutableList<Int>,
        farmNames: MutableList<String>
    ) {
        require(!farmIds.contains(id) && !farmNames.contains(name))
    }

    private fun validateFarmstead(idList: MutableList<Int>, farmId: Int, board: BoardData) {
        require(idList.isNotEmpty())
        val possibleSheds: MutableList<Tile> = mutableListOf()
        for (id in idList) {
            val tile = board.getTileById(id)
            require(tile != null && tile.farmID == farmId && tile.type == TileType.FARMSTEAD)
            possibleSheds.add(tile)
        }
        require(possibleSheds.any { it.shed })
    }

    private fun validateFields(idList: MutableList<Int>, farmId: Int, boardData: BoardData) {
        for (id in idList) {
            val tile = boardData.getTileById(id)
            require(tile != null && tile.farmID == farmId && tile.type == TileType.FIELD)
        }
    }

    private fun validatePlantations(idList: MutableList<Int>, farmId: Int, board: BoardData) {
        for (id in idList) {
            val tile = board.getTileById(id)
            require(tile != null && tile.farmID == farmId && tile.type == TileType.PLANTATION)
        }
    }

    private fun validateAtLeastOneFieldOrPlantationTile(
        fields: MutableList<Int>,
        plantations: MutableList<Int>
    ) {
        require((fields + plantations).isNotEmpty())
    }

    private fun parseSowingPlans(
        sowingPlansJson: JSONArray,
        board: BoardData,
        maxTick: Int,
        machines: List<Machine>,
        farmId: Int
    ): MutableList<SowingPlan> {
        val sowingPlanIds = mutableListOf<Int>()
        val sowingPlans = mutableListOf<SowingPlan>()
        for (sowingPlanJson in sowingPlansJson) {
            require(sowingPlanJson is JSONObject)

            val sowingPlanId = sowingPlanJson.getInt(ID)
            val sowingPlanTick = sowingPlanJson.getInt("tick")
            validateSowingPlanIdAndTick(sowingPlanId, sowingPlanTick, maxTick)
            sowingPlanIds.add(sowingPlanId)

            val sowingPlanPlant = sowingPlanJson.getString("plant")
            val plantType = validateSowingPlanPlantTypes(sowingPlanPlant)

            val sowingPlanFields = sowingPlanJson.optJSONArray("fields")
            val fields: MutableList<Tile> = mutableListOf()
            if (sowingPlanFields == null) {
                fields.addAll(
                    validateSowingPlanFieldsByRadius(
                        sowingPlanJson.getInt(LOCATION),
                        sowingPlanJson.getInt(RADIUS),
                        board,
                        farmId
                    )
                )
            } else {
                require(
                    !sowingPlanJson.keySet().contains(LOCATION) &&
                        !sowingPlanJson.keySet().contains(RADIUS) &&
                        !sowingPlanFields.isEmpty
                )
                val fieldInts = sowingPlanFields.map { (it ?: error("sowingPlanFields null")) as Int }.toMutableList()
                for (int in fieldInts) {
                    val tile = board.getTileById(int)
                    require(tile is Tile)
                    fields.add(tile)
                }
            }
            require(fields.isNotEmpty())
            val sowingPlan = SowingPlan(sowingPlanId, sowingPlanTick, plantType, fields.map { it.id })
            validateSowingPlanPossible(board, sowingPlan, machines, farmId)
            sowingPlans.add(sowingPlan)
        }
        return sowingPlans
    }

    private fun validateSowingPlanIdAndTick(id: Int, tick: Int, maxTick: Int) {
        require(!sowingPlanIds.contains(id) && tick < maxTick)
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

    private fun validateSowingPlanFieldsByRadius(
        tileId: Int,
        radius: Int,
        board: BoardData,
        farmId: Int
    ): MutableList<Tile> {
        val fieldCenter = board.getTileById(tileId)
        requireNotNull(fieldCenter)
        return board.neighbors(radius, fieldCenter)
            .filter { it is Fertile && it.farmID == farmId && it.type == TileType.FIELD }.toMutableList()
    }

    private fun validateMachines(farmId: Int, machines: JSONArray, board: BoardData): List<Machine> {
        val machinesInstances = mutableListOf<Machine>()
        for (machineJson in machines) {
            if (machineJson is JSONObject) {
                val machineID = machineJson.getInt(ID)
                val machineName = machineJson.getString(NAME)
                validateMachineIdAndName(machineID, machineName)

                val actionsJson = machineJson.getJSONArray("actions").map { (it ?: error("actions null")) as String }
                val actions = validateMachineActions(actionsJson)

                val plantsJson = machineJson.getJSONArray("plants").map { (it ?: error("plants null")) as String }
                val plantTypes = validateMachinePlants(plantsJson)

                val duration = machineJson.getInt("duration")
                val location = validateMachineTile(machineJson.getInt("location"), farmId, board)
                val locationTile = board.getTileById(location)
                requireNotNull(locationTile)

                val theMachine = Machine(machineID, actions, plantTypes, duration, locationTile)
                machinesInstances.add(theMachine)
                this.finishedMachines[machineID] = theMachine
            }
        }
        require(machinesInstances.isNotEmpty())
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
        require(actions.isNotEmpty() && actions.size == actions.toSet().size)
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
        require(plantTypes.isNotEmpty() && plantTypes.size == plantTypes.toSet().size)
        return plantTypes
    }

    private fun validateMachineTile(tileId: Int, farmId: Int, board: BoardData): Int {
        val tile = board.getTileById(tileId)
        require(tile != null && tile.type == TileType.FARMSTEAD && tile.farmID == farmId && tile.shed)
        return tileId
    }

    private fun createFieldMapByFarms(farms: List<Farm>):
        Triple<List<Int>, List<Int>, List<Int>> {
        val fields = mutableListOf<Int>()
        val plantations = mutableListOf<Int>()
        val farmsteads = mutableListOf<Int>()
        for (farm in farms) {
            fields.addAll(farm.fields)
            plantations.addAll(farm.plantages)
            farmsteads.addAll(farm.farmsteads)
        }
        return Triple(fields, plantations, farmsteads)
    }

    private fun validateFarmsOpinions(
        fields: List<Int>,
        plantations: List<Int>,
        farmsteads: List<Int>,
        boardData: BoardData
    ) {
        val fertiles = boardData.getFertiles()
        val boardFields = fertiles.values.filter { it.type == TileType.FIELD }.map { it.id }.sorted()
        val boardPlantations = fertiles.values.filter { it.type == TileType.PLANTATION }.map { it.id }.sorted()
        val boardFarmsteads = boardData.getTiles().filter { it.type == TileType.FARMSTEAD }.map { it.id }.sorted()
        val field = fields.sorted()
        val plantation = plantations.sorted()
        val farmstead = farmsteads.sorted()
        require(
            boardFields == field &&
                boardPlantations == plantation &&
                boardFarmsteads == farmstead
        )
    }

    private fun validateSowingPlanPossible(
        boardData: BoardData,
        sowingPlan: SowingPlan,
        machines: List<Machine>,
        farmId: Int
    ) {
        val fieldInts = sowingPlan.fields
        val fields: MutableList<Field> = mutableListOf()
        for (i in fieldInts) {
            val tile = boardData.getTileById(i) ?: return
            require(tile.farmID == farmId && tile is Field)
            fields.add(tile)
        }
        val plantTypes = fields.filter { it.possiblePlants.contains(sowingPlan.plant) }
        val possibleMachines = machines
            .filter { it.actions.contains(Action.SOWING) && it.plants.contains(sowingPlan.plant) }
        require(plantTypes.isNotEmpty() && possibleMachines.isNotEmpty())
    }
}
