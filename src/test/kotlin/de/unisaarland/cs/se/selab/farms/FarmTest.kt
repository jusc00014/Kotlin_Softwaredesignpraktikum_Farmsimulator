package de.unisaarland.cs.se.selab.farms

import de.unisaarland.cs.se.selab.plants.PlantType
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class FarmTest {
    @Test
    fun removeSowingPlan() {
        val sowingPlan = SowingPlan(
            0,
            0,
            PlantType.POTATO,
            listOf(0, 1)
        )
        val farm = Farm(
            0,
            listOf(0),
            listOf(0, 1),
            emptyList(),
            listOf(0),
            mutableListOf(sowingPlan)
        )
        farm.removeSowingPlan(sowingPlan)
        assertTrue(farm.plans == emptyList<SowingPlan>())
    }
}
