package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.plants.PlantType
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.SimulationTestExtension

/**
 * Tests 2 Droughts on same tile
 */
class DroughtTest : SimulationTestExtension(
    "incidentTest",
    scenarioFileName = "scenarioDrought.json",
) {
    override val description = "Tests Drought on same tile."

    override suspend fun run() {
        skipUntilString(farmFinishedActions(1))
        assertNextLine(incidentOccured(0, "DROUGHT", listOf(5, 6, 12)))
        assertNextLine(incidentOccured(1, "DROUGHT", emptyList()))
        assertNextLine(harvestEstimate(5, 0, PlantType.APPLE))
    }
}
