package de.unisaarland.cs.se.selab.systemtest.selab25.onefieldtest
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Tests irrigating and drought
 */
class Onefieldtest : TestExtension() {
    override val name = "OneFieldTest"
    override val description = "Tests one field with irrigating and drought"

    override val farms = "onefieldtest/farms.json"
    override val scenario = "onefieldtest/scenario.json"
    override val map = "onefieldtest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 9

    override suspend fun run() {
        return
    }
}

/**
 * Tests sowing and drought
 */
class Onefieldtestshouldsow : TestExtension() {
    override val name = "OneFieldTestShouldSow"
    override val description = "Tests one field with sowing"

    override val farms = "onefieldtest/farms.json"
    override val scenario = "onefieldtest/scenario.json"
    override val map = "onefieldtest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 17

    override suspend fun run() {
        return
    }
}
