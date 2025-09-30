package de.unisaarland.cs.se.selab.systemtest.selab25.sowingPlanTest

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Tests machine travel through villages and forests
 */
class SowingPlanTest : TestExtension() {
    override val name = "SowingPlanTest"
    override val description = "Tests sowing Plan execution."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "sowingPlanTest/farms.json"
    override val scenario = "sowingPlanTest/scenario.json"
    override val map = "sowingPlanTest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 24
    override val startYearTick = 13

    val pumpkinPlan = "[IMPORTANT] Farm Sowing: Machine 3 has sowed PUMPKIN according to sowing plan 3."
    val potatoPlan = "[IMPORTANT] Farm Sowing: Machine 0 has sowed POTATO according to sowing plan 0."
    val oatPlan = "[IMPORTANT] Farm Sowing: Machine 1 has sowed OAT according to sowing plan 1."
    val wheatPlan = "[IMPORTANT] Farm Sowing: Machine 2 has sowed WHEAT according to sowing plan 2."
    val farmAction = "Farm Action"

    override suspend fun run() {
        skipUntilLogType(LogLevel.IMPORTANT, farmAction)
        assertCurrentLine("[IMPORTANT] Farm Action: Machine 3 performs SOWING on tile 10 for 5 days.")
        assertNextLine(pumpkinPlan)
        assertNextLine("[IMPORTANT] Farm Action: Machine 3 performs SOWING on tile 11 for 5 days.")
        assertNextLine(pumpkinPlan)
        assertNextLine("[IMPORTANT] Farm Machine: Machine 3 is finished and returns to the shed at 100.")

        skipUntilLogType(LogLevel.IMPORTANT, farmAction)
        assertCurrentLine("[IMPORTANT] Farm Action: Machine 2 performs SOWING on tile 7 for 5 days.")
        assertNextLine(wheatPlan)
        assertNextLine("[IMPORTANT] Farm Action: Machine 2 performs SOWING on tile 8 for 5 days.")
        assertNextLine(wheatPlan)
        assertNextLine("[IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 100.")

        skipUntilLogType(LogLevel.IMPORTANT, farmAction)
        assertCurrentLine("[IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 4 for 5 days.")
        assertNextLine(oatPlan)
        assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 5 for 5 days.")
        assertNextLine(oatPlan)
        assertNextLine("[IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 100.")

        skipUntilLogType(LogLevel.IMPORTANT, farmAction)
        assertCurrentLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 1 for 5 days.")
        assertNextLine(potatoPlan)
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 2 for 5 days.")
        assertNextLine(potatoPlan)
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 100.")

        skipUntilLogType(LogLevel.IMPORTANT, "Simulation Statistics")
        assertCurrentLine("[IMPORTANT] Simulation Statistics: Farm 0 collected 0 g of harvest.")
        listOf("POTATO", "WHEAT", "OAT", "PUMPKIN", "APPLE", "GRAPE", "ALMOND", "CHERRY").forEach { plant ->
            assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of $plant harvested: 0 g.")
        }
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 1759910 g."
        )
    }
}
