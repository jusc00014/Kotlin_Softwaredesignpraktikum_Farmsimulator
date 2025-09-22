package de.unisaarland.cs.se.selab.farms
// import plants.PlantType

/**
 * Farm*/
class Farm(
    val id: Int,
    val farmsteads: List<Int>,
    val fields: List<Int>,
    val plantages: List<Int>,
    val machines: List<Int>,
    var plans: MutableList<SowingPlan>
) {
    /**
     * If a sowing plan was executed at least once*/
    fun removeSowingPlan(plan: SowingPlan) {
        plans.remove(plan)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is Farm) {
            val bool1 = this.id == other.id && this.farmsteads == other.farmsteads && this.fields == other.fields
            val bool2 = this.plantages == other.plantages && this.machines == other.machines
            return bool1 && bool2 && this.plans == other.plans
        } else {
            return false
        }
    }
}
