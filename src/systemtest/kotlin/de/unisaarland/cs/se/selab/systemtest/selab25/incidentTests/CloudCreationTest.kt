package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Tests CloudCreation
 */
class CloudCreationTest : TestExtension() {
    override val name = "CloudCreationTest"
    override val description = "Tests CloudCreationIncident at tick 1."

    override val farms = "incidentTest/farms.json"
    override val scenario = "incidentTest/scenario.json"
    override val map = "incidentTest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 1

    override suspend fun run() {
        val expectedLine = "[IMPORTANT] Incident: Incident 1 of type CLOUD_CREATION happened and affected tiles [5]."
        assert(skipUntilLogType(LogLevel.IMPORTANT, "Incident") == expectedLine)
        assertNextLine(
            "[IMPORTANT] Incident: Incident 2 of type CLOUD_CREATION happened and affected tiles [6, 7]."
        )
    }
}
