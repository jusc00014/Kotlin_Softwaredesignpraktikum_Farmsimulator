package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Checks if the Bees know what's good
 */
class WeirdBeeHappyTest2 : TestExtension() {
    override val name = "WeirdBeeHappy2"
    override val description = "Tests if BeeHappy affects all the correct tiles."

    override val farms = "incidentTest/farmsWeirdForBeeHappy.json"
    override val scenario = "incidentTest/scenarioWeirdBeeHappy.json"
    override val map = "incidentTest/mapWeirdForBeeHappy.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 8
    override val startYearTick = 7

    override suspend fun run() {
        skipUntilLogType(LogLevel.IMPORTANT, "Incident: Incident 4")
        assertCurrentLine("[IMPORTANT] Incident: Incident 4 of type BEE_HAPPY happened and affected tiles 7.")
        skipUntilLogType(LogLevel.IMPORTANT, "Simulation Statistics")
        assertCurrentLine("[IMPORTANT] Simulation Statistics: Farm 0 collected 0 g of harvest.")
        listOf("POTATO", "WHEAT", "OAT", "PUMPKIN", "APPLE", "GRAPE", "ALMOND", "CHERRY").forEach { plant ->
            assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of $plant harvested: 0 g.")
        }
    }
}
