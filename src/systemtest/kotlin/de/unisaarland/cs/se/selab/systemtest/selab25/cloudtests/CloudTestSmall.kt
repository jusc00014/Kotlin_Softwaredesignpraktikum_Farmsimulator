package de.unisaarland.cs.se.selab.systemtest.selab25.cloudtests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Detekt
 */
class CloudTestSmall : TestExtension() {
    override val name = "CloudTest"
    override val description = "Tests CloudBehavior."

    override val farms = "cloudTest/farms.json"
    override val scenario = "cloudTest/scenario.json"
    override val map = "cloudTest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 5

    override suspend fun run() {
        skipUntilLogType(LogLevel.IMPORTANT, "Cloud Rain")
        assertCurrentLine("[IMPORTANT] Cloud Rain: Cloud 0 on tile 4 rained down 100 L water.")
        assertNextLine("[INFO] Cloud Movement: Cloud 0 with 4900 L water moved from tile 4 to tile 6.")
        assertNextLine("[DEBUG] Cloud Movement: On tile 4, the amount of sunlight is 123.")
        assertNextLine("[INFO] Cloud Dissipation: Cloud 0 got stuck on tile 6.")
        assertNextLine("[INFO] Cloud Movement: Cloud 1 with 2000 L water moved from tile 1 to tile 4.")
        assertNextLine("[INFO] Cloud Movement: Cloud 1 with 2000 L water moved from tile 4 to tile 6.")
        assertNextLine("[DEBUG] Cloud Movement: On tile 4, the amount of sunlight is 120.")
        assertNextLine("[INFO] Cloud Dissipation: Cloud 1 got stuck on tile 6.")
        assertNextLine("[IMPORTANT] Cloud Rain: Cloud 2 on tile 7 rained down 6500 L water.")
        assertNextLine("[INFO] Cloud Dissipation: Cloud 2 dissipates on tile 7.")
        assertNextLine("[INFO] Cloud Movement: Cloud 3 with 3000 L water moved from tile 2 to tile 5.")
        assertNextLine("[INFO] Cloud Movement: Cloud 4 with 3000 L water moved from tile 3 to tile 5.")
        assertNextLine(
            "[IMPORTANT] Cloud Union: Clouds 3 and 4 united to cloud 5 with 6000 L water and duration 1 on tile 5."
        )
        assertNextLine("[IMPORTANT] Cloud Rain: Cloud 5 on tile 5 rained down 70 L water.")
        assertNextLine("[INFO] Cloud Dissipation: Cloud 5 dissipates on tile 5.")
    }
}
