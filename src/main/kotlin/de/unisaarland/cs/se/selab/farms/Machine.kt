package de.unisaarland.cs.se.selab.farms
import de.unisaarland.cs.se.selab.plants.PlantType

class Machine(
    val id: Int,
    val actions: List<Action>,
    val plants: List<PlantType>,
    val duration: Int,
    var location: Int,
    var brokenFor: Int = 0,
    var stuck: Boolean = false
) {
    fun setStuck() {
        stuck = true
    }
}
