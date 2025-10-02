package de.unisaarland.cs.se.selab.systemtest.selab25.machineMutantsHunt

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * D
 */
class MachineTest4 : TestExtension() {
    override val name = "WhoShouldGo"
    override val description = "Tests machine selection behavior."

    override val farms = "machineMutantsHunt/farms3.json"
    override val scenario = "machineMutantsHunt/scenario.json"
    override val map = "machineMutantsHunt/map3.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 21

    override suspend fun run() {
        return
    }
}
