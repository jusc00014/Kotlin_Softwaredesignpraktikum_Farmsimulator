package de.unisaarland.cs.se.selab.systemtest.selab25.plantTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Tests apples
 */
class BadAppleEstimateTest3 : TestExtension() {
    override val name = "BadAppleEstimate3Test"
    override val description = "Tests initial harvest estimate of apples in tick 1."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "plantTests/farmsBadApple.json"
    override val scenario = "plantTests/scenario.json"
    override val map = "plantTests/mapBadApple.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 3

    override suspend fun run() {
        skipUntilLogType(LogLevel.IMPORTANT, "Simulation Statistics")
        assertCurrentLine("[IMPORTANT] Simulation Statistics: Farm 0 collected 0 g of harvest.")
        listOf("POTATO", "WHEAT", "OAT", "PUMPKIN", "APPLE", "GRAPE", "ALMOND", "CHERRY").forEach { plant ->
            assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of $plant harvested: 0 g.")
        }
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 1377000 g."
        )
    }
}
