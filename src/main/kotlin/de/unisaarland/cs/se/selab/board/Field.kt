package de.unisaarland.cs.se.selab.board

import de.unisaarland.cs.se.selab.farms.Action
import de.unisaarland.cs.se.selab.plants.Plant
import de.unisaarland.cs.se.selab.plants.PlantData
import de.unisaarland.cs.se.selab.plants.PlantType

/**
 * Field class for Field exclusive logic and functions*/
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
        val actions = mutableListOf<Action>()
        if (irrigatable()) {
            actions.add(Action.IRRIGATING)
        }
        if (sowable(yearTick, plant.type)) {
            actions.add(Action.SOWING)
        }
        if (plant.weedable(yearTick)) {
            actions.add(Action.WEEDING)
        }
        if (plant.harvestable(yearTick)) {
            actions.add(Action.HARVESTING)
        }
        return actions
    }

    /**
     * returns plantTypes for sowable Plants by checking plantData*/
    fun sowablePlants(yearTick: Int, plantData: Map<PlantType, PlantData>): List<PlantType> {
        val pt = mutableListOf<PlantType>()
        for (plantType in possiblePlants) {
            val pd = plantData[plantType] ?: continue
            if (yearTick in pd.sowRange) {
                pt.add(plantType)
            }
        }
        return pt
    }

    /**
     * checks if the plantType is in possiblePlants and if it is not sown and not fallow*/
    fun sowable(yearTick: Int, plantType: PlantType): Boolean {
        return !plant.isSown() && !plant.isFallow(yearTick) && plantType in possiblePlants
    }
}