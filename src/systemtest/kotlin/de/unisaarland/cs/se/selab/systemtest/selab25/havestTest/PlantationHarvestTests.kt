package de.unisaarland.cs.se.selab.systemtest.selab25.havestTest

import de.unisaarland.cs.se.selab.Constants
import de.unisaarland.cs.se.selab.farms.Action
import de.unisaarland.cs.se.selab.plants.PlantType
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.SimulationTestExtension

/**
 * Base Class for every PlantationHarvestTest
 */
abstract class PlantationLateHarvestTestExtension(
    val plant: PlantType,
    override val startYearTick: Int,
    override val maxTicks: Int,
    override val description: String = "Test late harvest for $plant"
) : SimulationTestExtension(
    folder = "plantationHarvestTest",
    mapFileName = "map${plant.name}.json",
) {
    suspend fun assertHarvest(amount: Int?) {
        skipUntilString(farmStartActions(0))
        assertNextLine(farmSowingPlans(0, emptyList()))
        if (amount != null) {
            assertNextLine(machinePerformAction(0, Action.HARVESTING, 1, 1))
            assertNextLine(machineCollectedHarvest(0, amount, plant))
            assertNextLine(machineReturnShed(0, 0))
            assertNextLine(machineUnloaded(0, amount, plant))
        }
        assertNextLine(farmFinishedActions(0))
    }

    suspend fun tick0() {
        skipUntilString(tickStarted(0, startYearTick))
        assertNextLine(soilMoisture(0, 0))
        assertHarvest(null)
        assertNextLine(incidentOccured(0, "BROKEN_MACHINE", listOf(0)))
    }

    open suspend fun assertTickHarvest(tick: Int, yearTick: Int, amount: Int) {
        skipUntilString(tickStarted(tick, yearTick))
        assertNextLine(soilMoisture(0, 0))
        assertHarvest(amount)
    }
}

/**
 * GRAPE late
 * Log harvest Estimate change in tick 2 (last tick to harvest)
 */
class CherryLateHarvestTest3T : PlantationLateHarvestTestExtension(
    PlantType.CHERRY,
    Constants.JUN_2,
    3,
) {
    override suspend fun run() {
        tick0()
        assertNextLine(tickStarted(1, Constants.JUL_1))
        assertNextLine(soilMoisture(0, 0))
        assertHarvest(null)
        assertNextLine(tickStarted(2, Constants.JUL_2))
        assertHarvest(null)
        assertNextLine(actionNotPerformed(1, listOf(Action.HARVESTING)))
        assertNextLine(harvestEstimate(1, 360_000, plant))
    }
}

/**
 * GRAPE late
 * Log harvest Estimate change in tick 3 (first penalty tick)
 */
class CherryLateHarvestTest4T : PlantationLateHarvestTestExtension(
    PlantType.CHERRY,
    Constants.JUN_2,
    4,
) {
    override suspend fun run() {
        tick0()
        assertNextLine(tickStarted(1, Constants.JUL_1))
        assertNextLine(soilMoisture(0, 0))
        assertHarvest(null)
        assertNextLine(tickStarted(2, Constants.JUL_2))
        assertHarvest(null)
        assertNextLine(actionNotPerformed(1, listOf(Action.HARVESTING)))
        assertNextLine(tickStarted(2, Constants.AUG_1))
        assertNextLine(soilMoisture(0, 0))
        assertHarvest(null)
        assertNextLine(harvestEstimate(1, 360_000, plant))
    }
}
