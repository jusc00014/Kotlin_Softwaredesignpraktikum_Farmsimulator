package de.unisaarland.cs.se.selab.systemtest.selab25.farmactiontests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Test
 */
class HarvestingTypeTest : TestExtension() {
    override val name = "Harvesting Type Test"
    override val description = "Tests machine behavior"

    override val farms = "farmActionTests/HarvestingTypeTest/farm.json"
    override val scenario = "onefieldtest/noscenario.json"
    override val map = "farmActionTests/HarvestingTypeTest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 17

    override suspend fun run() {
        skipUntilString(
            "[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs HARVESTING on tile 1 for 1 days.")
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 1 has collected 800000 g of ALMOND harvest.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 5.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 1 unloads 800000 g of ALMOND harvest in the shed.")
        assertNextLine("[IMPORTANT] Farm: Farm 1 finished its actions.")
    }
}

/**
 * Test
 */
class HarvestingTypeTestEstimate : TestExtension() {
    override val name = "Harvesting Type Test Estimate"
    override val description = "Tests harvest estimate"

    override val farms = "farmActionTests/HarvestingTypeTest/farm.json"
    override val scenario = "onefieldtest/noscenario.json"
    override val map = "farmActionTests/HarvestingTypeTest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 17

    override suspend fun run() {
        skipUntilString("[DEBUG] Harvest Estimate: Required actions on tile 2 were not performed: MOWING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 2 changed to 1115370 g of APPLE.")
        assertNextLine(
            "[DEBUG] Harvest Estimate: Required actions on tile 4 were not performed: IRRIGATING,HARVESTING."
        )
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 4 changed to 1139952 g of GRAPE.")
        assertNextLine("[IMPORTANT] Simulation Info: Simulation ended at tick 1.")
    }
}
