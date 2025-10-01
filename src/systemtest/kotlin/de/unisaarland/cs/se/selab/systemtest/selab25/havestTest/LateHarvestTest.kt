package de.unisaarland.cs.se.selab.systemtest.selab25.havestTest

import de.unisaarland.cs.se.selab.farms.Action
import de.unisaarland.cs.se.selab.plants.PlantType
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.SimulationTestExtension

const val HARVEST_ESTIMATE = "Harvest Estimate"
const val FARM = "Farm"

/**
 * tests all plants harvest estimates without outside influence from november on*/
class LateHarvestTest : SimulationTestExtension("lateHarvesting") {
    override val description = "tests late harvest estimates on all plants"
    override val startYearTick = 21
    override val maxTicks = 24

    override suspend fun run() {
        skipUntilLogType(LogLevel.IMPORTANT, FARM)
        assertCurrentLine(farmStartActions(1))
        assertNextLine(farmSowingPlans(1, listOf(0, 1, 2, 3)))
        assertNextLine(machinePerformAction(3, Action.SOWING, 2, 7))
        skipUntilLogType(LogLevel.INFO, HARVEST_ESTIMATE) // Nov_1
        assertCurrentLine(harvestEstimate(2, 1_200_000, PlantType.WHEAT))
        assertNextLine(harvestEstimate(7, 1_530_000, PlantType.APPLE))
        skipUntilLogType(LogLevel.INFO, HARVEST_ESTIMATE) // Nov_2
        assertCurrentLine(harvestEstimate(7, 1_377_000, PlantType.APPLE))
        skipUntilLogType(LogLevel.INFO, HARVEST_ESTIMATE) // Dec_1
        assertCurrentLine(harvestEstimate(7, 1_239_300, PlantType.APPLE))
        skipUntilLogType(LogLevel.DEBUG, HARVEST_ESTIMATE) // Dec_2
        assertCurrentLine(actionNotPerformed(2, listOf(Action.WEEDING)))
        assertNextLine(harvestEstimate(2, 1_080_000, PlantType.WHEAT))
        assertNextLine(harvestEstimate(7, 1_115_370, PlantType.APPLE))
        skipUntilLogType(LogLevel.INFO, HARVEST_ESTIMATE) // Jan_1
        assertCurrentLine(harvestEstimate(7, 1_003_833, PlantType.APPLE))
        skipUntilLogType(LogLevel.INFO, HARVEST_ESTIMATE) // Jan_2
        assertCurrentLine(harvestEstimate(7, 903_449, PlantType.APPLE))
        skipUntilLogType(LogLevel.INFO, HARVEST_ESTIMATE) // Feb_1
        assertCurrentLine(harvestEstimate(7, 731_793, PlantType.APPLE))
        skipUntilLogType(LogLevel.DEBUG, HARVEST_ESTIMATE) // Feb_2
        assertCurrentLine(actionNotPerformed(6, listOf(Action.CUTTING)))
        assertNextLine(harvestEstimate(6, 400_000, PlantType.ALMOND))
        assertNextLine(actionNotPerformed(7, listOf(Action.CUTTING)))
        assertNextLine(harvestEstimate(7, 296_375, PlantType.APPLE))
        assertNextLine(actionNotPerformed(8, listOf(Action.CUTTING)))
        assertNextLine(harvestEstimate(8, 600_000, PlantType.CHERRY))
        skipUntilLogType(LogLevel.INFO, HARVEST_ESTIMATE) // Mar_1 126 sunlight is pretty high
        assertCurrentLine(harvestEstimate(2, 972_000, PlantType.WHEAT))
        assertNextLine(harvestEstimate(7, 216_058, PlantType.APPLE))
        skipUntilLogType(LogLevel.IMPORTANT, FARM) // Mar_2
        assertCurrentLine(farmStartActions(1))
        assertNextLine(farmSowingPlans(1, listOf(0, 2, 3)))
        assertNextLine(machinePerformAction(0, Action.SOWING, 3, 7))
        skipUntilLogType(LogLevel.DEBUG, HARVEST_ESTIMATE)
        assertCurrentLine(actionNotPerformed(2, listOf(Action.WEEDING)))
        assertNextLine(harvestEstimate(2, 787_320, PlantType.WHEAT))
        assertNextLine(harvestEstimate(3, 1_080_000, PlantType.OAT))
        assertNextLine(harvestEstimate(7, 157_506, PlantType.APPLE))
        skipUntilLogType(LogLevel.IMPORTANT, FARM) // Apr 1
        assertCurrentLine(farmStartActions(1))
        assertNextLine(farmSowingPlans(1, listOf(2, 3)))
        assertNextLine(machinePerformAction(1, Action.SOWING, 1, 7))
        skipUntilLogType(LogLevel.INFO, HARVEST_ESTIMATE)
        assertCurrentLine(harvestEstimate(1, 1_000_000, PlantType.POTATO))
        assertNextLine(harvestEstimate(2, 637_729, PlantType.WHEAT))
        assertNextLine(actionNotPerformed(3, listOf(Action.WEEDING)))
        assertNextLine(harvestEstimate(3, 787_320, PlantType.OAT))
        assertNextLine(actionNotPerformed(5, listOf(Action.MOWING)))
        assertNextLine(harvestEstimate(5, 1_080_000, PlantType.GRAPE))
        assertNextLine(harvestEstimate(7, 114_821, PlantType.APPLE))
        skipUntilLogType(LogLevel.INFO, HARVEST_ESTIMATE)
        assertCurrentLine(harvestEstimate(2, 516_560, PlantType.WHEAT))
        assertNextLine(harvestEstimate(3, 637_729, PlantType.OAT))
        assertNextLine(harvestEstimate(7, 75_332, PlantType.APPLE))
    }
}
