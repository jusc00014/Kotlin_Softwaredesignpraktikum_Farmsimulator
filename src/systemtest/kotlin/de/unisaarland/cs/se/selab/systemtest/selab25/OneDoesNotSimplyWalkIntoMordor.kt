package de.unisaarland.cs.se.selab.systemtest.selab25

import de.unisaarland.cs.se.selab.Constants
import de.unisaarland.cs.se.selab.farms.Action
import de.unisaarland.cs.se.selab.plants.PlantType
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.SimulationTestExtension

/**
 * Machine not sowing, as other Farm Plantation in the way
 */
class OneDoesNotSimplyWalkIntoMordor : SimulationTestExtension(
    "OneDoesNotSimplyWalkIntoMordor",
) {
    override val maxTicks: Int = 1
    override val description: String = "Machine not sowing, as other Farm Plantation in the way"
    override val startYearTick: Int = Constants.APR_2
    private val farm1 = 1
    private val farm2 = 2
    private val machineFarm1 = 1
    private val sowingPlan = 1
    private val shedFarm1 = 10
    private val fieldFarm1Potato1 = 11

    override suspend fun run() {
        skipUntilString(tickStarted(0, Constants.APR_2))
        assertNextLine(soilMoisture(0, 0))
        assertNextLine(farmStartActions(farm1))
        assertNextLine(farmSowingPlans(farm1, listOf(sowingPlan)))
        assertNextLine(machinePerformAction(machineFarm1, Action.SOWING, fieldFarm1Potato1, 1))
        assertNextLine(machineSowed(machineFarm1, PlantType.POTATO, sowingPlan))
        assertNextLine(machineReturnShed(machineFarm1, shedFarm1))
        assertNextLine(farmFinishedActions(farm1))
        assertNextLine(farmStartActions(farm2))
        assertNextLine(farmSowingPlans(farm2, emptyList()))
        assertNextLine(farmFinishedActions(farm2))
    }
}
