package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Detekt
 */
class VillageCreationTestCrossCloudCreation : TestExtension() {
    override val name = "VillageCreationTestCloudCross"
    override val description = "Tests VillageCreationIncident workflow with CloudCreation."

    override val farms = "incidentTest/farms.json"
    override val scenario = "incidentTest/scenarioVillageCreationCrossCloudCreation.json"
    override val map = "incidentTest/map2.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 1

    override suspend fun run() {
        val expectedLine = "[INFO] Initialization Info: map.json successfully parsed and validated."
        assert(skipUntilLogType(LogLevel.INFO, "Initialization Info") == expectedLine)
        assertNextLine("[INFO] Initialization Info: farms.json successfully parsed and validated.")
        assertNextLine("[IMPORTANT] Initialization Info: scenarioVillageCreationCrossCloudCreation.json is invalid.")
    }
}
