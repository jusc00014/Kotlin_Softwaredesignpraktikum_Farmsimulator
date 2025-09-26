package de.unisaarland.cs.se.selab.systemtest.selab25.cloudtests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Detekt
 */
class CloudTestIllRemain : TestExtension() {
    override val name = "CloudTest2"
    override val description = "Tests PostMovement CloudBehavior."

    override val farms = "cloudTest/farms.json"
    override val scenario = "cloudTest/scenario2.json"
    override val map = "cloudTest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 15

    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, "Cloud Movement")
        assertCurrentLine("[INFO] Cloud Movement: Cloud 1 with 3000 L water moved from tile 2 to tile 5.")
        assertNextLine("[INFO] Cloud Movement: Cloud 2 with 3000 L water moved from tile 3 to tile 5.")
        assertNextLine(
            "[IMPORTANT] Cloud Union: Clouds 1 and 2 united to cloud 3 with 6000 L water and duration 1 on tile 5."
        )
        assertNextLine("[IMPORTANT] Cloud Rain: Cloud 3 on tile 5 rained down 70 L water.")
        assertNextLine("[INFO] Cloud Dissipation: Cloud 3 dissipates on tile 5.")
        assertNextLine(
            "[DEBUG] Cloud Position: Cloud 3 is on tile 5, where the amount of sunlight is 118."
        )
    }
}
