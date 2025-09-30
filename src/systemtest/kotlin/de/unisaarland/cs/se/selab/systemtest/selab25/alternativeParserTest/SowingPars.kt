package de.unisaarland.cs.se.selab.systemtest.selab25.alternativeParserTest

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Test
 */
class FieldOfOtherFarm : TestExtension() {
    override val name = "Sowing plan only contains other farms field"
    override val description = Strgs.DESC

    override val farms = "alternativeParserTest/farm3.json"
    override val scenario = Strgs.SCENAAR
    override val map = "alternativeParserTest/map3.json"

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
class SowingAndVillage : TestExtension() {
    override val name = "Sowing plan only contains nonvillage of other farms"
    override val description = Strgs.DESC

    override val farms = "alternativeParserTest/farm4.json"
    override val scenario = "alternativeParserTest/scenario4.json"
    override val map = "alternativeParserTest/map4.json"

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
class MachineWrongPlant : TestExtension() {
    override val name = "MachineWrongPlant"
    override val description = Strgs.DESC

    override val farms = "alternativeParserTest/farm1.json"
    override val scenario = Strgs.SCENAAR
    override val map = "alternativeParserTest/map9.json"

    override val logLevel = Strgs.DBG
    override val maxTicks = 1
    override val startYearTick = 19

    override suspend fun run() {
        skipUntilString(
            "[INFO] Initialization Info: farms1 successfully parsed and validated."
        )
        assertCurrentLine(
            "[INFO] Initialization Info: farms1 successfully parsed and validated."
        )
    }
}

/**
 * Test
 */
class MachineCanTSow : TestExtension() {
    override val name = "MachineCantSow"
    override val description = Strgs.DESC

    override val farms = "alternativeParserTest/farm9.json"
    override val scenario = Strgs.SCENAAR
    override val map = "alternativeParserTest/map9.json"

    override val logLevel = Strgs.DBG
    override val maxTicks = 1
    override val startYearTick = 19

    override suspend fun run() {
        skipUntilString(
            "[INFO] Initialization Info: farms9 successfully parsed and validated."
        )
        assertCurrentLine(
            "[INFO] Initialization Info: farms9 successfully parsed and validated."
        )
    }
}
