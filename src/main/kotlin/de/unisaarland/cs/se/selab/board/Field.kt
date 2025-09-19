package de.unisaarland.cs.se.selab.board

import de.unisaarland.cs.se.selab.farms.Action
import de.unisaarland.cs.se.selab.plants.Plant
import de.unisaarland.cs.se.selab.plants.PlantData
import de.unisaarland.cs.se.selab.plants.PlantType

class Field(
    id: Int,
    coord: Coordinate,
    airflow: Direction?,
    farmID: Int,
    type: TileType,
    moistureCapacity: Int,
    plant: Plant,
    private val possiblePlants: Set<PlantType>
) : Fertile(id, coord, airflow, farmID, type, moistureCapacity, plant) {
    override fun performableActions(yearTick: Int): List<Action> {
        TODO("Not yet implemented")
    }

    override fun stampede(): Boolean {
        TODO("Not yet implemented")
    }

    fun sowablePlants(yearTick: Int, plantData: Map<PlantType, PlantData>): List<PlantType> {
        TODO()
    }

    fun sowable(yearTick: Int, plantType: PlantType): Boolean {
        TODO()
    }
}