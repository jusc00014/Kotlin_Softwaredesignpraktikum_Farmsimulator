package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Detekt
 */
class CloudCreationTestNoTileToAffect : TestExtension() {
    override val name = "CloudCreationTestLargeRadius"
    override val description = "Tests CloudCreationIncident with high radius."

    override val farms = "incidentTest/farms.json"
    override val scenario = "incidentTest/scenarioCloudCreationVillage.json"
    override val map = "incidentTest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 1
    override suspend fun run() {
        val expectedLine = "[INFO] Initialization Info: map.json successfully parsed and validated."
        assert(skipUntilLogType(LogLevel.INFO, "Initialization Info") == expectedLine)
        assertNextLine("[INFO] Initialization Info: farms.json successfully parsed and validated.")
        assertNextLine("[IMPORTANT] Initialization Info: scenarioCloudCreationVillage.json is invalid.")
    }
}
