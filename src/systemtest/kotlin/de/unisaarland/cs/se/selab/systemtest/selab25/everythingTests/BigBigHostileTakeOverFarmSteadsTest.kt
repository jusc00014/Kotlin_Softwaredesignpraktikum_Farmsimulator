package de.unisaarland.cs.se.selab.systemtest.selab25.everythingTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.SimulationTestExtension

/**
 * detekt haha*/
class BigBigHostileTakeOverFarmSteadsTest : SimulationTestExtension(
    "bigBigTest",
    "bigBigTestMap.json",
    "bigBigHostileTakeOverFarmSteadsFarms.json",
    "bigBigTestScenarioOctober1.json"
) {
    override val description = "Hail Marry to find the stupid Mutant"
    override val startYearTick = 19
    override val maxTicks = 1

    override val logLevel = "DEBUG"

    override suspend fun run() {
        val expectedLine = "[INFO] Initialization Info: bigBigTestMap.json successfully parsed and validated."
        skipUntilLogType(LogLevel.INFO, "Initialization Info")
        assertCurrentLine(expectedLine)
        assertNextLine(
            "[IMPORTANT] Initialization Info: bigBigHostileTakeOverFarmSteadsFarms.json is invalid."
        )
    }
}
