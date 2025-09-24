package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Super
 */
class CloudCreationTestOverlapping : TestExtension() {
    override val name = "CloudCreationTestOverlapping"
    override val description = "Tests ParsingValidation of CloudCreationIncident."

    override val farms = "incidentTest/farms.json"
    override val scenario = "incidentTest/scenarioCloudCreationOverlapping.json"
    override val map = "incidentTest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 1

    override suspend fun run() {
        val expectedLine = "Initialization Info: map.json successfully parsed and validated."
        assert(skipUntilLogType(LogLevel.INFO, "Initialization Info") == expectedLine)
        assertNextLine(
            "Initialization Info: farms.json successfully parsed and validated."
        )
        assertNextLine(
            "Initialization Info: scenarioCloudCreationOverlapping.json is invalid."
        )
    }
}
