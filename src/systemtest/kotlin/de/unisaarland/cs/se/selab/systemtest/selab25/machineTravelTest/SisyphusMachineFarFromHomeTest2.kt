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
    val soilMoist = "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles."
    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, "Simulation Info: Tick 18")
        assertCurrentLine("[INFO] Simulation Info: Tick 18 started at tick 15 within the year.")
        assertNextLine(soilMoist)
        assertNextLine(farmStart)
        assertNextLine(farmCurrentPlans)
        assertNextLine(farmFinish)

        skipUntilLogType(LogLevel.INFO, "Simulation Info: Tick 19")
        assertCurrentLine("[INFO] Simulation Info: Tick 19 started at tick 16 within the year.")
        assertNextLine(soilMoist)
        assertNextLine(farmStart)
        assertNextLine(farmCurrentPlans)
        assertNextLine(farmFinish)
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 7 were not performed: HARVESTING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 7 changed to 0 g of WHEAT.")
    }
}
