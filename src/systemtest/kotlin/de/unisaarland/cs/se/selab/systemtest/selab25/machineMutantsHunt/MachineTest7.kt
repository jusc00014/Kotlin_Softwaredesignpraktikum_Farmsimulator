package de.unisaarland.cs.se.selab.systemtest.selab25.machineMutantsHunt

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * D
 */
class MachineTest7 : TestExtension() {
    override val name = "HalfAYear"
    override val description = "Tests farm/machine behavior."

    override val farms = "machineMutantsHunt/farms5.json"
    override val scenario = "machineMutantsHunt/scenario.json"
    override val map = "machineMutantsHunt/map6.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 12
    override val startYearTick = 6

    override suspend fun run() {
        return
    }
}
