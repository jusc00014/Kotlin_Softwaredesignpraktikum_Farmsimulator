package de.unisaarland.cs.se.selab.farms
//import plants.PlantType


class Farm(
    val id: Int,
    val farmsteads: List<Int>,
    val fields: List<Int>,
    val plantages: List<Int>,
    val machines: List<Int>,
    var plans: MutableList<SowingPlan>
) {
    fun removeSowingPlan(plan: SowingPlan) {
        plans.remove(plan)
    }
}
