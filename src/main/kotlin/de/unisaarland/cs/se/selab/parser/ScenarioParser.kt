package de.unisaarland.cs.se.selab.parser

import com.github.erosb.jsonsKema.SchemaLoader
import com.github.erosb.jsonsKema.Validator
import com.github.erosb.jsonsKema.ValidatorConfig
import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.board.TileType
import de.unisaarland.cs.se.selab.clouds.Cloud
import de.unisaarland.cs.se.selab.clouds.CloudData
import de.unisaarland.cs.se.selab.farms.Farm
import de.unisaarland.cs.se.selab.farms.Machine
import de.unisaarland.cs.se.selab.incidents.AnimalAttack
import de.unisaarland.cs.se.selab.incidents.BeeHappy
import de.unisaarland.cs.se.selab.incidents.BrokenMachine
import de.unisaarland.cs.se.selab.incidents.CityExpansion
import de.unisaarland.cs.se.selab.incidents.CloudCreation
import de.unisaarland.cs.se.selab.incidents.Drought
import de.unisaarland.cs.se.selab.incidents.Incident
import org.json.JSONArray
import org.json.JSONObject

class ScenarioParser {
    var cloudData: CloudData = CloudData(0, mutableListOf())
    val incidents = mutableListOf<Incident>()
    val machineIds = mutableListOf<Int>()

    fun parse(jsonFile: String, board: BoardData, maxTick: Int, machines: Map<Int, Machine>, farms: List<Farm>): Pair<List<Incident>, CloudData>? {
        val schema = SchemaLoader.forURL("").load()

        val json = JSONObject(jsonFile)
        val clouds = json.getJSONArray("clouds")
        this.cloudData = parseClouds(clouds) ?: return null

        val incidentsJson = json.getJSONArray("incidents")
        val correct = parseIncidents(incidentsJson, board, maxTick, machines, cloudData)

        checkCityExpansions(board, farms)
    }

    private fun parseClouds(cloudsJson: JSONArray): CloudData? {
        val clouds: MutableList<Cloud> = mutableListOf()
        for (cloud in cloudsJson) {
            val id: Int
            val location: Int
            val duration: Int
            val amount: Int
            if (cloud is JSONObject) {
                id = cloud.getInt("id")
                location = cloud.getInt("location")
                duration = cloud.getInt("duration")
                amount = cloud.getInt("amount")
                clouds.add(Cloud(id, location, duration, amount, 10))
            } else {
                return null
            }
        }
        clouds.sortBy { it.id }
        return CloudData(clouds.last().id, clouds)
    }

    private fun parseIncidents(incidentsJson: JSONArray, board: BoardData, maxTick: Int, machines: Map<Int, Machine>, cloudData: CloudData): Boolean {
        var returnType = true
        for (incident in incidentsJson) {
            if (incident is JSONObject) {
                val id = incident.getInt("id")
                val tick = incident.getInt("tick")
                val type = incident.getString("type")
                returnType = typeCheckerAndValidator(incident, board, tick, machines, tick, id, type)
            } else {
                returnType = false
            }
        }
        return returnType
    }

    private fun typeCheckerAndValidator(obj: JSONObject,
                                        board: BoardData,
                                        maxTick: Int,
                                        machines: Map<Int, Machine>,
                                        tick: Int,
                                        id: Int,
                                        type: String): Boolean {
        var returnType = true
        if (maxTick > tick || this.incidents.any { it.id == id }) {
            returnType = false
        }
        when (type) {
            "CLOUD_CREATION" -> validateCloudCreation(id, tick, obj, board)
            "ANIMAL_ATTACK" -> validateAnimalAttack(id, tick, obj, board)
            "BEE_HAPPY" -> validateBeeHappy(id, tick, obj, board)
            "DROUGHT" -> validateDrought(id, tick, obj, board)
            "BROKEN_MACHINE" -> validateBrokenMachine(id, tick, obj, machines)
            "CITY_EXPANSION" -> validateCityExpansion(id, tick, obj, board)
            else -> returnType = false
        }
        return returnType
    }

    private fun validateCloudCreation(id: Int, tick: Int, obj: JSONObject, board: BoardData) {
        val incident: CloudCreation
        val location = obj.getInt("location")
        val radius = obj.getInt("radius")
        val duration = obj.getInt("duration")
        val amount = obj.getInt("amount")
        val tile = board.getTileById(location)
        if (tile != null) {
            val tiles = board.neighbors(radius, tile).toSet()
            incident = CloudCreation(id, tick, duration, amount, tiles, cloudData)
            incidents.add(incident)
        }
    }

    private fun validateAnimalAttack(id: Int, tick: Int, obj: JSONObject, board: BoardData) {
        val incident: AnimalAttack
        val location = obj.getInt("location")
        val radius = obj.getInt("radius")
        val tile = board.getTileById(location)
        if (tile != null) {
            val tiles = board.neighbors(radius, tile)
            val forestTiles = tiles.filter { it.type == TileType.FOREST }
            val affectedTiles = mutableSetOf<Tile>()
            for (forest in forestTiles) {
                affectedTiles.addAll(board.neighbors(radius, forest).filter { it.type == TileType.FIELD || it.type == TileType.PLANTATION })
            }
            val animalAttackTiles = affectedTiles.sortedBy { it.id }.toSet()
            incident = AnimalAttack(id, tick, animalAttackTiles)
            incidents.add(incident)
        }
    }

    private fun validateBeeHappy(id: Int, tick: Int, obj: JSONObject, board: BoardData) {
        val incident: BeeHappy
        val location = obj.getInt("location")
        val radius = obj.getInt("radius")
        val effect = obj.getInt("effect")
        val tile = board.getTileById(location)
        if (tile != null) {
            val tiles = board.neighbors(radius, tile)
            val meadowTiles = tiles.filter { it.type == TileType.MEADOW }
            val affectedTiles = mutableSetOf<Tile>()
            for (meadow in meadowTiles) {
                affectedTiles.addAll(board.neighbors(radius, meadow).filter { it.type == TileType.FIELD || it.type == TileType.PLANTATION })
            }
            val beeHappyTiles = affectedTiles.sortedBy { it.id }.toSet()
            incident = BeeHappy(id, tick, beeHappyTiles, effect)
            incidents.add(incident)
        }
    }

    private fun validateDrought(id: Int, tick: Int, obj: JSONObject, board: BoardData) {
        val incident: Drought
        val location = obj.getInt("location")
        val radius = obj.getInt("radius")
        val tile = board.getTileById(location)
        if (tile != null) {
            val tiles = board.neighbors(radius, tile).toSet()
            incident = Drought(id, tick, tiles)
            incidents.add(incident)
        }
    }

    private fun validateBrokenMachine(id: Int, tick: Int, obj: JSONObject, machines: Map<Int, Machine>) {
        val incident: BrokenMachine
        val duration = obj.getInt("duration")
        val machineId = obj.getInt("machineId")
        val machine = machines[machineId]
        if (machine != null) {
            incident = BrokenMachine(id, tick, duration, machine)
            incidents.add(incident)
            this.machineIds.add(machineId)
        }
    }

    private fun validateCityExpansion(id: Int, tick: Int, obj: JSONObject, board: BoardData) {
        val incident: CityExpansion
        val location = obj.getInt("location")
        val tile = board.getTileById(location)
        if (tile != null) {
            incident = CityExpansion(id, tick, tile, cloudData)
            incidents.add(incident)
        }
    }

    private fun checkCityExpansions(board: BoardData, farms: List<Farm>) {
        TODO()
    }
}