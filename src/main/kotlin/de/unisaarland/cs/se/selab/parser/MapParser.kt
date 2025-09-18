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
        TODO()
    }

    private fun validateCategory(category: String, coord: Coordinate): TileType {
        TODO()
    }

    private fun validateFarmId(farmId: Int, category: TileType): Int {
        TODO()
    }

    private fun validateAirflow(hasAirflow: Boolean, category: TileType): Boolean {
        TODO()
    }

    private fun validateDirection(direction: Int, coord: Coordinate): Direction {
        TODO()
    }

    private fun validateMoistureCapacity(capacity: Int, category: TileType): Int {
        TODO()
    }

    private fun validatePlant(category: TileType, plantData: PlantData): Plant {
        TODO()
    }

    private fun validatePossiblePlant(category: TileType, plantData: PlantData): Plant {
        TODO()
    }

    private fun validateShed(category: TileType): Boolean {
        TODO()
    }

    private fun addTileToMap (idToTiles: MutableMap<Int, Tile>): Unit {
        TODO()
    }
}