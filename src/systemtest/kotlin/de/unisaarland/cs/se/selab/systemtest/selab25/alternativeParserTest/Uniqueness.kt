package de.unisaarland.cs.se.selab.systemtest.selab25.alternativeParserTest

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Test
 */
class SameMachineId : TestExtension() {
    override val name = "Same machine ID"
    override val description = Strgs.DESC

    override val farms = "alternativeParserTest/farm5.json"
    override val scenario = Strgs.SCENAAR
    override val map = Strgs.MAAP

    override val logLevel = Strgs.DBG
    override val maxTicks = 1
    override val startYearTick = 19

    override suspend fun run() {
        skipUntilString(
            Strgs.STR
        )
        assertCurrentLine(
            Strgs.STR
        )
    }
}

/**
 * Test
 */
class SameMachineName : TestExtension() {
    override val name = "Same machine name"
    override val description = Strgs.DESC

    override val farms = "alternativeParserTest/farm6.json"
    override val scenario = Strgs.SCENAAR
    override val map = Strgs.MAAP

    override val logLevel = Strgs.DBG
    override val maxTicks = 1
    override val startYearTick = 19

    override suspend fun run() {
        skipUntilString(
            Strgs.STR
        )
        assertCurrentLine(
            Strgs.STR
        )
    }
}

/**
 * Test
 */
class SamePlanId : TestExtension() {
    override val name = "Same plan ID"
    override val description = Strgs.DESC

    override val farms = "alternativeParserTest/farm7.json"
    override val scenario = Strgs.SCENAAR
    override val map = Strgs.MAAP

    override val logLevel = Strgs.DBG
    override val maxTicks = 1
    override val startYearTick = 19

    override suspend fun run() {
        skipUntilString(
            Strgs.STR
        )
        assertCurrentLine(
            Strgs.STR
        )
    }
}

/**
 * Test
 */
class SowingPlanDoesTheSame : TestExtension() {
    override val name = "Two sowing plans do the same"
    override val description = Strgs.DESC

    override val farms = "alternativeParserTest/farm8.json"
    override val scenario = Strgs.SCENAAR
    override val map = Strgs.MAAP

    override val logLevel = Strgs.DBG
    override val maxTicks = 1
    override val startYearTick = 19

    override suspend fun run() {
        skipUntilString(
            "[INFO] Initialization Info: farm8.json successfully parsed and validated."
        )
    }
}
