package de.unisaarland.cs.se.selab.systemtest.selab25.everythingTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.SimulationTestExtension

const val CLOUD_MOVES_TO_98 = "[INFO] Cloud Movement: Cloud 1 with 4000 L water moved from tile 99 to tile 98."
const val CLOUD_MOVES_TO_99 = "[INFO] Cloud Movement: Cloud 1 with 4000 L water moved from tile 98 to tile 99."
const val SUNLIGHT_REDUCTION_99 = "[DEBUG] Cloud Movement: On tile 99, the amount of sunlight is "
const val SUNLIGHT_REDUCTION_98 = "[DEBUG] Cloud Movement: On tile 98, the amount of sunlight is "

/**
 * tests a lot of behavior in October1 (all phases) */
class BigBigTestOctober1 : SimulationTestExtension(
    "bigBigTest",
    "bigBigTestMap.json",
    "bigBigTestFarms.json",
    "bigBigTestScenarioOctober1.json"
) {
    override val name = "BigBigTestOctober1"
    override val description = "tests all phases for tick 19"

    override val map = "bigBigTest/bigBigTestMap.json"
    override val farms = "bigBigTest/bigBigTestFarms.json"
    override val scenario = "bigBigTest/bigBigTestScenarioOctober1.json"
    override val startYearTick = 19
    override val maxTicks = 1
    override val logLevel = "DEBUG"

    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, "Simulation Info")
        assertCurrentLine("[INFO] Simulation Info: Simulation started at tick 19 within the year.")
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 19 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")
        assertNextLine(CLOUD_MOVES_TO_98)
        assertNextLine(SUNLIGHT_REDUCTION_99 + "109.")
        for (i in 112 downTo 97 step 3) {
            assertNextLine(CLOUD_MOVES_TO_98)
            assertNextLine(SUNLIGHT_REDUCTION_99 + "$i")
            assertNextLine(CLOUD_MOVES_TO_99)
            assertNextLine(SUNLIGHT_REDUCTION_98 + "$i")
        }
        assertNextLine("[DEBUG] Cloud Position: Cloud 1 is on tile 99, where the amount of sunlight is 47.")

    }
}
