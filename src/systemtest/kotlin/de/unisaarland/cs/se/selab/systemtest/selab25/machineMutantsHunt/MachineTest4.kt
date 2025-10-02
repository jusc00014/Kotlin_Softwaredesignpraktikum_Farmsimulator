package de.unisaarland.cs.se.selab.systemtest.selab25.machineMutantsHunt

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
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
        skipUntilLogType(LogLevel.IMPORTANT, "Farm:")
        assertCurrentLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs CUTTING on tile 0 for 1 days.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs CUTTING on tile 3 for 1 days.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs CUTTING on tile 5 for 1 days.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 2.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 2 performs CUTTING on tile 6 for 1 days.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 2.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
    }
}
