package de.unisaarland.cs.se.selab.parser

import com.github.erosb.jsonsKema.FormatValidationPolicy
import com.github.erosb.jsonsKema.JsonParser
import com.github.erosb.jsonsKema.SchemaLoader
import com.github.erosb.jsonsKema.Validator
import com.github.erosb.jsonsKema.ValidatorConfig
import de.unisaarland.cs.se.selab.Constants
import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.Coordinate
import de.unisaarland.cs.se.selab.board.Direction
import de.unisaarland.cs.se.selab.board.Field
import de.unisaarland.cs.se.selab.board.Plantation
import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.board.TileType
import de.unisaarland.cs.se.selab.plants.Plant
import de.unisaarland.cs.se.selab.plants.PlantData
import de.unisaarland.cs.se.selab.plants.PlantType
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import kotlin.math.abs

/**
 * Parses the Map Data into a Map of Ids and Tiles
 */
class MapParser(
    private var tiles: MutableMap<Int, Tile>,
) {
    /**
     * Strings to check if the Json contains invalid keys
     */
    companion object {
        const val FARM = "farm"
        const val AIRFLOW = "airflow"
        const val DIRECTION = "direction"
        const val CAPACITY = "capacity"
        const val PLANT = "plant"
        const val POSSIBLE_PLANTS = "possiblePlants"
        const val SHED = "shed"
        var farmsteadInvalidKeys = setOf(CAPACITY, PLANT, POSSIBLE_PLANTS)
        val fieldInvalidKeys = setOf(PLANT, SHED)
        val forestInvalidKeys = setOf(FARM, CAPACITY, PLANT, POSSIBLE_PLANTS, SHED)
        val meadowInvalidKeys = setOf(FARM, CAPACITY, PLANT, POSSIBLE_PLANTS, SHED)
        val plantationInvalidKeys = setOf(POSSIBLE_PLANTS, SHED)
        val roadInvalidKeys = setOf(FARM, CAPACITY, PLANT, POSSIBLE_PLANTS, SHED)
        val villageInvalidKeys = setOf(FARM, AIRFLOW, CAPACITY, PLANT, POSSIBLE_PLANTS, SHED)
    }
    val coordList: MutableList<Coordinate> = mutableListOf()

    /**
     * Parse function which is called by main
     * Parses a given input into BoardData
     */
    fun parse(jsonFile: String, yearTick: Int): Pair<BoardData, Map<PlantType, PlantData>> {
        val schema = SchemaLoader.forURL("classpath://schema/map.schema").load()
        val validator = Validator.create(schema, ValidatorConfig(FormatValidationPolicy.ALWAYS))
        val instance = JsonParser(File(jsonFile).readText()).parse()
        val failure = validator.validate(instance)
        require(failure == null) { "${failure ?: "NULL"}" }
        val plantTypeList = listOf(
            PlantType.POTATO,
            PlantType.OAT,
            PlantType.WHEAT,
            PlantType.PUMPKIN,
            PlantType.CHERRY,
            PlantType.APPLE,
            PlantType.GRAPE,
            PlantType.ALMOND
        )
        val plantMap: MutableMap<PlantType, PlantData> = mutableMapOf()
        plantTypeList.forEach { plantMap[it] = createPlantData(it) }
        val jsonString = File(jsonFile).readText()
        val json = JSONObject(jsonString)
        val tilesJson = json.getJSONArray("tiles")
        for (i in 0 until tilesJson.length()) {
            val tile = tilesJson.getJSONObject(i)
            val (id, validTile) = validateTile(tile, plantMap, yearTick)
            val keys = tile.keySet()
            validateNoAdditionalKeysInJson(keys, validTile)
            addTileToMap(id to validTile)
        }
        val boardData = BoardData(tiles)
        validateAdjoiningTiles(boardData)
        return boardData to plantMap
    }
    private fun checkNoMutualKeys(jsonKeys: Set<String>, invalidKeys: Set<String>) {
        require(!jsonKeys.any { it in invalidKeys })
    }
    private fun validateNoAdditionalKeysInJson(keys: Set<String>, tile: Tile) {
        val directionSet = mutableSetOf<String>()
        if (tile.airflow == null) {
            directionSet += DIRECTION
        }
        when (tile.type) {
            TileType.FARMSTEAD -> {
                checkNoMutualKeys(keys, farmsteadInvalidKeys + directionSet)
            }
            TileType.FIELD -> {
                checkNoMutualKeys(keys, fieldInvalidKeys + directionSet)
            }
            TileType.FOREST -> {
                checkNoMutualKeys(keys, forestInvalidKeys + directionSet)
            }
            TileType.MEADOW -> {
                checkNoMutualKeys(keys, meadowInvalidKeys + directionSet)
            }
            TileType.PLANTATION -> {
                checkNoMutualKeys(keys, plantationInvalidKeys + directionSet)
            }
            TileType.ROAD -> {
                checkNoMutualKeys(keys, roadInvalidKeys + directionSet)
            }
            TileType.VILLAGE -> {
                checkNoMutualKeys(keys, villageInvalidKeys + directionSet)
            }
        }
    }
    private fun validateAdjoiningTiles(boardData: BoardData) {
        tiles.forEach {
            when (it.value.type) {
                TileType.FARMSTEAD -> {
                    val farmId = it.value.farmID
                    val neighborOfFarm = boardData.neighbors(1, it.value, excludeSelf = true)
                    require(
                        neighborOfFarm.none
                            { itt -> itt.type == TileType.MEADOW || itt.type == TileType.FARMSTEAD }
                    ) { "Farmstead cannot adjoin meadow or farmstead" }
                    require(
                        neighborOfFarm.none
                            { itt ->
                                if (itt.type == TileType.FIELD || itt.type == TileType.PLANTATION) {
                                    itt.farmID != farmId
                                } else {
                                    false
                                }
                            }
                    ) { "Farmstead cannot adjoin fields or plantations of different farms" }
                }
                TileType.MEADOW -> {
                    require(
                        boardData.neighbors(1, it.value, true).none { itt -> itt.type == TileType.MEADOW }
                    ) { "Meadow cannot adjoin another meadow" }
                }
                TileType.VILLAGE -> {
                    require(
                        boardData.neighbors(1, it.value, true).none { itt -> itt.type == TileType.FOREST }
                    ) { "Village shouldn't adjoin forest tiles" }
                }
                else -> {}
            }
        }
    }
    private fun createPlantData(plantType: PlantType): PlantData {
        return when (plantType) {
            PlantType.POTATO -> Constants.potato
            PlantType.WHEAT -> Constants.wheat
            PlantType.OAT -> Constants.oat
            PlantType.PUMPKIN -> Constants.pumpkin
            PlantType.APPLE -> Constants.apple
            PlantType.ALMOND -> Constants.almond
            PlantType.CHERRY -> Constants.cherry
            PlantType.GRAPE -> Constants.grape
        }
    }

    private fun validateTile(json: JSONObject, plantMap: Map<PlantType, PlantData>, yearTick: Int): Pair<Int, Tile> {
        val tileId = json.getInt("id")
        val coord = json.getJSONObject("coordinates")
        val xCoord = coord.getInt("x")
        val yCoord = coord.getInt("y")
        val validCoord = validateTileIdAndCoordinate(tileId, xCoord to yCoord)
        val category = json.getString("category")
        val validCategory = validateCategory(category, validCoord)
        val farmId = validateFarmId(json, validCategory)
        val airflow = validateAirflow(json, validCategory)
        val direction = if (airflow) validateDirection(json, validCoord) else null
        val capacity = validateMoistureCapacity(json, validCategory)
        val shed = if (validateShed(validCategory)) json.getBoolean("shed") else false
        val tile = when (validCategory) {
            TileType.PLANTATION -> {
                val plant = validatePlant(json.getString("plant"), plantMap, yearTick)
                Plantation(tileId, validCoord, direction, farmId ?: 0, validCategory, capacity, plant)
            }
            TileType.FIELD -> {
                val plant = Plant(PlantType.POTATO, Constants.potato, yearTick)
                val possiblePlants = validatePossiblePlant(json.getJSONArray("possiblePlants"))
                Field(tileId, validCoord, direction, farmId ?: 0, validCategory, capacity, plant, possiblePlants)
            }
            else -> {
                Tile(tileId, validCoord, direction, shed, farmId, validCategory)
            }
        }
        return tileId to tile
    }

    private fun validateTileIdAndCoordinate(id: Int, coord: Pair<Int, Int>): Coordinate {
        require(tiles[id] == null) { "TileIDs have to be unique. $id is not unique" }
        require(id >= 0) { "TileIDs have to be greater equal 0. $id is not" }
        val (xCoord: Int, yCoord: Int) = coord
        require(
            xCoord % 2 == 0 && yCoord % 2 == 0 ||
                abs(xCoord % 2) == 1 && abs(yCoord % 2) == 1
        ) { "invalid coordinates" }
        val returnCoord = Coordinate(xCoord, yCoord)
        require(!coordList.contains(returnCoord)) { "Coordinate of tile $id is already used" }
        coordList.add(returnCoord)
        return returnCoord
    }

    private fun validateCategory(category: String, coord: Coordinate): TileType {
        var tileType: TileType = TileType.FARMSTEAD // dummy value
        var errorCatch = true
        when (category) {
            "FARMSTEAD" -> if (abs(coord.x % 2) == 1) tileType = TileType.FARMSTEAD else errorCatch = false
            "FIELD" -> if (coord.x % 2 == 0) tileType = TileType.FIELD else errorCatch = false
            "FOREST" -> tileType = TileType.FOREST
            "MEADOW" -> if (abs(coord.x % 2) == 1) tileType = TileType.MEADOW else errorCatch = false
            "PLANTATION" -> if (coord.x % 2 == 0) tileType = TileType.PLANTATION else errorCatch = false
            "ROAD" -> tileType = TileType.ROAD
            "VILLAGE" -> tileType = TileType.VILLAGE
            else -> require(false) { "category string matches no existing category" }
        }
        require(errorCatch) { "invalid category $tileType on tile with coordinates $coord" }
        return tileType
    }

    private fun validateFarmId(json: JSONObject, category: TileType): Int? {
        var returnValue = when (category) {
            TileType.FARMSTEAD -> 1
            TileType.FIELD -> 1
            TileType.PLANTATION -> 1
            else -> null
        }
        if (returnValue != null) {
            returnValue = json.getInt("farm")
            require(returnValue >= 0) { "FarmIDs have to be greater equal 0. $returnValue is not" }
        }
        return returnValue
    }

    private fun validateAirflow(json: JSONObject, category: TileType): Boolean {
        return if (category == TileType.VILLAGE) {
            false
        } else {
            json.getBoolean("airflow")
        }
    }

    private fun validateDirection(json: JSONObject, coord: Coordinate): Direction {
        var returnDirection: Direction = Direction.NORTH // dummy value
        var errorCatch = true
        val direction = json.getString("direction")
        when (direction) {
            "0" -> if (coord.x % 2 == 0) returnDirection = Direction.NORTH else errorCatch = false
            "45" -> returnDirection = Direction.NORTHEAST
            "90" -> if (coord.x % 2 == 0) returnDirection = Direction.EAST else errorCatch = false
            "135" -> returnDirection = Direction.SOUTHEAST
            "180" -> if (coord.x % 2 == 0) returnDirection = Direction.SOUTH else errorCatch = false
            "225" -> returnDirection = Direction.SOUTHWEST
            "270" -> if (coord.x % 2 == 0) returnDirection = Direction.WEST else errorCatch = false
            "315" -> returnDirection = Direction.NORTHWEST
        }
        require(errorCatch) { "Airflow direction $direction invalid for given tile on $coord" }
        return returnDirection
    }

    private fun validateMoistureCapacity(json: JSONObject, category: TileType): Int {
        if (category == TileType.FIELD || category == TileType.PLANTATION) {
            val capacity = json.getInt("capacity")
            require(capacity > 0) { "Capacity has to be greater 0. $capacity is not" }
            return capacity
        } else {
            return -1
        }
    }

    private fun validatePlant(plant: String, plantData: Map<PlantType, PlantData>, yearTick: Int): Plant {
        var returnPlantType: PlantType = PlantType.POTATO
        var returnPlantData: PlantData? = plantData[PlantType.POTATO]
        when (plant) {
            "APPLE" -> {
                returnPlantType = PlantType.APPLE
                returnPlantData = plantData[PlantType.APPLE]
            }
            "ALMOND" -> {
                returnPlantType = PlantType.ALMOND
                returnPlantData = plantData[PlantType.ALMOND]
            }
            "CHERRY" -> {
                returnPlantType = PlantType.CHERRY
                returnPlantData = plantData[PlantType.CHERRY]
            }
            "GRAPE" -> {
                returnPlantType = PlantType.GRAPE
                returnPlantData = plantData[PlantType.GRAPE]
            }
            else -> {
                require(false) { "Invalid Plant for plantation" }
            }
        }
        return Plant(returnPlantType, returnPlantData ?: Constants.potato, yearTick)
    }

    private fun validatePossiblePlant(json: JSONArray): Set<PlantType> {
        val returnSet: MutableSet<PlantType> = mutableSetOf()
        for (plants in json) {
            when (plants) {
                "POTATO" -> returnSet.add(PlantType.POTATO)
                "WHEAT" -> returnSet.add(PlantType.WHEAT)
                "OAT" -> returnSet.add(PlantType.OAT)
                "PUMPKIN" -> returnSet.add(PlantType.PUMPKIN)
                else -> require(false) { "$plants is not a valid field plant" }
            }
        }
        return returnSet
    }

    private fun validateShed(category: TileType): Boolean {
        return category == TileType.FARMSTEAD
    }

    private fun addTileToMap(idToTiles: Pair<Int, Tile>) {
        val (id, tile) = idToTiles
        tiles[id] = tile
        return
    }
}
