package de.unisaarland.cs.se.selab.board

import de.unisaarland.cs.se.selab.farms.Action
import de.unisaarland.cs.se.selab.plants.Plant

/**
 * plantation class inherits from fertile and overwrites performableActions for plantation plants only and stampede*/
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
        if (drought) return emptyList()
        val actions = mutableListOf<Action>()
        if (irrigatable()) {
            actions.add(Action.IRRIGATING)
        }
        if (plant.harvestable(yearTick)) {
            actions.add(Action.HARVESTING)
        }
        if (plant.cuttable(yearTick)) {
            actions.add(Action.CUTTING)
        }
        if (plant.mowable(yearTick)) {
            actions.add(Action.MOWING)
        }
        return actions
    }
}
