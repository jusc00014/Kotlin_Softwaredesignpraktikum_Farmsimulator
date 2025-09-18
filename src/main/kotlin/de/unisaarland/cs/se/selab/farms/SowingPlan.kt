package farms
import plants.PlantType

class SowingPlan(
    val id: Int,
    val tick: Int,
    val plant: PlantType,
    val fields: List<Int>
) {
    fun isActive(currentTick: Int): Boolean {
        return tick == currentTick
    }
}
