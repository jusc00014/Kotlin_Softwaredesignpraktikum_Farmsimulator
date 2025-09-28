package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Tests if the Animal Attack incident has a forest tile in the radius
 */
class InvalidAnimalAttackTest : TestExtension() {
    override val name = "InvalidAnimalAttackTest"
    override val description = "Tests if the Animal Attack incident has a forest tile in the radius"

    override val map = "scenarioParser/map.json"
    override val farms = "scenarioParser/farms.json"
    override val scenario = "scenarioParser/invalidAnimalScenario.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 24
    override val startYearTick = 1

    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, "Initialization Info")
        assertCurrentLine("[INFO] Initialization Info: map.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: farms.json successfully parsed and validated.")
        assertNextLine("[IMPORTANT] Initialization Info: invalidAnimalScenario.json is invalid.")
    }
}
