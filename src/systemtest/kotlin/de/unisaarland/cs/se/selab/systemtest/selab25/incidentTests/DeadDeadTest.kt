package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.SimulationTestExtension

const val SOIL_MOISTURE = "Soil Moisture"

/**evil*/
class DeadDeadTest : SimulationTestExtension("deadDeadTest") {
    override val description = "dead Plantation, no actions"
    override val maxTicks = 24
    override val startYearTick = 16

    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, SOIL_MOISTURE)
        assertCurrentLine(soilMoisture(0, 1))
        assertNextLine(farmStartActions(1))
        assertNextLine(farmSowingPlans(1, emptyList()))
        assertNextLine(farmFinishedActions(1))
        assertNextLine(incidentOccured(1, "DROUGHT", listOf(1)))

        for (i in 17..24) {
            val x = 1
            assertNextLine(tickStarted(x, i))
            skipUntilLogType(LogLevel.INFO, SOIL_MOISTURE)
            assertCurrentLine(soilMoisture(0, 0))
            assertNextLine(farmStartActions(1))
            assertNextLine(farmSowingPlans(1, emptyList()))
            assertNextLine(farmFinishedActions(1))
        }

        for (i in 1..16) {
            val x = 9
            assertNextLine(tickStarted(x, i))
            skipUntilLogType(LogLevel.INFO, SOIL_MOISTURE)
            assertCurrentLine(soilMoisture(0, 0))
            assertNextLine(farmStartActions(1))
            assertNextLine(farmSowingPlans(1, emptyList()))
            assertNextLine(farmFinishedActions(1))
        }
    }
}
