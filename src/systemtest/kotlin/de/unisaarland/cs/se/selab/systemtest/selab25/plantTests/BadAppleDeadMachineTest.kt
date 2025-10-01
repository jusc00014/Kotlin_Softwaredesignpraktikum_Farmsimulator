package de.unisaarland.cs.se.selab.systemtest.selab25.plantTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Tests apples
 */
class BadAppleDeadMachineTest : TestExtension() {
    override val name = "BadAppleDeadMachineTest"
    override val description = "Tests what an apple is and how it works."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "plantTests/farmsBadApple.json"
    override val scenario = "plantTests/scenarioBroken.json"
    override val map = "plantTests/mapBadApple.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 24
    override val startYearTick = 1

    val harvestEstimate = "Harvest Estimate"
    val notPerformCut = "[DEBUG] Harvest Estimate: Required actions on tile 1 were not performed: CUTTING."
    val notPerformMow = "[DEBUG] Harvest Estimate: Required actions on tile 1 were not performed: MOWING."
    override suspend fun run() {
        skipUntilLogType(LogLevel.DEBUG, harvestEstimate)
        assertCurrentLine(notPerformCut)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 451724 g of APPLE.")

        skipUntilLogType(LogLevel.DEBUG, harvestEstimate)
        assertCurrentLine(notPerformMow)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 32425 g of APPLE.")

        skipUntilLogType(LogLevel.DEBUG, harvestEstimate)
        assertCurrentLine(notPerformMow)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 2582 g of APPLE.")

        skipUntilLogType(LogLevel.INFO, "Simulation Info: Tick 19")
        assertCurrentLine("[INFO] Simulation Info: Tick 19 started at tick 20 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 1 were not performed: HARVESTING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 0 g of APPLE.")
        skipUntilLogType(LogLevel.IMPORTANT, "Simulation Statistics")
        assertCurrentLine("[IMPORTANT] Simulation Statistics: Farm 0 collected 0 g of harvest.")
        listOf("POTATO", "WHEAT", "OAT", "PUMPKIN", "APPLE", "GRAPE", "ALMOND", "CHERRY").forEach { plant ->
            assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of $plant harvested: 0 g.")
        }
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 1115370 g."
        )
    }
}
