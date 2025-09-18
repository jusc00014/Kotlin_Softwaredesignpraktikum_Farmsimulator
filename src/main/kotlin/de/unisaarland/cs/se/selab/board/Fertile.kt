package board
import de.unisaarland.cs.se.selab.plants.Plant
import plants.PlantData
import plants.PlantType

abstract class Fertile(
    val tile: Tile,
    var sunhours: Int,
    val moistureCapacity: Int,
    val moisture: Int,
    var drought: Boolean,
    var plant: Plant
) {
    fun resetForNextTick() {
        TODO()
    }
    fun sowablePlants(yearTick: Int, plantData: Map<PlantType, PlantData>): List<PlantType> {
        TODO()
    }
}
