package de.unisaarland.cs.se.selab.systemtest.selab25.farmactiontests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Test
 */
class SowingEarlyTest : TestExtension() {
    override val name = "SowingEarlyTest"
    override val description = "Tests Sowing time"

    override val farms = "farmActionTests/SowingEarly/farm.json"
    override val scenario = "onefieldtest/noscenario.json"
    override val map = "farmActionTests/SowingEarly/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 17
    override val startYearTick = 6

    override suspend fun run() {
        skipUntilString("[IMPORTANT] Farm: Farm 1 starts its actions.")
        skipUntilString("[IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 1 for 6 days.")
        skipUntilString("[IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 2 for 6 days.")
        skipUntilString("[IMPORTANT] Farm Sowing: Machine 1 has sowed POTATO according to sowing plan 2.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 4.")
        assertNextLine("[IMPORTANT] Farm: Farm 1 finished its actions.")
        assertNextLine("[INFO] Simulation Info: Tick 2 started at tick 8 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")
        assertNextLine("[IMPORTANT] Farm: Farm 1 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: ."
        )
        skipUntilString("[INFO] Simulation Info: Tick 11 started at tick 17 within the year.")
        skipUntilString("[IMPORTANT] Farm Action: Machine 1 performs HARVESTING on tile 1 for 6 days.")
        skipUntilString("[IMPORTANT] Farm Action: Machine 1 performs HARVESTING on tile 2 for 6 days.")
        skipUntilString("[INFO] Simulation Info: Tick 16 started at tick 22 within the year.")
        skipUntilString(
            "[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1."
        )
        skipUntilString("[IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 1 for 6 days.")
    }
}

/**
 * Test
 */
class HarvestEstimateSowingEarlyTest : TestExtension() {
    override val name = "HarvestEstimateSowingEarlyTest"
    override val description = "Tests harvest estimate"

    override val farms = "farmActionTests/SowingEarly/farm.json"
    override val scenario = "onefieldtest/noscenario.json"
    override val map = "farmActionTests/SowingEarly/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 17
    override val startYearTick = 6

    override suspend fun run() {
        skipUntilString("[IMPORTANT] Simulation Info: Simulation statistics are calculated.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Farm 1 collected 697354 g of harvest.")
    }
}
