package de.unisaarland.cs.se.selab.systemtest.selab25.everythingTests

import de.unisaarland.cs.se.selab.farms.Action
import de.unisaarland.cs.se.selab.plants.PlantType
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.SimulationTestExtension

const val CLOUD_MOVES_TO_98 = "[INFO] Cloud Movement: Cloud 1 with 4000 L water moved from tile 99 to tile 98."
const val CLOUD_MOVES_TO_99 = "[INFO] Cloud Movement: Cloud 1 with 4000 L water moved from tile 98 to tile 99."
const val SUNLIGHT_REDUCTION_99 = "[DEBUG] Cloud Movement: On tile 99, the amount of sunlight is "
const val SUNLIGHT_REDUCTION_98 = "[DEBUG] Cloud Movement: On tile 98, the amount of sunlight is "

/**
 * tests a lot of behavior in October1 (all phases) */
class BigBigTestOctober1 : SimulationTestExtension(
    "bigBigTest",
    "bigBigTestMap.json",
    "bigBigTestFarms.json",
    "bigBigTestScenarioOctober1.json"
) {
    override val name = "BigBigTestOctober1"
    override val description = "tests all phases for tick 19"

    override val map = "bigBigTest/bigBigTestMap.json"
    override val farms = "bigBigTest/bigBigTestFarms.json"
    override val scenario = "bigBigTest/bigBigTestScenarioOctober1.json"
    override val startYearTick = 19
    override val maxTicks = 1
    override val logLevel = "DEBUG"

    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, "Simulation Info")
        assertCurrentLine("[INFO] Simulation Info: Simulation started at tick 19 within the year.")
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 19 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")
        for (i in 109 downTo 97 step 3) {
            assertNextLine(CLOUD_MOVES_TO_98)
            assertNextLine(SUNLIGHT_REDUCTION_99 + "$i.")
            assertNextLine(CLOUD_MOVES_TO_99)
            assertNextLine(SUNLIGHT_REDUCTION_98 + "$i.")
        }
        assertNextLine("[DEBUG] Cloud Position: Cloud 1 is on tile 99, where the amount of sunlight is 47.")
        assertNextLine(farmStartActions(1))
        assertNextLine(farmSowingPlans(1, listOf(1)))
        assertNextLine(machinePerformAction(0, Action.SOWING, 0, 4))
        assertNextLine(machineSowed(0, PlantType.WHEAT, 1))
        assertNextLine(machinePerformAction(0, Action.SOWING, 1, 4))
        assertNextLine(machineSowed(0, PlantType.WHEAT, 1))
        assertNextLine(machinePerformAction(0, Action.SOWING, 3, 4))
        assertNextLine(machineSowed(0, PlantType.WHEAT, 1))
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 4.")
        assertNextLine(machinePerformAction(1, Action.SOWING, 8, 7))
        assertNextLine(machineSowed(1, PlantType.WHEAT, 1))
        assertNextLine(machinePerformAction(1, Action.SOWING, 10, 7))
        assertNextLine(machineSowed(1, PlantType.WHEAT, 1))
        assertNextLine("[IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 6.")
        assertNextLine("[IMPORTANT] Farm: Farm 1 finished its actions.")
        assertNextLine(farmStartActions(2))
        assertNextLine(farmSowingPlans(2, emptyList()))
        assertNextLine(farmFinishedActions(2))

        assertNextLine(harvestEstimate(0, 1_500_000, PlantType.WHEAT))
        assertNextLine(harvestEstimate(1, 1_500_000, PlantType.WHEAT))
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 2 were not performed: HARVESTING.")
        assertNextLine(harvestEstimate(2, 1_083_000, PlantType.GRAPE))
        assertNextLine(harvestEstimate(3, 1_500_000, PlantType.WHEAT))
        assertNextLine(harvestEstimate(8, 1_500_000, PlantType.WHEAT))
        assertNextLine(harvestEstimate(10, 1_500_000, PlantType.WHEAT))
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 99 were not performed: HARVESTING.")
        assertNextLine(harvestEstimate(99, 1_083_000, PlantType.GRAPE))
    }
}
