package de.unisaarland.cs.se.selab.systemtest.selab25.onefieldtest

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

private object Logs {
    const val SOWING =
        "[IMPORTANT] Farm Sowing: Machine 3 has sowed WHEAT according to sowing plan 1."
    const val DROUGHT =
        "[IMPORTANT] Incident: Incident 1 of type DROUGHT happened and affected tiles 38."
    const val MOISTURE =
        "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles."
    const val IRRIGATION =
        "[IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 38 for 10 days."
    const val mapp = "onefieldtest/map.json"
    const val scenarioo = "onefieldtest/scenario.json"
    const val farmss = "onefieldtest/farms.json"
    const val debu = "DEBUG"
}

/**
 * Test of the expected results
 */
class OneFieldTestTrialMoisture : TestExtension() {
    override val name = "OneFieldTestTrialMoisture"
    override val description = "Tests moisture after drought"

    override val farms = Logs.farmss
    override val scenario = Logs.scenarioo
    override val map = Logs.mapp

    override val logLevel = Logs.debu
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
    override val description = "Tests sowing WHEAT at 18"

    override val farms = Logs.farmss
    override val scenario = Logs.scenarioo
    override val map = Logs.mapp

    override val logLevel = Logs.debu
    override val maxTicks = 2
    override val startYearTick = 18

    override suspend fun run() {
        skipUntilString(Logs.SOWING)
        assertCurrentLine(Logs.SOWING)
    }
}

/**
 * Test of the expected results
 */
class OneFieldTestTrialSowingTime19 : TestExtension() {
    override val name = "OneFieldTestTrialSowingTime19"
    override val description = "Tests sowing WHEAT at 19"

    override val farms = Logs.farmss
    override val scenario = Logs.scenarioo
    override val map = Logs.mapp

    override val logLevel = Logs.debu
    override val maxTicks = 2
    override val startYearTick = 19

    override suspend fun run() {
        skipUntilString(Logs.SOWING)
        assertCurrentLine(Logs.SOWING)
    }
}

/**
 * Test of the expected results
 */
class OneFieldTestTrialSowingTime20 : TestExtension() {
    override val name = "OneFieldTestTrialSowingTime20"
    override val description = "Tests Sowing WHEAT at 20"

    override val farms = Logs.farmss
    override val scenario = Logs.scenarioo
    override val map = Logs.mapp

    override val logLevel = Logs.debu
    override val maxTicks = 2
    override val startYearTick = 20

    override suspend fun run() {
        skipUntilString(Logs.SOWING)
        assertCurrentLine(Logs.SOWING)
    }
}

/**
 * Test of the expected results
 */
class OneFieldTestTrialSowingTime21 : TestExtension() {
    override val name = "OneFieldTestTrialSowingTime21"
    override val description = "Tests sowing WHEAT at 21"

    override val farms = Logs.farmss
    override val scenario = Logs.scenarioo
    override val map = Logs.mapp

    override val logLevel = Logs.debu
    override val maxTicks = 2
    override val startYearTick = 21

    override suspend fun run() {
        skipUntilString(Logs.SOWING)
        assertCurrentLine(Logs.SOWING)
    }
}

/**
 * Test of the expected results
 */
class OneFieldTestTrialSowingTime22 : TestExtension() {
    override val name = "OneFieldTestTrialSowingTime22"
    override val description = "Tests sowing WHEAT at 22"

    override val farms = Logs.farmss
    override val scenario = Logs.scenarioo
    override val map = Logs.mapp

    override val logLevel = Logs.debu
    override val maxTicks = 2
    override val startYearTick = 22

    override suspend fun run() {
        skipUntilString(Logs.SOWING)
        assertCurrentLine(Logs.SOWING)
    }
}

/**
 * Test of the expected results
 */
class OneFieldTestTrialSowingTime17 : TestExtension() {
    override val name = "OneFieldTestTrialSowingTime17"
    override val description = "Tests Sowing WHEAT at 17"

    override val farms = Logs.farmss
    override val scenario = Logs.scenarioo
    override val map = Logs.mapp

    override val logLevel = Logs.debu
    override val maxTicks = 2
    override val startYearTick = 17

    override suspend fun run() {
        skipUntilString(Logs.SOWING)
        assertCurrentLine(Logs.SOWING)
    }
}

/**
 * Test of the expected results
 */
class OneFieldTestTrialIrrigatingAfterDrought : TestExtension() {
    override val name = "OneFieldTestTrialIrrigatingAfterDrought"
    override val description = "Tests irrigating after drought"

    override val farms = Logs.farmss
    override val scenario = Logs.scenarioo
    override val map = Logs.mapp

    override val logLevel = Logs.debu
    override val maxTicks = 2
    override val startYearTick = 19

    override suspend fun run() {
        skipUntilString(Logs.DROUGHT)
        assertCurrentLine(Logs.DROUGHT)
    }
}

/**
 * Test of the expected results
 */
class OneFieldTestTrialIrrigating : TestExtension() {
    override val name = "OneFieldTestTrialIrrigating"
    override val description = "Tests irrigating"

    override val farms = Logs.farmss
    override val scenario = Logs.scenarioo
    override val map = Logs.mapp

    override val logLevel = Logs.debu
    override val maxTicks = 2
    override val startYearTick = 19

    override suspend fun run() {
        skipUntilString(Logs.IRRIGATION)
        assertCurrentLine(Logs.IRRIGATION)
    }
}
