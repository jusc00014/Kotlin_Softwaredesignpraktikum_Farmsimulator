package de.unisaarland.cs.se.selab.farms
import de.unisaarland.cs.se.selab.plants.PlantType

class SowingPlan(
    val id: Int,
    val tick: Int,
    val plant: PlantType,
    val fields: List<Int>
) {
    fun isActive(currentTick: Int): Boolean {
        return currentTick >= this.tick
    }

    fun getFields(): List<Int> {
        return fields
    }

    fun getPlant(): PlantType {
        return plant
    }
}
