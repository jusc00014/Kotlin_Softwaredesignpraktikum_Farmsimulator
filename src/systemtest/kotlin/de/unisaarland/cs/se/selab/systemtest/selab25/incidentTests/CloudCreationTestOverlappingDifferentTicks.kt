package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Detekt
 */
class CloudCreationTestOverlappingDifferentTicks : TestExtension() {
    override val name = "CloudCreationTestOverlapping"
    override val description = "Tests Overlapping of CloudCreation throughout ticks."

    override val farms = "incidentTest/farms.json"
    override val scenario = "incidentTest/scenarioCloudCreationOverlappingDifferentTick.json"
    override val map = "incidentTest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 1

    override suspend fun run() {
        var expectedLine = "[IMPORTANT] Incident: Incident 1 of type CLOUD_CREATION happened and affected tiles [1, 5]."
        assert(skipUntilLogType(LogLevel.IMPORTANT, "Incident") == expectedLine)
        expectedLine = "[IMPORTANT] Incident: Incident 2 of type CLOUD_CREATION happened and affected tiles [2, 5, 6]."
        assert(skipUntilLogType(LogLevel.IMPORTANT, "Incident") == expectedLine)
        assertNextLine(
            "[IMPORTANT] Cloud Union: Clouds 2 and 4 united to cloud 5 with 2000 L water and duration 1 on tile 5."
        )
    }
}
