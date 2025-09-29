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
    val possiblePlants: Set<PlantType>
) : Fertile(id, coord, airflow, farmID, type, moistureCapacity, plant) {
    /**
     * Constants used in Field
     */
    companion object {
        const val SOWING_LATE_TIMEFRAME = 2
    }

    override fun performableActions(yearTick: Int): List<Action> {
        if (drought) return emptyList()
        val actions = mutableListOf<Action>()
        if (irrigatable()) {
            actions.add(Action.IRRIGATING)
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
        if (!sowable(yearTick)) return emptyList()
        val pt = mutableListOf<PlantType>()
        for (plantType in possiblePlants) {
            val pd = plantData[plantType] ?: continue
            val extendedSowRange = IntRange(
                pd.sowRange.start,
                pd.sowRange.endInclusive + SOWING_LATE_TIMEFRAME
            )
            if (extendedSowRange.contains(yearTick)) {
                pt.add(plantType)
            }
        }
        return pt
    }

    /**
     * checks if the plantType is in possiblePlants and if it is not sown and not fallow*/
    fun sowable(yearTick: Int): Boolean {
        return !plant.isSown() && !plant.isFallow(yearTick)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is Field) {
            return this.possiblePlants == other.possiblePlants && super.equals(other)
        } else {
            return false
        }
    }
}
