package de.unisaarland.cs.se.selab.parser

import com.github.erosb.jsonsKema.FormatValidationPolicy
import com.github.erosb.jsonsKema.JsonParser
import com.github.erosb.jsonsKema.SchemaLoader
import com.github.erosb.jsonsKema.Validator
import com.github.erosb.jsonsKema.ValidatorConfig
import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.Coordinate
import de.unisaarland.cs.se.selab.board.Direction
import de.unisaarland.cs.se.selab.board.Field
import de.unisaarland.cs.se.selab.board.Plantation
import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.board.TileType
import de.unisaarland.cs.se.selab.plants.Plant
import de.unisaarland.cs.se.selab.plants.PlantData
import de.unisaarland.cs.se.selab.plants.PlantTile
import de.unisaarland.cs.se.selab.plants.PlantType
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

/**
 * Parses the Map Data into a Map of Ids and Tiles
 */
class MapParser(
    private var tiles: MutableMap<Int, Tile>,
) {
    /**
     * Constants because we love magic numbers
     */
    companion object Constants {

        const val POTATO_MOISTURE = 500
        const val WHEAT_MOISTURE = 450
        const val OAT_MOISTURE = 300
        const val PUMPKIN_MOISTURE = 600
        const val APPLE_MOISTURE = 100
        const val ALMOND_MOISTURE = 400
        const val CHERRY_MOISTURE = 150
        const val GRAPE_MOISTURE = 250

        const val POTATO_SUNLIGHT = 130
        const val WHEAT_SUNLIGHT = 90
        const val OAT_SUNLIGHT = 90
        const val PUMPKIN_SUNLIGHT = 120
        const val APPLE_SUNLIGHT = 50
        const val ALMOND_SUNLIGHT = 130
        const val CHERRY_SUNLIGHT = 120
        const val GRAPE_SUNLIGHT = 150

        const val POTATO_HE = 1_000_000
        const val WHEAT_HE = 1_500_000
        const val OAT_HE = 1_200_000
        const val PUMPKIN_HE = 500_000
        const val APPLE_HE = 1_700_000
        const val ALMOND_HE = 800_000
        const val CHERRY_HE = 1_200_000
        const val GRAPE_HE = 1_200_000

        const val JAN_1 = 1
        const val JAN_2 = 2
        const val FEB_1 = 3
        const val FEB_2 = 4
        const val MAR_1 = 5
        const val MAR_2 = 6
        const val APR_1 = 7
        const val APR_2 = 8
        const val MAY_1 = 9
        const val MAY_2 = 10
        const val JUN_1 = 11
        const val JUN_2 = 12
        const val JUL_1 = 13
        const val JUL_2 = 14
        const val AUG_1 = 15
        const val AUG_2 = 16
        const val SEP_1 = 17
        const val SEP_2 = 18
        const val OCT_1 = 19
        const val OCT_2 = 20
        const val NOV_1 = 21
        const val NOV_2 = 22
        const val DEC_1 = 23
        const val DEC_2 = 24
    }
    val potato = PlantData(
        POTATO_MOISTURE,
        POTATO_SUNLIGHT,
        POTATO_HE,
        3..3,
        true,
        SEP_1..OCT_2,
        0,
        APR_1..MAY_2,
        (JAN_2..DEC_2 step 2).toList(),
        emptyList(),
        emptyList(),
        PlantTile.FIELD
    )
    val wheat = PlantData(
        WHEAT_MOISTURE,
        WHEAT_SUNLIGHT,
        WHEAT_HE,
        MAY_1..MAY_1,
        false,
        JUN_1..JUL_1,
        2,
        OCT_1..OCT_2,
        listOf(FEB_1, MAY_1),
        emptyList(),
        emptyList(),
        PlantTile.FIELD
    )
    val oat = PlantData(
        OAT_MOISTURE,
        OAT_SUNLIGHT,
        OAT_HE,
        0..0,
        false,
        JUL_1..AUG_2,
        2,
        MAR_2..MAR_2,
        listOf(1, 2, 3),
        emptyList(),
        emptyList(),
        PlantTile.FIELD
    )
    val pumpkin = PlantData(
        PUMPKIN_MOISTURE,
        PUMPKIN_SUNLIGHT,
        PUMPKIN_HE,
        2..3,
        true,
        SEP_1..OCT_2,
        0,
        MAY_1..JUN_2,
        (JAN_2..DEC_2 step 2).toList(),
        emptyList(),
        emptyList(),
        PlantTile.FIELD
    )
    val apple = PlantData(
        APPLE_MOISTURE,
        APPLE_SUNLIGHT,
        APPLE_HE,
        APR_2..MAY_1,
        true,
        SEP_1..OCT_1,
        1,
        0..0,
        emptyList(),
        listOf(NOV_1, NOV_2, FEB_1, FEB_2),
        listOf(JUN_1, SEP_1),
        PlantTile.PLANTATION
    )
    val almond = PlantData(
        ALMOND_MOISTURE,
        ALMOND_SUNLIGHT,
        ALMOND_HE,
        FEB_2..MAR_1,
        true,
        AUG_2..OCT_1,
        1,
        0..0,
        emptyList(),
        listOf(NOV_1, NOV_2, FEB_1, FEB_2),
        listOf(JUN_1, SEP_1),
        PlantTile.PLANTATION
    )
    val cherry = PlantData(
        CHERRY_MOISTURE,
        CHERRY_SUNLIGHT,
        CHERRY_HE,
        APR_2..MAY_1,
        true,
        JUL_1..JUL_2,
        1,
        0..0,
        emptyList(),
        listOf(NOV_1, NOV_2, FEB_1, FEB_2),
        listOf(JUN_1),
        PlantTile.PLANTATION
    )
    val grape = PlantData(
        GRAPE_MOISTURE,
        GRAPE_SUNLIGHT,
        GRAPE_HE,
        JUN_2..AUG_2,
        false,
        SEP_1..SEP_1,
        3,
        0..0,
        emptyList(),
        listOf(JUL_2, AUG_1, AUG_2),
        listOf(APR_1, JUL_1),
        PlantTile.PLANTATION
    )
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
                    require(
                        boardData.neighbors(1, it.value).none
                            { itt -> itt.type == TileType.MEADOW || itt.type == TileType.FARMSTEAD }
                    )
                }
                TileType.MEADOW -> {
                    require(boardData.neighbors(1, it.value).none { itt -> itt.type == TileType.MEADOW })
                }
                TileType.VILLAGE -> {
                    require(boardData.neighbors(1, it.value).none { itt -> itt.type == TileType.FOREST })
                }
                else -> {}
            }
        }
        return boardData to plantMap
    }
    private fun createPlantData(plantType: PlantType): PlantData {
        return when (plantType) {
            PlantType.POTATO -> potato
            PlantType.WHEAT -> wheat
            PlantType.OAT -> oat
            PlantType.PUMPKIN -> pumpkin
            PlantType.APPLE -> apple
            PlantType.ALMOND -> almond
            PlantType.CHERRY -> cherry
            PlantType.GRAPE -> grape
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
                val plant = Plant(PlantType.POTATO, potato, yearTick)
                val possiblePlants = validatePossiblePlant(json.getJSONArray("possiblePlants"))
                Field(tileId, validCoord, direction, farmId ?: 0, validCategory, capacity, plant, possiblePlants)
            }
            TileType.VILLAGE -> {
                Tile(tileId, validCoord, null, shed, null, validCategory)
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
                setOf(-1, 1).contains(xCoord % 2) && setOf(-1, 1).contains(yCoord % 2)
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
            "FARMSTEAD" -> if (coord.x % 2 == 1) tileType = TileType.FARMSTEAD else errorCatch = false
            "FIELD" -> if (coord.x % 2 == 0) tileType = TileType.FIELD else errorCatch = false
            "FOREST" -> tileType = TileType.FOREST
            "MEADOW" -> if (coord.x % 2 == 1) tileType = TileType.MEADOW else errorCatch = false
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
            assert(returnValue >= 0)
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
            assert(capacity > 0)
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
        return Plant(returnPlantType, returnPlantData ?: potato, yearTick)
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
