package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Test
 */
class MissingPlantationTest : TestExtension() {
    override val name = "MissingPlantationTest"
    override val description = "Tests Missing Plantation"

    override val farms = "farmParser/missingplantagefarms.json"
    override val scenario = "onefieldtest/noscenario.json"
    override val map = "farmActionTests/MachineTravel/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 16

    override suspend fun run() {
        skipUntilString("[IMPORTANT] Initialization Info: missingplantagefarms.json is invalid.")
    }
}

/**
 * Test
 */
class MissingFarmsteadTest : TestExtension() {
    override val name = "MissingFarmsteadTest"
    override val description = "Tests Missing Farmstead"

    override val farms = "farmParser/missingfarmsteadfarm.json"
    override val scenario = "onefieldtest/noscenario.json"
    override val map = "farmActionTests/MachineTravel/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 16

    override suspend fun run() {
        skipUntilString("[IMPORTANT] Initialization Info: missingfarmsteadfarm.json is invalid.")
    }
}
