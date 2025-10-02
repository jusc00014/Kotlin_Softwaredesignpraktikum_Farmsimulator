package de.unisaarland.cs.se.selab.systemtest.selab25.machineMutantsHunt

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * D
 */
class MachineTest7 : TestExtension() {
    override val name = "HalfAYear"
    override val description = "Tests farm/machine behavior."

    override val farms = "machineMutantsHunt/farms5.json"
    override val scenario = "machineMutantsHunt/scenario.json"
    override val map = "machineMutantsHunt/map6.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 12
    override val startYearTick = 6

    val str1 = "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles."
    val str2 = "[IMPORTANT] Farm: Farm 0 starts its actions."
    val str3 = "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 0."
    val str4 = "[IMPORTANT] Farm: Farm 0 finished its actions."
    val str5 = "[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 1."
    val str6 = "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: ."
    val str7 = "[IMPORTANT] Farm Action: Machine 0 performs WEEDING on tile 0 for 1 days."

    override suspend fun run() {
        first()
        second()
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 0 changed to 478296 g of POTATO.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 52531 g of APPLE.")
        assertNextLine("[INFO] Simulation Info: Tick 9 started at tick 15 within the year.")
        assertNextLine(str1)
        assertNextLine(str2)
        assertNextLine(str6)
        assertNextLine(str7)
        assertNextLine(str5)
        assertNextLine(str4)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 34464 g of APPLE.")
        assertNextLine("[INFO] Simulation Info: Tick 10 started at tick 16 within the year.")
        assertNextLine(str1)
        assertNextLine(str2)
        assertNextLine(str6)
        assertNextLine(str4)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 22610 g of APPLE.")
        assertNextLine("[INFO] Simulation Info: Tick 11 started at tick 17 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 1 FIELD and 0 PLANTATION tiles.")
        assertNextLine(str2)
        assertNextLine(str6)
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs HARVESTING on tile 3 for 1 days.")
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 0 has collected 22610 g of APPLE harvest.")
        assertNextLine(str5)
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 unloads 22610 g of APPLE harvest in the shed.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 2 performs HARVESTING on tile 0 for 1 days.")
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 2 has collected 478296 g of POTATO harvest.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 1.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 2 unloads 478296 g of POTATO harvest in the shed.")
        assertNextLine(str4)
        assertNextLine("[IMPORTANT] Simulation Info: Simulation ended at tick 12.")
        assertNextLine("[IMPORTANT] Simulation Info: Simulation statistics are calculated.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Farm 0 collected 500906 g of harvest.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of POTATO harvested: 478296 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of WHEAT harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of OAT harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of PUMPKIN harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of APPLE harvested: 22610 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of GRAPE harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of ALMOND harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of CHERRY harvested: 0 g.")
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 0 g."
        )
    }
    suspend fun first() {
        skipUntilLogType(LogLevel.INFO, "Simulation Info:")
        assertCurrentLine("[INFO] Simulation Info: Simulation started at tick 6 within the year.")
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 6 within the year.")
        assertNextLine(str1)
        assertNextLine(str2)
        assertNextLine(str3)
        assertNextLine(str4)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 1239300 g of APPLE.")
        assertNextLine("[INFO] Simulation Info: Tick 1 started at tick 7 within the year.")
        assertNextLine(str1)
        assertNextLine(str2)
        assertNextLine(str3)
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 0 for 1 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 0 has sowed POTATO according to sowing plan 0.")
        assertNextLine(str5)
        assertNextLine(str4)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 903449 g of APPLE.")
        assertNextLine("[INFO] Simulation Info: Tick 2 started at tick 8 within the year.")
        assertNextLine(str1)
        assertNextLine(str2)
        assertNextLine(str6)
        assertNextLine(str4)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 658613 g of APPLE.")
        assertNextLine("[INFO] Simulation Info: Tick 3 started at tick 9 within the year.")
        assertNextLine(str1)
        assertNextLine(str2)
        assertNextLine(str6)
        assertNextLine(str7)
        assertNextLine(str5)
        assertNextLine(str4)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 0 changed to 900000 g of POTATO.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 432114 g of APPLE.")
        assertNextLine("[INFO] Simulation Info: Tick 4 started at tick 10 within the year.")
        assertNextLine(str1)
        assertNextLine(str2)
        assertNextLine(str6)
        assertNextLine(str4)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 0 changed to 810000 g of POTATO.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 283508 g of APPLE.")
        assertNextLine("[INFO] Simulation Info: Tick 5 started at tick 11 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 1 FIELD and 0 PLANTATION tiles.")
        assertNextLine(str2)
        assertNextLine(str6)
    }
    suspend fun second() {
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs IRRIGATING on tile 0 for 1 days.")
        assertNextLine(str5)
        assertNextLine("[IMPORTANT] Farm Action: Machine 2 performs MOWING on tile 3 for 1 days.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 1.")
        assertNextLine(str4)
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 0 were not performed: WEEDING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 0 changed to 656100 g of POTATO.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 186008 g of APPLE.")
        assertNextLine("[INFO] Simulation Info: Tick 6 started at tick 12 within the year.")
        assertNextLine(str1)
        assertNextLine(str2)
        assertNextLine(str6)
        assertNextLine(str4)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 0 changed to 590490 g of POTATO.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 122039 g of APPLE.")
        assertNextLine("[INFO] Simulation Info: Tick 7 started at tick 13 within the year.")
        assertNextLine(str1)
        assertNextLine(str2)
        assertNextLine(str6)
        assertNextLine(str7)
        assertNextLine(str5)
        assertNextLine(str4)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 0 changed to 531441 g of POTATO.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 3 changed to 80068 g of APPLE.")
        assertNextLine("[INFO] Simulation Info: Tick 8 started at tick 14 within the year.")
        assertNextLine(str1)
        assertNextLine(str2)
        assertNextLine(str6)
        assertNextLine(str4)
    }
}
