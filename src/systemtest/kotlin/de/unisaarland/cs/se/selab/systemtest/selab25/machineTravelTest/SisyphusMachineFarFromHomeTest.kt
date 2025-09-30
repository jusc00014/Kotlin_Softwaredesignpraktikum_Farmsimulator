package de.unisaarland.cs.se.selab.systemtest.selab25.machineTravelTest

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Tests the patience of the machine
 */
class SisyphusMachineFarFromHomeTest : TestExtension() {
    override val name = "SisyphusMachineFarFromHomeTest"
    override val description = "Tests if the machine takes the long route home and doesn't sow to early."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "machineTravelTest/farmsFarFromHome.json"
    override val scenario = "machineTravelTest/scenario.json"
    override val map = "machineTravelTest/mapFarFromHome.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 24
    override val startYearTick = 21
    val farmStart = "[IMPORTANT] Farm: Farm 0 starts its actions."
    val farmCurrentPlans =
        "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 1."
    val farmFinish = "[IMPORTANT] Farm: Farm 0 finished its actions."
    val farmGoHome = "[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 100."
    val soilMoist = "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles."
    override suspend fun run() {
        skipUntilLogType(LogLevel.IMPORTANT, "Farm Action")
        assertCurrentLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 7 for 14 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 0 has sowed WHEAT according to sowing plan 0.")
        assertNextLine(farmGoHome)

        skipUntilLogType(LogLevel.INFO, "Simulation Info: Tick 18")
        assertCurrentLine("[INFO] Simulation Info: Tick 18 started at tick 15 within the year.")
        assertNextLine(soilMoist)
        assertNextLine(farmStart)
        assertNextLine(farmCurrentPlans)
        assertNextLine(farmFinish)
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 7 were not performed: HARVESTING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 7 changed to 0 g of WHEAT.")

        skipUntilLogType(LogLevel.INFO, "Simulation Info: Tick 19")
        assertCurrentLine("[INFO] Simulation Info: Tick 19 started at tick 16 within the year.")
        assertNextLine(soilMoist)
        assertNextLine(farmStart)
        assertNextLine(farmCurrentPlans)
        assertNextLine(farmFinish)

        assertNextLine("[INFO] Simulation Info: Tick 20 started at tick 17 within the year.")
        assertNextLine(soilMoist)
        assertNextLine(farmStart)
        assertNextLine(farmCurrentPlans)
        assertNextLine(farmFinish)

        assertNextLine("[INFO] Simulation Info: Tick 21 started at tick 18 within the year.")
        assertNextLine(soilMoist)
        assertNextLine(farmStart)
        assertNextLine(farmCurrentPlans)
        assertNextLine(farmFinish)

        assertNextLine("[INFO] Simulation Info: Tick 22 started at tick 19 within the year.")
        assertNextLine(soilMoist)
        assertNextLine(farmStart)
        assertNextLine(farmCurrentPlans)
        assertNextLine(farmFinish)

        assertNextLine("[INFO] Simulation Info: Tick 23 started at tick 20 within the year.")
        assertNextLine(soilMoist)
        assertNextLine(farmStart)
        assertNextLine(farmCurrentPlans)
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 7 for 14 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 0 has sowed WHEAT according to sowing plan 1.")
        assertNextLine(farmGoHome)
        assertNextLine(farmFinish)

        skipUntilLogType(LogLevel.IMPORTANT, "Simulation Statistics")
        assertCurrentLine("[IMPORTANT] Simulation Statistics: Farm 0 collected 0 g of harvest.")
        listOf("POTATO", "WHEAT", "OAT", "PUMPKIN", "APPLE", "GRAPE", "ALMOND", "CHERRY").forEach { plant ->
            assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of $plant harvested: 0 g.")
        }
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 1500000 g."
        )
    }
}
