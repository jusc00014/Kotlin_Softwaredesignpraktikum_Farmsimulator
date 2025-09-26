package de.unisaarland.cs.se.selab.systemtest.selab25.mutant

import de.unisaarland.cs.se.selab.Constants
import de.unisaarland.cs.se.selab.farms.Action
import de.unisaarland.cs.se.selab.plants.PlantType
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.SimulationTestExtension

/**
 * Machine with everything only sowing 1 Pumpkin
 */
class SwissArmyKnife : SimulationTestExtension(folder = "mutantSwissArmyKnife") {
    override val description: String = "Machine with everything only sowing 1 Pumpkin"
    override val startYearTick: Int = Constants.MAY_2
    private val farmID = 0
    private val sowingPlanID = 0
    private val machineID = 0
    private val duration = 12
    private val fieldPumpkin = 4
    private val shedID = 0

    override suspend fun run() {
        skipUntilString(farmStartActions(farmID))
        assertNextLine(farmSowingPlans(farmID, listOf(sowingPlanID)))
        assertNextLine(machinePerformAction(machineID, Action.SOWING, fieldPumpkin, duration))
        assertNextLine(machineSowed(machineID, PlantType.PUMPKIN, sowingPlanID))
        assertNextLine(machineReturnShed(machineID, shedID))
        assertNextLine(farmFinishedActions(farmID))
    }
}
