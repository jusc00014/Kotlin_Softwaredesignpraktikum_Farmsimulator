package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Detekt
 */
class VillageCreationTest : TestExtension() {
    override val name = "VillageCreationTest"
    override val description = "Tests VillageCreationIncident at tick 1."

    override val farms = "incidentTest/farms.json"
    override val scenario = "incidentTest/scenarioVillageCreation.json"
    override val map = "incidentTest/map2.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 1

    override suspend fun run() {
        skipUntilLogType(LogLevel.IMPORTANT, "Incident")
        assertCurrentLine("[IMPORTANT] Incident: Incident 1 of type CITY_EXPANSION happened and affected tiles [14].")
    }
}
