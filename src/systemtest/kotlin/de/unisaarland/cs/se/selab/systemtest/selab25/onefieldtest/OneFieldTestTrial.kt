package de.unisaarland.cs.se.selab.systemtest.selab25.onefieldtest

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

private object Logs {
    const val SOWING =
        "[IMPORTANT] Farm Sowing: Machine 3 has sowed WHEAT according to sowing plan 1."
    const val DROUGHT =
        "[IMPORTANT] Incident: Incident 1 of type DROUGHT happened and affected tiles 38."
    const val MOISTURE =
        "[INFO] Soil Moisture: The soil moisture is below threshold in 1 FIELD and 0 PLANTATION tiles."
    const val IRRIGATION =
        "[IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 38 for 10 days."
}

/**
 * Test of the expected results
 */
class OneFieldTestTrialMoisture : TestExtension() {
    override val name = "OneFieldTestTrialMoisture"
    override val description = "Tests if expected results are wrong"

    override val farms = "onefieldtest/farms.json"
    override val scenario = "onefieldtest/scenario.json"
    override val map = "onefieldtest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 19

    override suspend fun run() {
        skipUntilString(Logs.DROUGHT)
        assertCurrentLine(Logs.DROUGHT)
        skipUntilString(Logs.MOISTURE)
        assertCurrentLine(Logs.MOISTURE)
    }
}

/**
 * Test of the expected results
 */
class OneFieldTestTrialSowingTime18 : TestExtension() {
    override val name = "OneFieldTestTrialSowingTime18"
    override val description = "Tests if expected results are wrong"

    override val farms = "onefieldtest/farms.json"
    override val scenario = "onefieldtest/scenario.json"
    override val map = "onefieldtest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 18

    override suspend fun run() {
        skipUntilString(Logs.SOWING)
        assertCurrentLine(Logs.SOWING)
    }
}

class OneFieldTestTrialSowingTime19 : TestExtension() {
    override val name = "OneFieldTestTrialSowingTime19"
    override val description = "Tests if expected results are wrong"

    override val farms = "onefieldtest/farms.json"
    override val scenario = "onefieldtest/scenario.json"
    override val map = "onefieldtest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 19

    override suspend fun run() {
        skipUntilString(Logs.SOWING)
        assertCurrentLine(Logs.SOWING)
    }
}

class OneFieldTestTrialSowingTime20 : TestExtension() {
    override val name = "OneFieldTestTrialSowingTime20"
    override val description = "Tests if expected results are wrong"

    override val farms = "onefieldtest/farms.json"
    override val scenario = "onefieldtest/scenario.json"
    override val map = "onefieldtest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 20

    override suspend fun run() {
        skipUntilString(Logs.SOWING)
        assertCurrentLine(Logs.SOWING)
    }
}

class OneFieldTestTrialIrrigatingAfterDrought : TestExtension() {
    override val name = "OneFieldTestTrialIrrigatingAfterDrought"
    override val description = "Tests if expected results are wrong"

    override val farms = "onefieldtest/farms.json"
    override val scenario = "onefieldtest/scenario.json"
    override val map = "onefieldtest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 19

    override suspend fun run() {
        skipUntilString(Logs.DROUGHT)
        assertCurrentLine(Logs.DROUGHT)

        skipUntilString(Logs.IRRIGATION)
        assertCurrentLine(Logs.IRRIGATION)
    }
}

