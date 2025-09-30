package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Detekt
 */
class BeeHappyTest : TestExtension() {
    override val name = "BeeHappy"
    override val description = "Tests BeeHappyImpact."

    override val farms = "incidentTest/farms.json"
    override val scenario = "incidentTest/scenarioBeeHappy2.json"
    override val map = "incidentTest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 1

    override suspend fun run() {
        skipUntilLogType(LogLevel.IMPORTANT, "Incident:")
        assertCurrentLine("[IMPORTANT] Incident: Incident 0 of type BEE_HAPPY happened and affected tiles .")
    }
}
