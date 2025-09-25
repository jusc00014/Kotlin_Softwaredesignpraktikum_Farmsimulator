package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Detekt Boom
 */
class CloudCreationTestLargeRadius : TestExtension() {
    override val name = "CloudCreationTestLargeRadius"
    override val description = "Tests CloudCreationIncident with high radius."

    override val farms = "incidentTest/farms.json"
    override val scenario = "incidentTest/scenarioCloudCreationLargeRadius.json"
    override val map = "incidentTest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 1

    override suspend fun run() {
        val expectedLine = "[IMPORTANT] Incident: Incident 1 of type CLOUD_CREATION" +
            " happened and affected tiles [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 13, 14, 16, 17]."
        skipUntilLogType(LogLevel.IMPORTANT, "Incident")
        assertCurrentLine(expectedLine)
    }
}
