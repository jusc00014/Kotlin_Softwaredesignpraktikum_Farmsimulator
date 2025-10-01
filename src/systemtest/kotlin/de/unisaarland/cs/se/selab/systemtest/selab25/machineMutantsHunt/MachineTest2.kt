package de.unisaarland.cs.se.selab.systemtest.selab25.machineMutantsHunt

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * D
 */
class MachineTest2 : TestExtension() {
    override val name = "ThereWeGoAgain"
    override val description = "Tests machine additional behavior."

    override val farms = "machineMutantsHunt/farms2.json"
    override val scenario = "machineMutantsHunt/scenario.json"
    override val map = "machineMutantsHunt/map2.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 16

    override suspend fun run() {
        val str = "[IMPORTANT] Farm Harvest: Machine 0 has collected 800000 g of ALMOND harvest."
        skipUntilLogType(LogLevel.IMPORTANT, "Farm Action:")
        assertCurrentLine("[IMPORTANT] Farm Action: Machine 0 performs HARVESTING on tile 0 for 1 days.")
        assertNextLine(str)
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs HARVESTING on tile 3 for 1 days.")
        assertNextLine(str)
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs HARVESTING on tile 6 for 1 days.")
        assertNextLine(str)
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished but failed to return.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
    }
}
