package de.unisaarland.cs.se.selab.systemtest.selab25.machineTravelTest

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Tests machine travel through villages and forests
 */
class MachineTravelTest : TestExtension() {
    override val name = "MachineTravelTest"
    override val description = "Tests machine movement for ?? ticks."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "machineTravelTest/farms.json"
    override val scenario = "machineTravelTest/scenario.json"
    override val map = "machineTravelTest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 24
    override val startYearTick = 21

    override suspend fun run() {
        skipUntilLogType(LogLevel.IMPORTANT, "Simulation Statistics")
        assertCurrentLine("[IMPORTANT] Simulation Statistics: Farm 0 collected 1000000 g of harvest.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Farm 1 collected 800000 g of harvest.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of POTATO harvested: 0 g.")
        listOf("WHEAT", "OAT", "PUMPKIN", "APPLE").forEach { plant ->
            assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of $plant harvested: 0 g.")
        }
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of GRAPE harvested: 1200000 g.")
        listOf("ALMOND", "CHERRY").forEach { plant ->
            assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of $plant harvested: 0 g.")
        }
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 0 g."
        )
    }
}
