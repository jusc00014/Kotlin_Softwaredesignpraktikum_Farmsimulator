package de.unisaarland.cs.se.selab.systemtest.selab25.farmactiontests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Test
 */
class AnotherIrrigation : TestExtension() {
    override val name = "AnotherIrrigation"
    override val description = "Tests irrigation order"

    override val farms = "farmActionTests/AnotherIrrigation/farm.json"
    override val scenario = "onefieldtest/noscenario.json"
    override val map = "farmActionTests/AnotherIrrigation/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 6

    override suspend fun run() {
        skipUntilString("[IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 1 for 1 days.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 5 for 1 days.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 4 for 1 days.")
        skipUntilString("[IMPORTANT] Farm Action: Machine 2 performs IRRIGATING on tile 3 for 1 days.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 2 performs IRRIGATING on tile 2 for 1 days.")
    }
}

/**
 * Test
 */
class AnotherIrrigationEstimate : TestExtension() {
    override val name = "AnotherIrrigationEstimate"
    override val description = "Tests harvest estimate"

    override val farms = "farmActionTests/AnotherIrrigation/farm.json"
    override val scenario = "onefieldtest/noscenario.json"
    override val map = "farmActionTests/AnotherIrrigation/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 6

    override suspend fun run() {
        skipUntilString("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 1080000 g of OAT.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 2 changed to 1239300 g of APPLE.")
        skipUntilString("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 787320 g of OAT.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 2 changed to 903449 g of APPLE.")
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 3 were not performed: WEEDING.")
    }
}
