package de.unisaarland.cs.se.selab.parser

import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.Coordinate
import de.unisaarland.cs.se.selab.board.TileType
import de.unisaarland.cs.se.selab.board.Direction
import de.unisaarland.cs.se.selab.plants.Plant
import de.unisaarland.cs.se.selab.plants.PlantData

class MapParser (
    private var tiles: MutableMap<Int, Tile>
) {
    fun parse (jsonFile: String): BoardData {
        TODO()
    }

    private fun validateTileIdAndCoordinate(id: Int, coord: Pair<Int, Int>): Coordinate {
        assert(tiles[id] == null)
        val (xCoord: Int, yCoord: Int) = coord
        if (xCoord % 2 == 0 && yCoord % 2 == 0)
            return Coordinate(xCoord, yCoord)
        if (xCoord % 2 == 1 && yCoord % 2 == 1)
            return Coordinate(xCoord, yCoord)
        throw IllegalArgumentException("Coordinate does not exist")
    }

    private fun validateCategory(category: String, coord: Coordinate): TileType {
        var tileType: TileType = TileType.FARMSTEAD // dummy value
        var errorCatch: Boolean = false
        when (category) {
            "FARMSTEAD" -> if (coord.x % 2 == 1) tileType = TileType.FARMSTEAD else errorCatch = true
            "FIELD" -> if (coord.x % 2 == 0) tileType = TileType.FIELD else errorCatch = true
            "FOREST" -> tileType = TileType.FOREST
            "MEADOW" -> if (coord.x % 2 == 1) tileType = TileType.MEADOW else errorCatch = true
            "PLANTATION" -> if (coord.x % 2 == 0) tileType = TileType.PLANTATION else errorCatch = true
            "ROAD" -> tileType = TileType.ROAD
            "VILLAGE" -> tileType = TileType.VILLAGE
            else -> throw IllegalArgumentException("String doesn't match any existing category")
        }
        if (errorCatch) throw IllegalArgumentException("Coordinate does not match valid tile for $category")
        return tileType
    }

    private fun validateFarmId(farmId: Int, category: TileType): Int {
        assert(farmId >= 0)
        when (category) {
            TileType.FARMSTEAD -> ""
            TileType.FIELD -> ""
            TileType.PLANTATION -> ""
            else -> throw IllegalArgumentException("$category can't have a farmId")
        }
        return farmId
    }

    private fun validateAirflow(hasAirflow: Boolean, category: TileType): Boolean {
        if (category == TileType.VILLAGE && hasAirflow)
            throw IllegalArgumentException("Villages have no airflow")
        else return hasAirflow
    }

    private fun validateDirection(direction: Int, coord: Coordinate): Direction {
        var returnDirection: Direction = Direction.NORTH // dummy value
        var errorCatch: Boolean = false
        when (direction) {
            0 -> if (coord.x % 2 == 0) returnDirection = Direction.NORTH else errorCatch = true
            45 -> returnDirection = Direction.NORTHEAST
            90 -> if (coord.x % 2 == 0) returnDirection = Direction.EAST else errorCatch = true
            135 -> returnDirection = Direction.SOUTHEAST
            180 -> if (coord.x % 2 == 0) returnDirection = Direction.SOUTH else errorCatch = true
            225 -> returnDirection = Direction.SOUTHWEST
            270 -> if (coord.x % 2 == 0) returnDirection = Direction.WEST else errorCatch = true
            315 -> returnDirection = Direction.NORTHWEST
        }
        if (errorCatch) throw IllegalArgumentException("Invalid airflow on a square field")
        return returnDirection
    }

    private fun validateMoistureCapacity(capacity: Int, category: TileType): Int {
        if ((category == TileType.FIELD || category == TileType.PLANTATION) && capacity > 0)
            return capacity
        else throw IllegalArgumentException("Moisture Capacity only exists for fields and plantations, given field was $category")
    }

    private fun validatePlant(category: TileType, plantData: PlantData): Plant {
        TODO()
    }

    private fun validatePossiblePlant(category: TileType, plantData: PlantData): Plant {
        TODO()
    }

    private fun validateShed(category: TileType): Boolean {
        return (category == TileType.FARMSTEAD)
    }

    private fun addTileToMap (idToTiles: Pair<Int, Tile>): Unit {
        val (id, tile) = idToTiles
        tiles[id] = tile
        return
    }
}