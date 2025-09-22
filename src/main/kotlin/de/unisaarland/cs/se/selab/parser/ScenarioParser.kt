package de.unisaarland.cs.se.selab.parser

import com.github.erosb.jsonsKema.JsonParser
import com.github.erosb.jsonsKema.SchemaLoader
import com.github.erosb.jsonsKema.Validator
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
import java.io.File

const val LOCATION = "location"
const val DURATION = "duration"
const val RADIUS = "radius"
const val PERCENT = 100

/**
 * Parser responsible to parse and validate scenario and cross-check all possible problems created by city expansion*/
class ScenarioParser {
    var cloudData: CloudData = CloudData(0, mutableListOf())
    val incidents = mutableListOf<Incident>()
    val machineIds = mutableListOf<Int>()

    /**
     * called by main with necessary arguments to parse and validate scenarioFile*/
    fun parse(
        jsonFile: String,
        board: BoardData,
        maxTick: Int,
        machines: Map<Int, Machine>,
        farms: List<Farm>,
        yearTick: Int
    ): Pair<List<Incident>, CloudData> {
        val schema = SchemaLoader.forURL("classpath://schema/scenario.schema").load()

        val validator = Validator.forSchema(schema)
        val instance = JsonParser(File(jsonFile).readText()).parse()

        val failure = validator.validate(instance)

        require(failure == null) { failure.toString() }

        val json = JSONObject(jsonFile)
        val clouds = json.getJSONArray("clouds")
        this.cloudData = parseClouds(clouds, board)

        val incidentsJson = json.getJSONArray("incidents")
        parseIncidents(incidentsJson, board, maxTick, machines, yearTick)

        checkCityExpansions(board, farms)
        return Pair(incidents, cloudData)
    }

    private fun parseClouds(cloudsJson: JSONArray, board: BoardData): CloudData {
        val clouds: MutableList<Cloud> = mutableListOf()
        val ids = mutableSetOf<Int>()
        val locations = mutableSetOf<Int>()
        for (cloud in cloudsJson) {
            require(cloud is JSONObject)
            val id: Int = cloud.getInt("id")
            require(id !in ids && id > 0)
            ids.add(id)
            val location: Int = cloud.getInt(LOCATION)
            require(location !in locations)
            val tile = board.getTileById(location)
            require(tile != null && tile.type != TileType.VILLAGE)
            locations.add(location)
            val duration: Int = cloud.getInt(DURATION)
            require(duration > 0 || duration == -1)
            val amount: Int = cloud.getInt("amount")
            require(amount > 0)
            clouds.add(Cloud(id, location, duration, amount))
        }
        clouds.sortBy { it.id }
        val maxId = clouds.lastOrNull()?.id ?: 0
        return CloudData(maxId, clouds)
    }

    private fun parseIncidents(
        incidentsJson: JSONArray,
        board: BoardData,
        maxTick: Int,
        machines: Map<Int, Machine>,
        yearTick: Int
    ) {
        for (incident in incidentsJson) {
            require(incident is JSONObject)
            val id = incident.getInt("id")
            val tick = incident.getInt("tick")
            val type = incident.getString("type")
            typeCheckerAndValidator(incident, board, maxTick, machines, tick, id, type, yearTick)
        }
    }

    private fun typeCheckerAndValidator(
        obj: JSONObject,
        board: BoardData,
        maxTick: Int,
        machines: Map<Int, Machine>,
        tick: Int,
        id: Int,
        type: String,
        yearTick: Int
    ) {
        require(tick < maxTick) { "[Incident $id] Tick $tick after maxTick $maxTick" }
        require(!this.incidents.any { it.id == id }) { "[Incident $id] ID already in use" }
        when (type) {
            "CLOUD_CREATION" -> validateCloudCreation(id, tick, obj, board)
            "ANIMAL_ATTACK" -> validateAnimalAttack(id, tick, obj, board)
            "BEE_HAPPY" -> validateBeeHappy(id, tick, obj, board, yearTick)
            "DROUGHT" -> validateDrought(id, tick, obj, board)
            "BROKEN_MACHINE" -> validateBrokenMachine(id, tick, obj, machines)
            "CITY_EXPANSION" -> validateCityExpansion(id, tick, obj, board)
            else -> throw IllegalArgumentException("[Incident $id] Invalid Incident type: $type")
        }
    }

    private fun validateCloudCreation(id: Int, tick: Int, obj: JSONObject, board: BoardData) {
        val incident: CloudCreation
        val location = obj.getInt(LOCATION)
        val radius = obj.getInt(RADIUS)
        val duration = obj.getInt(DURATION)
        val amount = obj.getInt("amount")
        val tile = board.getTileById(location)
        requireNotNull(tile) { "[CloudCreation $id] Invalid location: $location" }
        val tiles = board.neighbors(radius, tile).toSet()
        incident = CloudCreation(id, tick, duration, amount, tiles, cloudData)
        incidents.add(incident)
    }

    private fun validateAnimalAttack(id: Int, tick: Int, obj: JSONObject, board: BoardData) {
        val incident: AnimalAttack
        val location = obj.getInt(LOCATION)
        val radius = obj.getInt(RADIUS)
        val tile = board.getTileById(location)
        requireNotNull(tile) { "[AnimalAttack $id] Invalid location: $location" }
        val tiles = board.neighbors(radius, tile)
        val forestTiles = tiles.filter { it.type == TileType.FOREST }
        val affectedTiles = mutableSetOf<Tile>()
        for (forest in forestTiles) {
            affectedTiles.addAll(
                board.neighbors(radius, forest)
                    .filter { it.type == TileType.FIELD || it.type == TileType.PLANTATION }
            )
        }
        val animalAttackTiles = affectedTiles.sortedBy { it.id }.toSet()
        incident = AnimalAttack(id, tick, animalAttackTiles)
        incidents.add(incident)
    }

    private fun validateBeeHappy(id: Int, tick: Int, obj: JSONObject, board: BoardData, yearTick: Int) {
        val incident: BeeHappy
        val location = obj.getInt(LOCATION)
        val radius = obj.getInt(RADIUS)
        val effect = obj.getInt("effect")
        val tile = board.getTileById(location)
        requireNotNull(tile) { "[Bee Happy $id] Invalid location: $location" }
        val tiles = board.neighbors(radius, tile)
        val meadowTiles = tiles.filter { it.type == TileType.MEADOW }
        val affectedTiles = mutableSetOf<Tile>()
        for (meadow in meadowTiles) {
            affectedTiles.addAll(
                board.neighbors(radius, meadow)
                    .filter { it.type == TileType.FIELD || it.type == TileType.PLANTATION }
            )
        }
        val doubleEffect = effect.toDouble() / PERCENT
        val beeHappyTiles = affectedTiles.sortedBy { it.id }.toSet()
        incident = BeeHappy(id, tick, beeHappyTiles, doubleEffect, yearTick)
        incidents.add(incident)
    }

    private fun validateDrought(id: Int, tick: Int, obj: JSONObject, board: BoardData) {
        val incident: Drought
        val location = obj.getInt(LOCATION)
        val radius = obj.getInt(RADIUS)
        val tile = board.getTileById(location)
        requireNotNull(tile) { "[Drought $id] Invalid location: $location" }
        val tiles = board.neighbors(radius, tile).toSet()
        incident = Drought(id, tick, tiles)
        incidents.add(incident)
    }

    private fun validateBrokenMachine(id: Int, tick: Int, obj: JSONObject, machines: Map<Int, Machine>) {
        val incident: BrokenMachine
        val duration = obj.getInt(DURATION)
        val machineId = obj.getInt("machineId")
        val machine = machines[machineId]
        requireNotNull(machine) { "[Broken Machine $id] Invalid machine: $machineId" }
        incident = BrokenMachine(id, tick, duration, machine)
        incidents.add(incident)
        this.machineIds.add(machineId)
    }

    private fun validateCityExpansion(id: Int, tick: Int, obj: JSONObject, board: BoardData) {
        val incident: CityExpansion
        val location = obj.getInt(LOCATION)
        val tile = board.getTileById(location)
        requireNotNull(tile) { "[City Expansion $id] Invalid location: $location" }
        incident = CityExpansion(id, tick, tile, cloudData)
        incidents.add(incident)
    }

    private fun checkCityExpansionRequirementsCV(
        incident: CityExpansion,
        board: BoardData,
        tilesModified: MutableMap<Int, TileType>
    ) {
        val validTypes = setOf(TileType.ROAD, TileType.FIELD)
        require(incident.affectedTile.type in validTypes) {
            "[City Expansion ${incident.id}] TileType is invalid: ${incident.affectedTile.type}"
        }
        val affectedTileModified = tilesModified[incident.affectedTile.id]
        require(affectedTileModified == null || affectedTileModified in validTypes) {
            "[City Expansion ${incident.id}] TileType changed to invalid: ${incident.affectedTile.type}"
        }
        val neighbours = board.neighbors(1, incident.affectedTile)
        require(neighbours.any { (tilesModified[it.id] ?: it.type) in validTypes }) {
            "[City Expansion ${incident.id}] No adjoining Village tile found."
        }
        tilesModified[incident.affectedTile.id] = TileType.VILLAGE
    }

    private fun checkDroughtRequirementsCV(incident: Drought, tilesModified: Map<Int, TileType>) {
        val validTypes = setOf(TileType.FIELD, TileType.PLANTATION)
        require(incident.affectedTiles.any { it.type in validTypes }) {
            "[Drought $incident.id] No Fertile in affectedTiles"
        }
        val affectedTileModified = incident.affectedTiles.map { tilesModified[it.id] ?: it.type }
        require(affectedTileModified.any { it in validTypes }) {
            "[Drought $incident.id] No Fertile in affectedTiles at tick"
        }
    }

    private fun checkCloudCreationRequirementsCV(
        incident: CloudCreation,
        tilesModified: Map<Int, TileType>,
        cloudTilesForTick: Map<Int, Set<Int>>
    ) {
        cloudTilesForTick[incident.tick]?.let { tiles ->
            {
                require(incident.tiles.find { it.id in tiles } == null) {
                    "[CloudCreation $incident.id] Tiles overlap!"
                }
            }
        }
        require(incident.tiles.any { it.type != TileType.VILLAGE }) {
            "[CloudCreation $incident.id] No non village tile in tiles"
        }
        require(incident.tiles.any { (tilesModified[it.id] ?: it.type) != TileType.VILLAGE }) {
            "[CloudCreation $incident.id] No non village tile in tiles at tick"
        }
    }

    private fun checkCityExpansions(board: BoardData, farms: List<Farm>) {
        val tilesModified = mutableMapOf<Int, TileType>()
        val cloudTilesForTick = mutableMapOf<Int, Set<Int>>()
        // Sort is Stable!
        for (incident in incidents.sortedBy { it.id }.sortedBy { it.tick }) {
            when (incident) {
                is CityExpansion -> checkCityExpansionRequirementsCV(incident, board, tilesModified)
                is Drought -> checkDroughtRequirementsCV(incident, tilesModified)
                is CloudCreation -> checkCloudCreationRequirementsCV(incident, tilesModified, cloudTilesForTick)
                else -> continue
            }
        }

        for (farm in farms) {
            for (sowingPlan in farm.plans) {
                require(sowingPlan.fields.any { (tilesModified[it] ?: board.getTileById(it)) == TileType.FIELD }) {
                    "[SowingPlan ${sowingPlan.id}] No field in tiles at end of Simulation"
                }
            }
        }
    }
}
