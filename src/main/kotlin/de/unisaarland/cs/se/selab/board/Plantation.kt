package de.unisaarland.cs.se.selab.board

import de.unisaarland.cs.se.selab.farms.Action
import de.unisaarland.cs.se.selab.plants.Plant

class Plantation(
    id: Int,
    coord: Coordinate,
    airflow: Direction?,
    farmID: Int,
    type: TileType,
    moistureCapacity: Int,
    plant: Plant
) : Fertile(id, coord, airflow, farmID, type, moistureCapacity, plant) {
    override fun performableActions(yearTick: Int): List<Action> {
        TODO("Not yet implemented")
    }

    override fun stampede(): Boolean {
        TODO("Not yet implemented")
    }
}