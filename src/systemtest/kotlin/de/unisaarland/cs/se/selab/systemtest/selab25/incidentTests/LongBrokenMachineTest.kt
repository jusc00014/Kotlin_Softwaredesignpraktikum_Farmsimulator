package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Tests if the machine stays broken for the whole duration
 */
class LongBrokenMachineTest : TestExtension() {
    override val name = "LongBrokenMachine"
    override val description = "Tests the duration of broken machine."

    override val farms = "incidentTest/farmsSmall.json"
    override val scenario = "incidentTest/longBrokenMachine.json"
    override val map = "incidentTest/mapSmall.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 31
    override val startYearTick = 1

    override suspend fun run() {
        skipUntilLogType(LogLevel.IMPORTANT, "Simulation Statistics")
        assertCurrentLine("[IMPORTANT] Simulation Statistics: Farm 0 collected 0 g of harvest.")
        listOf("POTATO", "WHEAT", "OAT", "PUMPKIN", "APPLE", "GRAPE", "ALMOND", "CHERRY").forEach { plant ->
            assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of $plant harvested: 0 g.")
        }
    }
}
