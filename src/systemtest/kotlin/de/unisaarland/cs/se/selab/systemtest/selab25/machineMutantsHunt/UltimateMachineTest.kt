package de.unisaarland.cs.se.selab.systemtest.selab25.machineMutantsHunt

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Detekt
 */
class UltimateMachineTest : TestExtension() {
    override val name = "UltimateMachineTest"
    override val description = "Tests machine behavior."

    override val farms = "machineTravelTest/farms.json"
    override val scenario = "machineTravelTest/scenario.json"
    override val map = "machineTravelTest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 1

    override suspend fun run() {
        return
    }
}
