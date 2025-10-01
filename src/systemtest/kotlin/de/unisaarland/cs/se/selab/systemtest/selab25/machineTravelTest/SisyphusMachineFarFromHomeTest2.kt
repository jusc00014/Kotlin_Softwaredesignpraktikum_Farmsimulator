package de.unisaarland.cs.se.selab.systemtest.selab25.machineTravelTest

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Tests the patience of the machine
 */
class SisyphusMachineFarFromHomeTest2 : TestExtension() {
    override val name = "SisyphusMachineFarFromHomeTest2"
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
    val missedHarvest = "[DEBUG] Harvest Estimate: Required actions on tile 7 were not performed: HARVESTING."
    override suspend fun run() {
        skipUntilLogType(LogLevel.IMPORTANT, "Farm Action")
        assertCurrentLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 7 for 14 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 0 has sowed WHEAT according to sowing plan 0.")
        assertNextLine(farmGoHome)

        skipUntilLogType(LogLevel.INFO, "Simulation Info: Tick 16")
        assertCurrentLine("[INFO] Simulation Info: Tick 16 started at tick 13 within the year.")
        assertNextLine(soilMoist)
        assertNextLine(farmStart)
        assertNextLine(farmCurrentPlans)
        assertNextLine(farmFinish)

        skipUntilLogType(LogLevel.INFO, "Simulation Info: Tick 17")
        assertCurrentLine("[INFO] Simulation Info: Tick 17 started at tick 14 within the year.")
        assertNextLine(soilMoist)
        assertNextLine(farmStart)
        assertNextLine(farmCurrentPlans)
        assertNextLine(farmFinish)
        assertNextLine(missedHarvest)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 7 changed to 61258 g of WHEAT.")

        skipUntilLogType(LogLevel.INFO, "Simulation Info: Tick 18")
        assertCurrentLine("[INFO] Simulation Info: Tick 18 started at tick 15 within the year.")
        assertNextLine(soilMoist)
        assertNextLine(farmStart)
        assertNextLine(farmCurrentPlans)
        assertNextLine(farmFinish)
        assertNextLine(missedHarvest)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 7 changed to 0 g of WHEAT.")

        skipUntilLogType(LogLevel.INFO, "Simulation Info: Tick 19")
        assertCurrentLine("[INFO] Simulation Info: Tick 19 started at tick 16 within the year.")
        assertNextLine(soilMoist)
        assertNextLine(farmStart)
        assertNextLine(farmCurrentPlans)
        assertNextLine(farmFinish)
        for (i in 17..19) {
            assertNextLine("[INFO] Simulation Info: Tick ${i + 3} started at tick $i within the year.")
            assertNextLine(soilMoist)
            assertNextLine(farmStart)
            assertNextLine(farmCurrentPlans)
            assertNextLine(farmFinish)
        }
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
