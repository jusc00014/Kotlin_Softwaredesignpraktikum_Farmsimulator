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
            addTileToMap(id to validTile)
        }
        val boardData = BoardData(tiles)
        tiles.forEach {
            when (it.value.type) {
                TileType.FARMSTEAD -> {
                    val farmId = it.value.farmID
                    val neighborOfFarm = boardData.neighbors(1, it.value, excludeSelf = true)
                    require(
                        neighborOfFarm.none
                        { itt -> itt.type == TileType.MEADOW || itt.type == TileType.FARMSTEAD }
                    )
                    require(
                        neighborOfFarm.none
                        { itt ->
                            if (itt.type == TileType.FIELD || itt.type == TileType.PLANTATION) {
                                itt.farmID != farmId
                            } else {
                                false
                            }
                        }
                    )
                }
                TileType.MEADOW -> {
                    require(
                        boardData.neighbors(1, it.value, excludeSelf = true).none { itt -> itt.type == TileType.MEADOW }
                    )
                }
                TileType.VILLAGE -> {
                    require(
                        boardData.neighbors(1, it.value, excludeSelf = true).none { itt -> itt.type == TileType.FOREST }
                    )
                }
                else -> {}
            }
        }
        return boardData to plantMap
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
        require(tiles[id] == null)
        require(id >= 0)
        val (xCoord: Int, yCoord: Int) = coord
        require(
            xCoord % 2 == 0 && yCoord % 2 == 0 ||
                abs(xCoord % 2) == 1 && abs(yCoord % 2) == 1
        ) { "invalid coordinates" }
        val returnCoord = Coordinate(xCoord, yCoord)
        require(!coordList.contains(returnCoord))
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
            require(returnValue >= 0)
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
            require(capacity > 0)
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
                returnPlantType = PlantType.CHERRY
                returnPlantData = plantData[PlantType.CHERRY]
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
