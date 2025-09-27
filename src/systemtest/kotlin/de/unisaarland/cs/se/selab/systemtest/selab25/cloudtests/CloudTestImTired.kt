package de.unisaarland.cs.se.selab.systemtest.selab25.cloudtests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Detekt
 */
class CloudTestImTired : TestExtension() {
    override val name = "CloudTest3"
    override val description = "Tests Sun hour reduction and MovementExhaustion."

    override val farms = "cloudTest/farms2.json"
    override val scenario = "cloudTest/scenario3.json"
    override val map = "cloudTest/map2.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 12

    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, "Soil Moisture")
        for (i in 1..5) {
            val light = 168 - i * 3
            assertNextLine("[INFO] Cloud Movement: Cloud 0 with 4000 L water moved from tile 5 to tile 8.")
            assertNextLine("[DEBUG] Cloud Movement: On tile 5, the amount of sunlight is $light.")
            assertNextLine("[INFO] Cloud Movement: Cloud 0 with 4000 L water moved from tile 8 to tile 5.")
            assertNextLine("[DEBUG] Cloud Movement: On tile 8, the amount of sunlight is $light.")
        }
        assertNextLine("[DEBUG] Cloud Position: Cloud 0 is on tile 5, where the amount of sunlight is 103.")
        skipUntilLogType(LogLevel.INFO, "Soil Moisture")
        for (i in 1..5) {
            val light = 168 - i * 3
            assertNextLine("[INFO] Cloud Movement: Cloud 0 with 4000 L water moved from tile 5 to tile 8.")
            assertNextLine("[DEBUG] Cloud Movement: On tile 5, the amount of sunlight is $light.")
            assertNextLine("[INFO] Cloud Movement: Cloud 0 with 4000 L water moved from tile 8 to tile 5.")
            assertNextLine("[DEBUG] Cloud Movement: On tile 8, the amount of sunlight is $light.")
        }
        assertNextLine("[INFO] Cloud Dissipation: Cloud 0 dissipates on tile 5.")
    }
}
