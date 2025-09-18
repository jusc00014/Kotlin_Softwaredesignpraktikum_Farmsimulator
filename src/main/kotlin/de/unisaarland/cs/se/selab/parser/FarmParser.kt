
import com.github.erosb.jsonsKema.SchemaLoader
import de.unisaarland.cs.se.selab.farms.Machine
import de.unisaarland.cs.se.selab.farms.SowingPlan
import de.unisaarland.cs.se.selab.plants.PlantType
import de.unisaarland.cs.se.selab.farms.Farm
import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.TileType
import de.unisaarland.cs.se.selab.farms.Action

import org.json.JSONArray
import org.json.JSONObject

class FarmParser {
    private val machineNames = mutableListOf<String>()
    private val finishedMachines = mutableMapOf<Int, Machine>()

    fun parse(jsonFile: String, board: BoardData): Pair<List<Farm>, Map<Int, Machine>>? {

        val json = JSONObject(jsonFile)

        val farmsJson = json.getJSONArray("farms")

        val farms = parseFarms(farmsJson, board) ?: return null

        return Pair(farms, finishedMachines)
    }

    private fun parseFarms(json: JSONArray, board: BoardData): List<Farm>? {
        val farms = mutableListOf<Farm>()
        for (i in 0 until json.length()) {
            val corporationJson = json.getJSONObject(i)
            val farm = parseFarm(corporationJson, board) ?: return null
            farms.add(farm)
        }
        return farms
    }

    private fun parseFarm(json: JSONObject, board: BoardData): Farm? {
        val farmId = json.getInt("id")
        val farmName = json.getString("name")
        val (id, name) = validateFarmIdAndName(farmId, farmName)
        val farmsteadsJson = json.getJSONArray("farmsteads")
        val farmsteadsInt = farmsteadsJson.map { it as Int }.toMutableList()
        val farmsteads = validateFarmstead(farmsteadsInt, id, board)
        val fieldsInts = json.getJSONArray("fields").map { it as Int }.toMutableList()
        val fields = validateFields(fieldsInts, id, board)
        val plantationsInts = json.getJSONArray("plantations").map { it as Int }.toMutableList()
        val plantations = validatePlantations(plantationsInts, id, board)
        validateAtLeastOneFieldOrPlantationTile(fields, plantations)
        val sowingPlansToValidate = json.getJSONArray("sowing_plans")
        val sowingPlans = parseSowingPlans(sowingPlansToValidate, board) ?: return null
        val machines = validateMachines(id, json.getJSONArray("machines"), board)
        return Farm(id, farmsteads, fields, plantations, machines.map { it.id }, sowingPlans)
    }


    private fun validateFarmIdAndName(id: Int, name: String): Pair<Int, String> {
        return id to name
    }

    private fun validateFarmstead(idList: MutableList<Int>, farmId: Int, board: BoardData): MutableList<Int> {
        var returnList = idList
        for (id in idList) {
            val tile = board.getTileById(id)
            if (tile == null || tile.farmID != farmId || tile.type != TileType.FARMSTEAD) {
                returnList = mutableListOf()
            }
        }
        return returnList
    }

    private fun validateFields(idList: MutableList<Int>, farmId: Int, boardData: BoardData): MutableList<Int> {
        return idList
    }

    private fun validatePlantations(idList: MutableList<Int>, farmId: Int, board: BoardData): MutableList<Int> {
        return idList
    }

    private fun validateAtLeastOneFieldOrPlantationTile(
        fields: MutableList<Int>,
        plantations: MutableList<Int>
    ): Boolean {
        return (fields + plantations).isNotEmpty()
    }

    private fun parseSowingPlans(sowingPlansJson: JSONArray, board: BoardData): MutableList<SowingPlan>? {
        val sowingPlans = mutableListOf<SowingPlan>()
        for (sowingPlanJson in sowingPlansJson) {
            if (sowingPlanJson is JSONObject) {
                val sowingPlanId = sowingPlanJson.getInt("id")
                val sowingPlanTick = sowingPlanJson.getInt("tick")
                val (id, tick) = validateSowingPlanIdAndTick(sowingPlanId, sowingPlanTick)
                val sowingPlanPlant = sowingPlanJson.getString("plant")
                val plantType = validateSowingPlanPlantTypes(sowingPlanPlant) ?: return null
                val sowingPlanFields = sowingPlanJson.getJSONArray("fields")
                val fields: MutableList<Int>
                if (sowingPlanFields == null) {
                    fields = validateSowingPlanFieldsByRadius(
                        sowingPlanJson.getInt("location"),
                        sowingPlanJson.getInt("radius"),
                        board
                    )
                } else {
                    fields = sowingPlanFields.map { it as Int }.toMutableList()
                }
                sowingPlans.add(SowingPlan(id, tick, plantType, fields))
            }
        }
        return sowingPlans
    }

    private fun validateSowingPlanIdAndTick(id: Int, tick: Int): Pair<Int, Int> {
        return id to tick
    }

    private fun validateSowingPlanPlantTypes(plant: String): PlantType? {
        val plantType: PlantType
        when (plant) {
            "POTATO" -> plantType = PlantType.POTATO
            "WHEAT" -> plantType = PlantType.WHEAT
            "OAT" -> plantType = PlantType.OAT
            "PUMPKIN" -> plantType = PlantType.PUMPKIN
            else -> return null
        }
        return plantType
    }

    private fun validateSowingPlanFieldsByRadius(tileId: Int, radius: Int, board: BoardData): MutableList<Int> {
        val fieldCenter = board.getTileById(tileId) ?: return mutableListOf()
        return board.neighbors(radius, fieldCenter).map { it.id }.toMutableList()
    }

    private fun validateMachines(farmId: Int, machines: JSONArray, board: BoardData): List<Machine> {
        val machinesInstances = mutableListOf<Machine>()
        for (machineJson in machines) {
            if (machineJson is JSONObject) {
                val machineID = machineJson.getInt("id")
                val machineName = machineJson.getString("name")
                val (id, name) = validateMachineIdAndName(machineID, machineName)
                machineNames.add(name)
                val actionsJson = machineJson.getJSONArray("actions").map { it as String }
                val actions = validateMachineActions(actionsJson)
                val plantsJson = machineJson.getJSONArray("plants").map { it as String }
                val  plantTypes = validateMachinePlants(plantsJson)
                val duration = validateMachineDuration(machineJson.getInt("duration"))
                val location = validateMachineTile(machineJson.getInt("location"), farmId, board)
                val theMachine = Machine(id, actions, plantTypes, duration, location)
                machinesInstances.add(theMachine)
                this.finishedMachines[id] = theMachine
            }
        }
        return machinesInstances
    }

    private fun validateMachineIdAndName(id: Int, name: String): Pair<Int, String> {
        return id to name
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
                else -> return listOf()
            }
        }
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
                else -> return listOf()
            }
        }
        return plantTypes
    }

    private fun validateMachineDuration(dur: Int): Int {
        return dur
    }


    private fun validateMachineTile(tileId: Int, farmId: Int, board: BoardData): Int {
        var returnStuff = -1
        val tile = board.getTileById(tileId)
        if (tile != null && tile.type == TileType.FARMSTEAD && tile.farmID == farmId) {
            returnStuff = tileId
        }
        return returnStuff
    }

    private fun addMachineToLists(currentFarmMachines: MutableList<Machine>) {

    }

    private fun addToList(currentFarm: Farm, farms: MutableList<Farm>) {

    }
}