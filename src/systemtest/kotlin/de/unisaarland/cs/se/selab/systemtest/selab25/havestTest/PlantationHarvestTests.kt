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
    scenarioFileName = "scenario${plant.name}.json",
) {
    suspend fun assertTickStart(tick: Int, yearTick: Int) {
        assertNextLine(tickStarted(tick, yearTick))
        skipLines(3) // Skips Soil moisture + Cloud
        assertNextLine(farmStartActions(0))
        assertNextLine(farmSowingPlans(0, emptyList()))
    }

    suspend fun assertHarvest(amount: Int) {
        assertNextLine(machinePerformAction(0, Action.HARVESTING, 1, 1))
        assertNextLine(machineCollectedHarvest(0, amount, plant))
        assertNextLine(machineReturnShed(0, 0))
        assertNextLine(machineUnloaded(0, amount, plant))
    }

    suspend fun assertCutting() {
        assertNextLine(machinePerformAction(1, Action.CUTTING, 1, 1))
        assertNextLine(machineReturnShed(1, 0))
    }

    suspend fun assertMowing() {
        assertNextLine(machinePerformAction(1, Action.MOWING, 1, 1))
        assertNextLine(machineReturnShed(1, 0))
    }

    suspend fun skipInitAndSimStart() =
        skipLines(4)
    suspend fun assertFarmFinished() =
        assertNextLine(farmFinishedActions(0))
    suspend fun assertIncident() =
        assertNextLine(incidentOccured(0, "BROKEN_MACHINE", listOf(0)))
    suspend fun assertHarvestEstimate(amount: Int) =
        assertNextLine(harvestEstimate(1, amount, plant))
    suspend fun assertMissedHarvesting() =
        assertNextLine(actionNotPerformed(1, listOf(Action.HARVESTING)))
    suspend fun assertSimEnd() =
        assertNextLine(simulationEnd(maxTicks))
}

/**
 * Apple Late
 */
class AppleLateHarvestTest : PlantationLateHarvestTestExtension(
    PlantType.APPLE,
    Constants.AUG_2,
    5,
) {
    override suspend fun run() {
        // Apple max Sunlight: 50h
        // Sunlight reduction Cloud: -50h
        skipInitAndSimStart()
        assertTickStart(0, startYearTick) // (154h/104h)
        assertFarmFinished()
        assertIncident()
        // Harvest Penalty: 2x 0.9
        // 1_530_000
        assertHarvestEstimate(1_377_000)

        assertTickStart(1, Constants.SEP_1) // First Harvest Tick (126h/76h)
        assertMowing()
        assertFarmFinished()
        // Harvest Penalty: 1x 0.9
        assertHarvestEstimate(1_239_300)

        assertTickStart(2, Constants.SEP_2) // Second Harvest Tick (126h/76h)
        assertFarmFinished()
        // Harvest Penalty: 1x 0.9
        assertHarvestEstimate(1_115_370)

        assertTickStart(3, Constants.OCT_1) // Last Harvest Tick (112h/62h)
        assertFarmFinished()
        assertMissedHarvesting()
        // Harvest Penalty: 0.5 Late
        assertHarvestEstimate(557_685)

        assertTickStart(4, Constants.OCT_2) // Late Harvest Penalty Tick (112h/62h)
        assertHarvest(557_685)
        assertFarmFinished()
        assertSimEnd()
    }
}

/**
 * Almond Late
 * Log harvest Estimate change in tick 5 (last tick to harvest)
 * Log harvest in tick 5
 */
class AlmondLateHarvestTest : PlantationLateHarvestTestExtension(
    PlantType.ALMOND,
    Constants.AUG_1,
    6,
) {
    override suspend fun run() {
        skipInitAndSimStart()
        assertTickStart(0, startYearTick)
        assertFarmFinished()
        assertIncident()

        assertTickStart(1, Constants.AUG_2) // First Harvest Tick
        assertFarmFinished()

        assertTickStart(2, Constants.SEP_1) // Second Harvest Tick
        assertMowing()
        assertFarmFinished()

        assertTickStart(3, Constants.SEP_2) // Third Harvest Tick
        assertFarmFinished()

        assertTickStart(4, Constants.OCT_1) // Last Harvest Tick
        assertFarmFinished()
        assertMissedHarvesting()
        assertHarvestEstimate(720_000)

        assertTickStart(5, Constants.OCT_2) // Late Harvest Penalty Tick
        assertHarvest(720_000)
        assertFarmFinished()
        assertSimEnd()
    }
}

/**
 * Cherry late
 * Log harvest Estimate change in tick 2 (last tick to harvest)
 * Log harvest in tick 3
 */
class CherryLateHarvestTest : PlantationLateHarvestTestExtension(
    PlantType.CHERRY,
    Constants.JUN_2,
    4,
) {
    override suspend fun run() {
        skipInitAndSimStart()
        assertTickStart(0, startYearTick)
        assertFarmFinished()
        assertIncident()

        assertTickStart(1, Constants.JUL_1) // First Harvest Tick
        assertFarmFinished()

        assertTickStart(2, Constants.JUL_2) // Last Harvest Tick
        assertFarmFinished()
        assertMissedHarvesting()
        assertHarvestEstimate(360_000)

        assertTickStart(3, Constants.AUG_1) // Late Harvest Penalty Tick
        assertHarvest(360_000)
        assertFarmFinished()
        assertSimEnd()
    }
}

/**
 * GRAPE late
 * Log harvest Estimate change in tick 2 & 3 (last tick to harvest)
 * Log harvest in tick 4
 */
class GrapeLateHarvestTest : PlantationLateHarvestTestExtension(
    PlantType.GRAPE,
    Constants.AUG_2,
    5,
) {
    override suspend fun run() {
        skipInitAndSimStart()
        assertTickStart(0, Constants.AUG_2)
        assertCutting()
        assertFarmFinished()
        assertIncident()

        assertTickStart(1, Constants.SEP_1) // Only Harvest Tick
        assertFarmFinished()
        assertMissedHarvesting()
        assertHarvestEstimate(1_140_000)

        assertTickStart(2, Constants.SEP_2) // First Harvest Penalty Tick
        assertFarmFinished()
        assertMissedHarvesting()
        assertHarvestEstimate(1_083_000)

        assertTickStart(3, Constants.OCT_1) // Second Harvest Penalty Tick
        assertFarmFinished()
        assertMissedHarvesting()
        assertHarvestEstimate(1_028_850)

        assertTickStart(4, Constants.OCT_2) // Third Harvest Penalty Tick
        assertHarvest(1_028_850)
        assertFarmFinished()
        assertSimEnd()
    }
}
