package de.unisaarland.cs.se.selab.systemtest.selab25.machineMutantsHunt

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * D
 */
class MachineTest3x2 : TestExtension() {
    override val name = "ALongDay2RangeCheck"
    override val description = "Tests machine behavior."

    override val farms = "machineMutantsHunt/farms2.json"
    override val scenario = "machineMutantsHunt/scenario.json"
    override val map = "machineMutantsHunt/map3.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 21

    override suspend fun run() {
        skipUntilLogType(LogLevel.IMPORTANT, "Farm Action:")
        assertCurrentLine("[IMPORTANT] Farm Action: Machine 0 performs CUTTING on tile 0 for 1 days.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs CUTTING on tile 3 for 1 days.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs CUTTING on tile 5 for 1 days.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 2.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
    }
}
