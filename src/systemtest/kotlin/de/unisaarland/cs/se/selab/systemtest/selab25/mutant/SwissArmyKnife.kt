package de.unisaarland.cs.se.selab.systemtest.selab25.mutant

import de.unisaarland.cs.se.selab.Constants.MAY_1
import de.unisaarland.cs.se.selab.farms.Action
import de.unisaarland.cs.se.selab.plants.PlantType
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.SimulationTestExtension

/**
 * g
 */
class SwissArmyKnife : SimulationTestExtension(folder = "mutantSwissArmyKnife") {
    override val description: String = ""
    override val startYearTick: Int = MAY_1
    private val farmID = 0
    private val sowingPlanID = 0
    private val sowingPlanIDs = listOf(sowingPlanID)
    private val machineID = 0
    private val duration = 12
    private val fieldPumpkin = 4
    private val shedID = 0

    override suspend fun run() {
        skipUntilString("[IMPORTANT] Farm: Farm $farmID starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm $farmID has the following active sowing plans " +
                "it intends to pursue in this tick: ${sowingPlanIDs.joinToString(",")}."
        )
        assertNextLine(
            "[IMPORTANT] Farm Action: Machine $machineID performs ${Action.SOWING} " +
                "on tile $fieldPumpkin for $duration days."
        )
        assertNextLine(
            "[IMPORTANT] Farm Sowing: Machine $machineID has sowed ${PlantType.PUMPKIN} " +
                "according to sowing plan $sowingPlanID."
        )
        assertNextLine("[IMPORTANT] Farm Machine: Machine $machineID is finished and returns to the shed at $shedID.")
        assertNextLine("[IMPORTANT] Farm: Farm $farmID finished its actions.")
    }
}
