package de.unisaarland.cs.se.selab.systemtest.selab25.farmactiontests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Test
 */
class CatchPlantLover : TestExtension() {
    override val name = "CatchPlantLover1"
    override val description = "Tests machine behavior"

    override val farms = "farmActionTests/CatchPlantLover/farm1.json"
    override val scenario = "onefieldtest/noscenario.json"
    override val map = "farmActionTests/CatchPlantLover/map1.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 16

    override suspend fun run() {
        skipUntilString("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 4 PLANTATION tiles.")
        skipUntilString(
            "[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm: Farm 1 finished its actions.")
    }
}
