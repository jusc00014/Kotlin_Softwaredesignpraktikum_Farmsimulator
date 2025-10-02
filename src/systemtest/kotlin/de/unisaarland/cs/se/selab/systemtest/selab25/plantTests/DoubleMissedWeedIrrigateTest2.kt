package de.unisaarland.cs.se.selab.systemtest.selab25.plantTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Tests missed irrigate death + weeding
 */
class DoubleMissedWeedIrrigateTest2 : TestExtension() {
    override val name = "DoubleMissedWeedIrrigateTest2"
    override val description = "Tests if weeding is considered as missed if the plant died of thirst."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "plantTests/farmsDoubleMissWeed.json"
    override val scenario = "plantTests/scenario.json"
    override val map = "plantTests/mapDoubleMissedWeedIrrigateAction.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 3
    override val startYearTick = 11

    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, "Simulation Info: Tick 2")
        assertCurrentLine("[INFO] Simulation Info: Tick 2 started at tick 13 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 1 FIELD and 0 PLANTATION tiles.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 1 were not performed: IRRIGATING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 0 g of PUMPKIN.")
        skipUntilLogType(LogLevel.IMPORTANT, "Simulation Statistics")
        assertCurrentLine("[IMPORTANT] Simulation Statistics: Farm 0 collected 0 g of harvest.")
        listOf("POTATO", "WHEAT", "OAT", "PUMPKIN", "APPLE", "GRAPE", "ALMOND", "CHERRY").forEach { plant ->
            assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of $plant harvested: 0 g.")
        }
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 0 g."
        )
    }
}
