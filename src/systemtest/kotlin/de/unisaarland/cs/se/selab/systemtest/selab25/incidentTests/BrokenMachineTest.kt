package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Detekt
 */
class BrokenMachineTest : TestExtension() {
    override val name = "BrokenMachine"
    override val description = "Tests BrokenDuration."

    override val farms = "incidentTest/farms32.json"
    override val scenario = "incidentTest/scenarioBrokenMachine.json"
    override val map = "incidentTest/map3.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 5
    override val startYearTick = 7

    override suspend fun run() {
        val harvestEstimate = "Harvest Estimate"
        skipUntilLogType(LogLevel.IMPORTANT, "Farm Sowing:")
        assertCurrentLine("[IMPORTANT] Farm Sowing: Machine 1 has sowed POTATO according to sowing plan 1.")
        skipUntilLogType(LogLevel.IMPORTANT, "Incident:")
        assertCurrentLine("[IMPORTANT] Incident: Incident 0 of type BROKEN_MACHINE happened and affected tiles 3.")
        skipUntilLogType(LogLevel.INFO, "Simulation Info: Tick 2")
        skipUntilLogType(LogLevel.DEBUG, harvestEstimate)
        assertCurrentLine("[DEBUG] Harvest Estimate: Required actions on tile 4 were not performed: WEEDING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 4 changed to 810000 g of POTATO.")
        skipUntilLogType(LogLevel.INFO, harvestEstimate)
        assertCurrentLine("[INFO] Harvest Estimate: Harvest estimate on tile 4 changed to 729000 g of POTATO.")
        skipUntilLogType(LogLevel.IMPORTANT, "Farm Action")
        assertCurrentLine("[IMPORTANT] Farm Action: Machine 1 performs WEEDING on tile 4 for 10 days.")
        skipUntilLogType(LogLevel.INFO, harvestEstimate)
        assertCurrentLine("[INFO] Harvest Estimate: Harvest estimate on tile 4 changed to 656100 g of POTATO.")
        skipUntilLogType(LogLevel.IMPORTANT, "Simulation Statistics: Total harvest estimate still")
        assertCurrentLine(
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 656100 g."
        )
    }
}
