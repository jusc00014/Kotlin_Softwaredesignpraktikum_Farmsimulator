package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Detekt
 */
class VillageCreationTestChained : TestExtension() {
    override val name = "ChainedVillages"
    override val description = "Tests validVillageChains."

    override val farms = "incidentTest/farms3.json"
    override val scenario = "incidentTest/scenarioChainedVillage.json"
    override val map = "incidentTest/map3.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 10

    override suspend fun run() {
        skipUntilLogType(LogLevel.IMPORTANT, "Incident")
        assertCurrentLine("[IMPORTANT] Incident: Incident 100 of type CITY_EXPANSION happened and affected tiles 1.")
        assertNextLine(
            "[IMPORTANT] Incident: Incident 101 of type CITY_EXPANSION happened and affected tiles 2."
        )
    }
}
