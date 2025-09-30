package de.unisaarland.cs.se.selab.systemtest.selab25.farmactiontests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Test
 */
class MachineTimeOut : TestExtension() {
    override val name = "machineTimeOut"
    override val description = "Tests machine working after timeout"

    override val farms = "farmActionTests/MachineTimeOut/farm.json"
    override val scenario = "onefieldtest/noscenario.json"
    override val map = "farmActionTests/MachineTimeOut/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 6

    override suspend fun run() {
        skipUntilString("[IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 2 for 8 days.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 7.")
    }
}

/**
 * Test
 */
class MachineOrder : TestExtension() {
    override val name = "machineOrder"
    override val description = "Tests simple machine order"

    override val farms = "farmActionTests/MachineOrder/farm.json"
    override val scenario = "onefieldtest/noscenario.json"
    override val map = "farmActionTests/MachineOrder/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 6

    override suspend fun run() {
        skipUntilString("[IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 2 for 6 days.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 3 for 6 days.")
    }
}
