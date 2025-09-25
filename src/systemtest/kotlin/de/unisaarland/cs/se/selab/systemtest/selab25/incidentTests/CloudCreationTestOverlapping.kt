package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Super
 */
class CloudCreationTestOverlapping : TestExtension() {
    override val name = "CloudCreationTestOverlapping"
    override val description = "Tests CloudCreationIncident Overlapping Tiles within same tick."

    override val farms = "incidentTest/farms.json"
    override val scenario = "incidentTest/scenarioCloudCreationOverlapping.json"
    override val map = "incidentTest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 1

    override suspend fun run() {
        val expectedLine = "[IMPORTANT] Incident: Incident 1 of type CLOUD_CREATION happened and affected tiles [1, 5]."
        assert(skipUntilLogType(LogLevel.INFO, "Incident") == expectedLine)
        assertNextLine("[IMPORTANT] Incident: Incident 2 of type CLOUD_CREATION happened and affected tiles [2, 6].")
    }
}
