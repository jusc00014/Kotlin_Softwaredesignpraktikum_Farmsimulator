package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Tests if the machine stays broken for the whole duration
 */
class VeryLongBrokenMachineTest : TestExtension() {
    override val name = "VeryLongBrokenMachine"
    override val description = "Tests if -1 duration of broken machine is overwritten."

    override val farms = "incidentTest/farmsSmall.json"
    override val scenario = "incidentTest/veryLongBrokenMachine.json"
    override val map = "incidentTest/mapSmall.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 32
    override val startYearTick = 1

    val farmStart = "[IMPORTANT] Farm: Farm 0 starts its actions."
    val farmCurrentPlans =
        "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 1,2."
    val farmFinish = "[IMPORTANT] Farm: Farm 0 finished its actions."
    val soilMoist = "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles."

    override suspend fun run() {
        skipUntilLogType(LogLevel.IMPORTANT, "Incident")
        assertCurrentLine("[IMPORTANT] Incident: Incident 1 of type BROKEN_MACHINE happened and affected tiles 0.")
        skipUntilLogType(LogLevel.IMPORTANT, "Incident")
        assertCurrentLine("[IMPORTANT] Incident: Incident 0 of type BROKEN_MACHINE happened and affected tiles 0.")
        skipUntilLogType(LogLevel.INFO, "Simulation Info: Tick 6")
        assertCurrentLine("[INFO] Simulation Info: Tick 6 started at tick 7 within the year.")
        assertNextLine(soilMoist)
        assertNextLine(farmStart)
        assertNextLine(farmCurrentPlans)
        assertNextLine(farmFinish)

        skipUntilLogType(LogLevel.INFO, "Simulation Info: Tick 31")
        assertCurrentLine("[INFO] Simulation Info: Tick 31 started at tick 8 within the year.")
        assertNextLine(soilMoist)
        assertNextLine(farmStart)
        assertNextLine(farmCurrentPlans)
        assertNextLine(farmFinish)

        skipUntilLogType(LogLevel.IMPORTANT, "Simulation Statistics")
        assertCurrentLine("[IMPORTANT] Simulation Statistics: Farm 0 collected 0 g of harvest.")
        listOf("POTATO", "WHEAT", "OAT", "PUMPKIN", "APPLE", "GRAPE", "ALMOND", "CHERRY").forEach { plant ->
            assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of $plant harvested: 0 g.")
        }
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 0 g."
        )
    }
}
