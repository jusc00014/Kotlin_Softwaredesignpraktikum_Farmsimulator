package de.unisaarland.cs.se.selab.systemtest.selab25.cloudtests

import de.unisaarland.cs.se.selab.board.APRIL
import de.unisaarland.cs.se.selab.board.AUGUST
import de.unisaarland.cs.se.selab.board.DECEMBER
import de.unisaarland.cs.se.selab.board.EIGHT
import de.unisaarland.cs.se.selab.board.ELEVEN
import de.unisaarland.cs.se.selab.board.FEBRUARY_AND_OCTOBER
import de.unisaarland.cs.se.selab.board.FIVE
import de.unisaarland.cs.se.selab.board.FOUR
import de.unisaarland.cs.se.selab.board.JANUARY_AND_NOVEMBER
import de.unisaarland.cs.se.selab.board.MARCH_AND_SEPTEMBER
import de.unisaarland.cs.se.selab.board.MAY_AND_JUNE_AND_JULY
import de.unisaarland.cs.se.selab.board.NINE
import de.unisaarland.cs.se.selab.board.SEVEN
import de.unisaarland.cs.se.selab.board.SIX
import de.unisaarland.cs.se.selab.board.TEN
import de.unisaarland.cs.se.selab.board.TWELVE
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Detekt
 */
class ClimateChange : TestExtension() {
    override val name = "SunlightTest"
    override val description = "Tests Sunlight over a year."

    override val farms = "incidentTest/farms3.json"
    override val scenario = "cloudTest/scenario4.json"
    override val map = "incidentTest/map3.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 24
    override val startYearTick = 1

    override suspend fun run() {
        val sunLight: Map<Int, Int> = mapOf(
            1 to JANUARY_AND_NOVEMBER,
            2 to FEBRUARY_AND_OCTOBER,
            3 to MARCH_AND_SEPTEMBER,
            FOUR to APRIL,
            FIVE to MAY_AND_JUNE_AND_JULY,
            SIX to MAY_AND_JUNE_AND_JULY,
            SEVEN to MAY_AND_JUNE_AND_JULY,
            EIGHT to AUGUST,
            NINE to MARCH_AND_SEPTEMBER,
            TEN to FEBRUARY_AND_OCTOBER,
            ELEVEN to JANUARY_AND_NOVEMBER,
            TWELVE to DECEMBER
        )
        for (i in 1..24) {
            var light = sunLight[(i + 1) / 2] ?: error("Cant happen1000")
            light -= 50
            skipUntilLogType(LogLevel.DEBUG, "Cloud Position")
            assertCurrentLine(
                "[DEBUG] Cloud Position: Cloud 0 is on tile 4, where the amount of sunlight is $light."
            )
        }
    }
}
