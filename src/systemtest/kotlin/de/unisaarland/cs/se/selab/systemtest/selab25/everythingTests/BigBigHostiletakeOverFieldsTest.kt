package de.unisaarland.cs.se.selab.systemtest.selab25.everythingTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.SimulationTestExtension

/**
 * funny */
class BigBigHostiletakeOverFieldsTest : SimulationTestExtension(
    "bigBigTest",
    "bigBigTestMap.json",
    "bigBigHostileTakeOverFieldsFarms.json",
    "bigBigTestScenarioOctober1.json"
) {
    override val description = "Hail Marry to find the stupid Mutant"
    override val startYearTick = 19
    override val maxTicks = 1

    override suspend fun run() {
        val expectedLine = "[INFO] Initialization Info: bigBigTestMap.json successfully parsed and validated."
        skipUntilLogType(LogLevel.INFO, "Initialization Info")
        assertCurrentLine(expectedLine)
        assertNextLine(
            "[IMPORTANT] Initialization Info: bigBigHostileTakeOverFieldsFarms.json is invalid."
        )
    }
}
