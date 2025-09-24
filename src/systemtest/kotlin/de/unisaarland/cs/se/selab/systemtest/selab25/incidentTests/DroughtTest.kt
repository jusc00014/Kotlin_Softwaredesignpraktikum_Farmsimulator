package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Test
 */
class DroughtTest : TestExtension() {
    override val name = "DroughtTest"
    override val description = "Tests Drought."

    override val farms = "incidentTest/farms.json"
    override val scenario = "incidentTest/scenarioDrought.json"
    override val map = "incidentTest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 1

    override suspend fun run() {
        return
    }
}
