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
    override val maxTicks = 32
    override val startYearTick = 1

    override suspend fun run() {
        skipUntilLogType(LogLevel.IMPORTANT, "Incident")
        assertCurrentLine("[IMPORTANT] Incident: Incident 1 of type BROKEN_MACHINE happened and affected tiles 0.")
        skipUntilLogType(LogLevel.IMPORTANT, "Incident")
        assertCurrentLine("[IMPORTANT] Incident: Incident 0 of type BROKEN_MACHINE happened and affected tiles 0.")
        skipUntilLogType(LogLevel.INFO, "Simulation Info: Tick 31")
        assertCurrentLine("[INFO] Simulation Info: Tick 31 started at tick 8 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 1."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 1 for 14 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 0 has sowed POTATO according to sowing plan 1.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0.")

        skipUntilLogType(LogLevel.IMPORTANT, "Simulation Statistics")
        assertCurrentLine("[IMPORTANT] Simulation Statistics: Farm 0 collected 0 g of harvest.")
        listOf("POTATO", "WHEAT", "OAT", "PUMPKIN", "APPLE", "GRAPE", "ALMOND", "CHERRY").forEach { plant ->
            assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of $plant harvested: 0 g.")
        }
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 1000000 g."
        )
    }
}
