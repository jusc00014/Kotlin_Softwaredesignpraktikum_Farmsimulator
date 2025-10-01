package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Detekt
 */
class WeirdBeeHappyTest : TestExtension() {
    override val name = "WeirdBeeHappy"
    override val description = "Tests if BeeHappy affects all the correct tiles."

    override val farms = "incidentTest/farmsWeirdForBeeHappy.json"
    override val scenario = "incidentTest/scenarioWeirdBeeHappy.json"
    override val map = "incidentTest/mapWeirdForBeeHappy.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 8
    override val startYearTick = 7

    override suspend fun run() {
        skipUntilLogType(LogLevel.IMPORTANT, "Simulation Statistics")
        assertCurrentLine("[IMPORTANT] Simulation Statistics: Farm 0 collected 0 g of harvest.")
        listOf("POTATO", "WHEAT", "OAT", "PUMPKIN", "APPLE", "GRAPE", "ALMOND", "CHERRY").forEach { plant ->
            assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of $plant harvested: 0 g.")
        }
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 1934032 g."
        )
    }
}
