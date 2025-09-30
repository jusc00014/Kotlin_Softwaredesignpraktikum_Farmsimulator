package de.unisaarland.cs.se.selab.systemtest.selab25.havestTest

import de.unisaarland.cs.se.selab.farms.Action
import de.unisaarland.cs.se.selab.plants.PlantType
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.SimulationTestExtension

const val HARVEST_ESTIMATE = "Harvest Estimate"

/**
 * tests all plants harvest estimates without outside influence from november on*/
class LateHarvestTest : SimulationTestExtension("lateHarvesting") {
    override val map = mapFileName
    override val farms = farmsFileName
    override val scenario = scenarioFileName

    override val description = "tests late harvest estimates on all plants"
    override val startYearTick = 21
    override val maxTicks = 24

    override suspend fun run() {
        skipUntilLogType(LogLevel.IMPORTANT, "Farm")
        assertCurrentLine(farmStartActions(1))
        assertNextLine(farmSowingPlans(1, listOf(0, 1, 2, 3)))
        assertNextLine(machinePerformAction(3, Action.SOWING, 2, 7))
        skipUntilLogType(LogLevel.INFO, HARVEST_ESTIMATE) // Nov_1
        assertCurrentLine(harvestEstimate(2, 1_200_000, PlantType.WHEAT))
        assertNextLine(harvestEstimate(5, 1_200_000, PlantType.GRAPE))
        assertNextLine(harvestEstimate(6, 800_000, PlantType.ALMOND))
        assertNextLine(harvestEstimate(7, 1_530_000, PlantType.APPLE))
        assertNextLine(harvestEstimate(8, 1_200_000, PlantType.CHERRY))
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
        assertCurrentLine(harvestEstimate(7, 813_104, PlantType.APPLE))
        skipUntilLogType(LogLevel.INFO, HARVEST_ESTIMATE) // Feb_1
        assertCurrentLine(harvestEstimate(7, 658_614, PlantType.APPLE))
        skipUntilLogType(LogLevel.DEBUG, HARVEST_ESTIMATE) // Feb_2
        assertCurrentLine(actionNotPerformed(6, listOf(Action.CUTTING)))
        assertNextLine(harvestEstimate(6, 400_000, PlantType.ALMOND))
        assertNextLine(actionNotPerformed(7, listOf(Action.CUTTING)))
        assertNextLine(harvestEstimate(7, 266_738, PlantType.APPLE))
        assertNextLine(actionNotPerformed(8, listOf(Action.CUTTING)))
        assertNextLine(harvestEstimate(8, 600_000, PlantType.CHERRY))
        skipUntilLogType(LogLevel.INFO, HARVEST_ESTIMATE) // Mar_1 126 sunlight is pretty high
    }
}
