package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Detekt
 */
class VillageCreationTestNextToForest : TestExtension() {
    override val name = "VillageCreationTestInvalid"
    override val description = "Tests VillageCreationIncident ParsingValidation."

    override val farms = "incidentTest/farms.json"
    override val scenario = "incidentTest/scenarioVillageCreation.json"
    override val map = "incidentTest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 1

    override suspend fun run() {
        val expectedLine = "[INFO] Initialization Info: map.json successfully parsed and validated."
        skipUntilLogType(LogLevel.INFO, "Initialization Info")
        assertCurrentLine(expectedLine)
        assertNextLine("[INFO] Initialization Info: farms.json successfully parsed and validated.")
        assertNextLine("[IMPORTANT] Initialization Info: scenarioVillageCreation.json is invalid.")
    }
}
