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
}
