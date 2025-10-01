package de.unisaarland.cs.se.selab.systemtest.selab25.machineMutantsHunt

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Detekt
 */
class UltimateMachineTest : TestExtension() {
    override val name = "NoWayHomeTest"
    override val description = "Tests machine movement behavior."

    override val farms = "machineMutantsHunt/farms.json"
    override val scenario = "machineMutantsHunt/scenario.json"
    override val map = "machineMutantsHunt/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 16

    override suspend fun run() {
        skipUntilLogType(LogLevel.IMPORTANT, "Farm Action:")
        assertCurrentLine("[IMPORTANT] Farm Action: Machine 0 performs HARVESTING on tile 0 for 1 days.")
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 0 has collected 800000 g of ALMOND harvest.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished but failed to return.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")
    }
}
