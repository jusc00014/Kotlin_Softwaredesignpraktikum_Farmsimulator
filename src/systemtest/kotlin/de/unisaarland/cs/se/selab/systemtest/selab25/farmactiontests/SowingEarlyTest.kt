package de.unisaarland.cs.se.selab.systemtest.selab25.farmactiontests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Test
 */
class SowingEarlyTest : TestExtension() {
    override val name = "NonPrioritizedTestPerformsIrrigatingOnTile3"
    override val description = "Tests nonPrioActions"

    override val farms = "farmActionTests/farm.json"
    override val scenario = "onefieldtest/noscenario.json"
    override val map = "farmActionTests/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 3
    override val startYearTick = 9

    val qwertz = "[IMPORTANT] Farm Action: Machine 2 performs IRRIGATING on tile 3 for 6 days."

    override suspend fun run() {
        skipUntilString(qwertz)
        assertCurrentLine(qwertz)
    }
}
