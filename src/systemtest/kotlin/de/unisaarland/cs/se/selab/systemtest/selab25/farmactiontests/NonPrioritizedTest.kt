package de.unisaarland.cs.se.selab.systemtest.selab25.farmactiontests
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Tests non Prio
 */
class NonPrioritizedTest : TestExtension() {
    override val name = "NonPrioritizedTest"
    override val description = "Tests nonPrioActions"

    override val farms = "farmActionTests/farm.json"
    override val scenario = "onefieldtest/noscenario.json"
    override val map = "farmActionTests/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 3
    override val startYearTick = 9

    val sownstr = "[IMPORTANT] Farm Sowing: Machine 4 has sowed POTATO according to sowing plan 1."

    override suspend fun run() {
        skipUntilString(sownstr)
        assertCurrentLine("[IMPORTANT] Farm Sowing: Machine 4 has sowed POTATO according to sowing plan 1.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 4 performs SOWING on tile 3 for 4 days.")
        assertNextLine(sownstr)
        assertNextLine("[IMPORTANT] Farm Action: Machine 4 performs SOWING on tile 5 for 4 days.")
        assertNextLine(sownstr)
        assertNextLine("[IMPORTANT] Farm Machine: Machine 4 is finished and returns to the shed at 7.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 2 performs SOWING on tile 6 for 6 days.")
        skipUntilString("[IMPORTANT] Farm Action: Machine 4 performs WEEDING on tile 1 for 4 days.")
        assertCurrentLine("[IMPORTANT] Farm Action: Machine 4 performs WEEDING on tile 1 for 4 days.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 4 performs WEEDING on tile 3 for 4 days.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 4 performs WEEDING on tile 5 for 4 days.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 4 is finished and returns to the shed at 7.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 2 performs IRRIGATING on tile 6 for 6 days.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 2 performs IRRIGATING on tile 4 for 6 days.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 7.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 3 performs MOWING on tile 2 for 8 days.")
        skipUntilString("[DEBUG] Harvest Estimate: Required actions on tile 3 were not performed: IRRIGATING.")
        assertCurrentLine("[DEBUG] Harvest Estimate: Required actions on tile 3 were not performed: IRRIGATING.")
    }
}
