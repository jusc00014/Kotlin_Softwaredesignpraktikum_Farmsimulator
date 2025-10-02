package de.unisaarland.cs.se.selab.systemtest.selab25.farmactiontests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Tests machine irrigation order
 */
class IrrigationContinuationTest : TestExtension() {
    override val name = "IrrigationContinuationTest"
    override val description = "Tests irrigation machine action continuation."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "farmActionTests/IrrigationContinuation/farms.json"
    override val scenario = "machineTravelTest/scenario.json"
    override val map = "farmActionTests/IrrigationContinuation/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 6

    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, "Simulation Info: Tick 1")
        assertCurrentLine("[INFO] Simulation Info: Tick 1 started at tick 7 within the year.")
        skipUntilLogType(LogLevel.IMPORTANT, "Farm Action")
        assertCurrentLine("[IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 1 for 1 days.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 2 for 1 days.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 4 for 1 days.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 3 for 1 days.")

        skipUntilLogType(LogLevel.IMPORTANT, "Simulation Statistics")
        assertCurrentLine("[IMPORTANT] Simulation Statistics: Farm 0 collected 0 g of harvest.")
        listOf("POTATO", "WHEAT", "OAT", "PUMPKIN", "APPLE", "GRAPE", "ALMOND", "CHERRY").forEach { plant ->
            assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of $plant harvested: 0 g.")
        }
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 3174595 g."
        )
    }
}
