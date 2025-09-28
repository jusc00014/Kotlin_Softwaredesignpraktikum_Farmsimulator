package de.unisaarland.cs.se.selab.systemtest.selab25.cloudtests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Detekt
 */
class Floodgates : TestExtension() {
    override val name = "RainLimitTest"
    override val description = "Tests RainBehavior."

    override val farms = "incidentTest/farms3.json"
    override val scenario = "cloudTest/scenario5.json"
    override val map = "incidentTest/map3.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 1

    override suspend fun run() {
        skipUntilLogType(LogLevel.IMPORTANT, "Cloud Rain")
        assertCurrentLine("[IMPORTANT] Cloud Rain: Cloud 0 on tile 1 rained down 5000 L water.")
        assertNextLine("[INFO] Cloud Dissipation: Cloud 0 dissipates on tile 1.")
        assertNextLine("[INFO] Cloud Dissipation: Cloud 1 dissipates on tile 2.")
    }
}
