package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Tests if the farm has at least 1 plantation/field left after city expansions
 */
class InvalidCityExpansionNoFieldLeftTest : TestExtension() {
    override val name = "InvalidCityExpansionNoFieldLeftTest"
    override val description = "Tests if the farm has at least 1 plantation/field left after city expansions"

    override val map = "scenarioParser/mapNoFieldForFarm.json"
    override val farms = "scenarioParser/farms.json"
    override val scenario = "scenarioParser/invalidCityScenario.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 24
    override val startYearTick = 1

    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, "Initialization Info")
        assertCurrentLine("[INFO] Initialization Info: mapNoFieldForFarm.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: farms.json successfully parsed and validated.")
        assertNextLine("[IMPORTANT] Initialization Info: invalidCityScenario.json is invalid.")
    }
}
