package de.unisaarland.cs.se.selab.systemtest.selab25.machineMutantsHunt

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * D
 */
class MachineTest6 : TestExtension() {
    override val name = "SowDroughtIrrigate"
    override val description = "Tests machine behavior."

    override val farms = "machineMutantsHunt/farms4.json"
    override val scenario = "machineMutantsHunt/scenario2.json"
    override val map = "machineMutantsHunt/map5.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 3
    override val startYearTick = 6

    override suspend fun run() {
        val str1 = "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles."
        val str2 = "[IMPORTANT] Farm: Farm 0 starts its actions."
        val str3 = "[IMPORTANT] Farm: Farm 0 finished its actions."
        val str4 = "[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 1."
        skipUntilLogType(LogLevel.INFO, "Simulation Info:")
        assertCurrentLine("[INFO] Simulation Info: Simulation started at tick 6 within the year.")
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 6 within the year.")
        assertNextLine(str1)
        assertNextLine(str2)
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 0,1."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 2 for 1 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 0 has sowed OAT according to sowing plan 1.")
        assertNextLine(str4)
        assertNextLine(str3)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 2 changed to 1080000 g of OAT.")
        assertNextLine("[INFO] Simulation Info: Tick 1 started at tick 7 within the year.")
        assertNextLine(str1)
        assertNextLine(str2)
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 0."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 0 for 1 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 0 has sowed POTATO according to sowing plan 0.")
        assertNextLine(str4)
        assertNextLine("[IMPORTANT] Farm Action: Machine 2 performs WEEDING on tile 2 for 1 days.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 1.")
        assertNextLine(str3)
        assertNextLine("[IMPORTANT] Incident: Incident 0 of type DROUGHT happened and affected tiles 0.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 0 changed to 0 g of POTATO.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 2 changed to 874800 g of OAT.")
        assertNextLine("[INFO] Simulation Info: Tick 2 started at tick 8 within the year.")
        assertNextLine(str1)
        assertNextLine(str2)
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs WEEDING on tile 2 for 1 days.")
        assertNextLine(str4)
        assertNextLine(str3)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 2 changed to 708588 g of OAT.")
        assertNextLine("[IMPORTANT] Simulation Info: Simulation ended at tick 3.")
    }
}
