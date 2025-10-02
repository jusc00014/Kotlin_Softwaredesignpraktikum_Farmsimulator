package de.unisaarland.cs.se.selab.systemtest.selab25.validationTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.SimulationTestExtension
/**Meadow*/
class IllegalMeadowCityExpansionTest : SimulationTestExtension(
    "ValidationTests",
    scenarioFileName = "meadowScenario.json"
) {
    override val description = "Illegal City Expansion onto Meadow Tile"
    override val maxTicks = 0
    override val startYearTick = 21

    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, "Initialization Info")
        assertCurrentLine("[INFO] Initialization Info: map.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: farms.json successfully parsed and validated.")
        assertNextLine("[IMPORTANT] Initialization Info: meadowScenario.json is invalid.")
    }
}
