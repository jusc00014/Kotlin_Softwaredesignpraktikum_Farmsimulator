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

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is SowingPlan) {
            return this.id == other.id &&
                this.tick == other.tick &&
                this.plant == other.plant &&
                this.fields == other.fields
        } else {
            return false
        }
    }
}
