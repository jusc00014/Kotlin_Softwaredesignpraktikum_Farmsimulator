package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Detekt
 */
class VillageCreationTestCrossCloudValid : TestExtension() {
    override val name = "VillageCreationTestCloudCrossValid"
    override val description = "Tests VillageCreationIncident workflow with CloudCreation."

    override val farms = "incidentTest/farms.json"
    override val scenario = "incidentTest/scenarioVillageCreationCrossCloudCreation2.json"
    override val map = "incidentTest/map2.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 1

    override suspend fun run() {
        skipUntilLogType(LogLevel.IMPORTANT, "Incident:")
        assertCurrentLine("[IMPORTANT] Incident: Incident 1 of type CITY_EXPANSION happened and affected tiles 14.")
        assertNextLine("[INFO] Cloud Dissipation: Cloud 0 got stuck on tile 14.")
        assertNextLine("[IMPORTANT] Incident: Incident 2 of type CLOUD_CREATION happened and affected tiles 13,17.")
    }
}
