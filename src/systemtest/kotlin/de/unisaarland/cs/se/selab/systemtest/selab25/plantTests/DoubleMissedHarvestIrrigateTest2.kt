package de.unisaarland.cs.se.selab.systemtest.selab25.plantTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Tests missed irrigate death + harvesting
 */
class DoubleMissedHarvestIrrigateTest2 : TestExtension() {
    override val name = "DoubleMissedHarvestIrrigateTest2"
    override val description = "Tests if harvesting is considered as missed if the plant died of thirst."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "plantTests/farmsBadApple.json"
    override val scenario = "plantTests/scenario.json"
    override val map = "plantTests/mapDoubleMissedHarvestIrrigateAction.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 15

    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, "Simulation Info: Tick 0")
        assertCurrentLine("[INFO] Simulation Info: Tick 0 started at tick 15 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 1 PLANTATION tiles.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
        assertNextLine(
            "[DEBUG] Harvest Estimate: Required actions on tile 1 were not performed: IRRIGATING."
        )
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 0 g of CHERRY.")
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
