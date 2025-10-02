package de.unisaarland.cs.se.selab.systemtest.selab25.machineMutantsHunt

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * D
 */
class MachineTest6 : TestExtension() {
    override val name = "WetWorks"
    override val description = "Tests machine Irrigating behavior."

    override val farms = "machineMutantsHunt/farms4.json"
    override val scenario = "machineMutantsHunt/scenario.json"
    override val map = "machineMutantsHunt/map5.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 21

    override suspend fun run() {
        skipUntilLogType(LogLevel.IMPORTANT, "Farm:")
    }
}
