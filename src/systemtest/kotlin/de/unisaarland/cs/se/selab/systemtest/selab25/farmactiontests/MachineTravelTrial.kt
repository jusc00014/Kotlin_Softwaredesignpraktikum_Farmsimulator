package de.unisaarland.cs.se.selab.systemtest.selab25.farmactiontests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * test
 */
class MachineTravelTrial : TestExtension() {
    override val name = "MachineTravelTrial"
    override val description = "Tests machine travel"

    override val farms = "farmActionTests/MachineTravel/farm.json"
    override val scenario = "onefieldtest/noscenario.json"
    override val map = "farmActionTests/MachineTravel/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 16

    override suspend fun run() {
        skipUntilString("[IMPORTANT] Farm Harvest: Machine 1 has collected 800000 g of ALMOND harvest.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 1 is finished but failed to return.")
        skipUntilString("[IMPORTANT] Farm Harvest: Machine 2 has collected 800000 g of ALMOND harvest.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 3.")
        skipUntilString("[DEBUG] Harvest Estimate: Required actions on tile 8 were not performed: CUTTING.")
    }
}
