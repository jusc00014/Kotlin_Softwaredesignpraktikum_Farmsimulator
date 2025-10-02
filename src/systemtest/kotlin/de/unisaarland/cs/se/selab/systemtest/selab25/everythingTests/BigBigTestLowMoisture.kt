package de.unisaarland.cs.se.selab.systemtest.selab25.everythingTests

import de.unisaarland.cs.se.selab.farms.Action
import de.unisaarland.cs.se.selab.plants.PlantType
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.SimulationTestExtension

/**
 * same as bigBigTestOctober1, but with low moisture stuff*/
class BigBigTestLowMoisture : SimulationTestExtension(
    "bigBigTest",
    "bigBigTestLowMoistureMap.json",
    "bigBigTestLowMoistureFarm.json",
    "bigBigTestScenarioOctober1.json"
) {
    override val description = "tests all phases for tick 19"

    override val startYearTick = 19
    override val maxTicks = 2
    override val logLevel = "DEBUG"

    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, "Simulation Info")
        assertCurrentLine("[INFO] Simulation Info: Simulation started at tick 19 within the year.")
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 19 within the year.")
        assertNextLine(
            "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 1 PLANTATION tiles."
        )
        for (i in 109 downTo 97 step 3) {
            assertNextLine(CLOUD_MOVES_TO_98)
            assertNextLine(SUNLIGHT_REDUCTION_99 + "$i.")
            assertNextLine(CLOUD_MOVES_TO_99)
            assertNextLine(SUNLIGHT_REDUCTION_98 + "$i.")
        }
        assertNextLine("[DEBUG] Cloud Position: Cloud 1 is on tile 99, where the amount of sunlight is 47.")
        assertNextLine(farmStartActions(1))
        assertNextLine(farmSowingPlans(1, listOf(1)))
        assertNextLine(machinePerformAction(0, Action.SOWING, 0, 3))
        assertNextLine(machineSowed(0, PlantType.WHEAT, 1))
        assertNextLine(machinePerformAction(0, Action.SOWING, 1, 3))
        assertNextLine(machineSowed(0, PlantType.WHEAT, 1))
        assertNextLine(machinePerformAction(0, Action.SOWING, 3, 3))
        assertNextLine(machineSowed(0, PlantType.WHEAT, 1))
        assertNextLine(machinePerformAction(0, Action.SOWING, 8, 3))
        assertNextLine(machineSowed(0, PlantType.WHEAT, 1))
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 4.")
        assertNextLine(machinePerformAction(1, Action.SOWING, 10, 7))
        assertNextLine(machineSowed(1, PlantType.WHEAT, 1))
        assertNextLine("[IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 6.")
        assertNextLine("[IMPORTANT] Farm: Farm 1 finished its actions.")
        assertNextLine(farmStartActions(2))
        assertNextLine(farmSowingPlans(2, emptyList()))
        assertNextLine(farmFinishedActions(2))

        assertNextLine(actionNotPerformed(0, listOf(Action.IRRIGATING)))
        assertNextLine(harvestEstimate(0, 1_499_950, PlantType.WHEAT))
        assertNextLine(actionNotPerformed(1, listOf(Action.IRRIGATING)))
        assertNextLine(harvestEstimate(1, 1_499_950, PlantType.WHEAT))
        assertNextLine(actionNotPerformed(2, listOf(Action.IRRIGATING, Action.HARVESTING)))
        assertNextLine(harvestEstimate(2, 1_028_802, PlantType.GRAPE))
        assertNextLine(actionNotPerformed(99, listOf(Action.HARVESTING)))
        assertNextLine(harvestEstimate(99, 1_028_850, PlantType.GRAPE))

        assertNextLine("[INFO] Simulation Info: Tick 1 started at tick 20 within the year.")
        assertNextLine(soilMoisture(3, 1))
        skipUntilLogType(LogLevel.IMPORTANT, "Farm")
        assertCurrentLine(farmStartActions(1))
        assertNextLine(farmSowingPlans(1, emptyList()))
        assertNextLine(machinePerformAction(0, Action.IRRIGATING, 0, 3))
        assertNextLine(machinePerformAction(0, Action.IRRIGATING, 1, 3))
        assertNextLine(machinePerformAction(0, Action.IRRIGATING, 3, 3))
        assertNextLine(machinePerformAction(0, Action.IRRIGATING, 2, 3))
    }
}
