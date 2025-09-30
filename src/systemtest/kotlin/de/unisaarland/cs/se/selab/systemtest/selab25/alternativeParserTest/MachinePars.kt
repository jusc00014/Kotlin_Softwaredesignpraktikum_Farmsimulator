package de.unisaarland.cs.se.selab.systemtest.selab25.alternativeParserTest

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Strings
 */
object Strgs {
    const val STR = "[INFO] Initialization Info: farms.json successfully parsed and validated."
    const val DESC = "Tests if this is valid"
    const val MAAP = "alternativeParserTest/map3.json"
    const val SCENAAR = "alternativeParserTest/noscenarios.json"
    const val DBG = "DEBUG"
}

/**
 * Test
 */
class MachineCanTReach : TestExtension() {
    override val name = "Machien can not reach"
    override val description = Strgs.DESC

    override val farms = "alternativeParserTest/farm1.json"
    override val scenario = Strgs.SCENAAR
    override val map = "alternativeParserTest/map1.json"

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
class IDsNotUnique : TestExtension() {
    override val name = "IDs not unique among all farms"
    override val description = Strgs.DESC

    override val farms = "alternativeParserTest/farm2.json"
    override val scenario = Strgs.SCENAAR
    override val map = "alternativeParserTest/map2.json"

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
