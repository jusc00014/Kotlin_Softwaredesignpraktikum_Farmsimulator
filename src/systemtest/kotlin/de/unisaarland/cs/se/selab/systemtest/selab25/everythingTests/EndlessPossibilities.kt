package de.unisaarland.cs.se.selab.systemtest.selab25.everythingTests

import de.unisaarland.cs.se.selab.farms.Action
import de.unisaarland.cs.se.selab.plants.PlantType
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.SimulationTestExtension
/**
 * */
class EndlessPossibilities : SimulationTestExtension("endlessPossibilities") {
    override val description = "a lot in 3 ticks"

    override val maxTicks = 2
    override val startYearTick = 10

    override suspend fun run() {
        skipUntilLogType(LogLevel.IMPORTANT, "Farm")
        assertCurrentLine(farmStartActions(1))
        assertNextLine(farmSowingPlans(1, listOf(0, 1, 2, 3)))
        assertNextLine(machinePerformAction(0, Action.SOWING, 1, 4))
        assertNextLine(machineSowed(0, PlantType.POTATO, 0))
        assertNextLine(machineReturnShed(0, 0))
        assertNextLine(machinePerformAction(1, Action.SOWING, 5, 3))
        assertNextLine(machineSowed(1, PlantType.PUMPKIN, 2))
        assertNextLine(machineReturnShed(1, 0))
        assertNextLine(farmFinishedActions(1))

        skipUntilLogType(LogLevel.IMPORTANT, "Farm")
        assertCurrentLine(farmStartActions(1))
        assertNextLine(farmSowingPlans(1, listOf(0, 2)))
        assertNextLine(machinePerformAction(0, Action.IRRIGATING, 1, 4))
        assertNextLine(machinePerformAction(0, Action.IRRIGATING, 2, 4))
        assertNextLine(machinePerformAction(0, Action.IRRIGATING, 3, 4))
        assertNextLine(machineReturnShed(0, 0))
        assertNextLine(machinePerformAction(1, Action.IRRIGATING, 5, 3))
        assertNextLine(machinePerformAction(1, Action.IRRIGATING, 4, 3))
        assertNextLine(machineReturnShed(1, 0))
        assertNextLine(farmFinishedActions(1))
    }
}
