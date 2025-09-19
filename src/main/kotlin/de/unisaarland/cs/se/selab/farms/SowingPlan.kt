package de.unisaarland.cs.se.selab.farms
import de.unisaarland.cs.se.selab.plants.PlantType

/**
 * SowingPlan class stored in Farms*/
class SowingPlan(
    val id: Int,
    val tick: Int,
    val plant: PlantType,
    val fields: List<Int>
) {
    /**
     * checks if sowingPlan is active by comparing ticks.
     * True if current tick larger or equal start tick of plan*/
    fun isActive(currentTick: Int): Boolean {
        return currentTick >= this.tick
    }

   /**
    * returns the ids of tiles in sowingPlans*/
    fun getFields(): List<Int> {
        return fields
    }

    /**
     * returns plantType of sowingPlan*/
    fun getPlant(): PlantType {
        return plant
    }
}
