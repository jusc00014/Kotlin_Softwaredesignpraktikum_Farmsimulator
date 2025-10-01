package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Detekt
 */
class BeeHappyTestInvalid : TestExtension() {
    override val name = "BeeHappy4"
    override val description = "Tests BeeHappyValidation."

    override val farms = "incidentTest/farms.json"
    override val scenario = "incidentTest/scenarioBeeHappyNoMeadow.json"
    override val map = "incidentTest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 8

    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, "Initialization Info")
        assertCurrentLine("[INFO] Initialization Info: map.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: farms.json successfully parsed and validated.")
        assertNextLine("[IMPORTANT] Initialization Info: scenarioBeeHappyNoMeadow.json is invalid.")
    }

}
