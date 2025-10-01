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
        skipUntilLogType(LogLevel.IMPORTANT, "Incident:")
    }
}
