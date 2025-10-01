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
        november()
        december()
        january()
        february()
        march()
        april()
        may()
        june()
        july()
        august()
    }

    suspend fun november() {
        skipUntilLogType(LogLevel.INFO, HARVEST_ESTIMATE) // Nov_1
        assertCurrentLine(harvestEstimate(2, 1_200_000, PlantType.WHEAT))
        assertNextLine(harvestEstimate(7, 1_530_000, PlantType.APPLE))
        skipUntilLogType(LogLevel.INFO, HARVEST_ESTIMATE) // Nov_2
        assertCurrentLine(harvestEstimate(7, 1_377_000, PlantType.APPLE))
    }

    suspend fun december() {
        skipUntilLogType(LogLevel.INFO, HARVEST_ESTIMATE) // Dec_1
        assertCurrentLine(harvestEstimate(7, 1_239_300, PlantType.APPLE))
        skipUntilLogType(LogLevel.DEBUG, HARVEST_ESTIMATE) // Dec_2
        assertCurrentLine(actionNotPerformed(2, listOf(Action.WEEDING)))
        assertNextLine(harvestEstimate(2, 1_080_000, PlantType.WHEAT))
        assertNextLine(harvestEstimate(7, 1_115_370, PlantType.APPLE))
    }

    suspend fun january() {
        skipUntilLogType(LogLevel.INFO, HARVEST_ESTIMATE) // Jan_1
        assertCurrentLine(harvestEstimate(7, 1_003_833, PlantType.APPLE))
        skipUntilLogType(LogLevel.INFO, HARVEST_ESTIMATE) // Jan_2
        assertCurrentLine(harvestEstimate(7, 903_449, PlantType.APPLE))
    }

    suspend fun february() {
        skipUntilLogType(LogLevel.INFO, HARVEST_ESTIMATE) // Feb_1
        assertCurrentLine(harvestEstimate(7, 731_793, PlantType.APPLE))
        skipUntilLogType(LogLevel.DEBUG, HARVEST_ESTIMATE) // Feb_2
        assertCurrentLine(actionNotPerformed(6, listOf(Action.CUTTING)))
        assertNextLine(harvestEstimate(6, 400_000, PlantType.ALMOND))
        assertNextLine(actionNotPerformed(7, listOf(Action.CUTTING)))
        assertNextLine(harvestEstimate(7, 296_375, PlantType.APPLE))
        assertNextLine(actionNotPerformed(8, listOf(Action.CUTTING)))
        assertNextLine(harvestEstimate(8, 600_000, PlantType.CHERRY))
    }

    suspend fun march() {
        skipUntilLogType(LogLevel.INFO, HARVEST_ESTIMATE) // Mar_1 126 sunlight is pretty high
        assertCurrentLine(harvestEstimate(2, 972_000, PlantType.WHEAT))
        assertNextLine(harvestEstimate(7, 216_056, PlantType.APPLE))
        skipUntilLogType(LogLevel.IMPORTANT, FARM) // Mar_2
        assertCurrentLine(farmStartActions(1))
        assertNextLine(farmSowingPlans(1, listOf(0, 2, 3)))
        assertNextLine(machinePerformAction(0, Action.SOWING, 3, 7))
        skipUntilLogType(LogLevel.DEBUG, HARVEST_ESTIMATE)
        assertCurrentLine(actionNotPerformed(2, listOf(Action.WEEDING)))
        assertNextLine(harvestEstimate(2, 787_320, PlantType.WHEAT))
        assertNextLine(harvestEstimate(3, 1_080_000, PlantType.OAT))
        assertNextLine(harvestEstimate(7, 157_504, PlantType.APPLE))
    }

    suspend fun april() {
        skipUntilLogType(LogLevel.IMPORTANT, FARM) // Apr 1
        assertCurrentLine(farmStartActions(1))
        assertNextLine(farmSowingPlans(1, listOf(2, 3)))
        assertNextLine(machinePerformAction(1, Action.SOWING, 1, 7))
        skipUntilLogType(LogLevel.INFO, HARVEST_ESTIMATE)
        assertCurrentLine(harvestEstimate(2, 637_729, PlantType.WHEAT))
        assertNextLine(actionNotPerformed(3, listOf(Action.WEEDING)))
        assertNextLine(harvestEstimate(3, 787_320, PlantType.OAT))
        assertNextLine(actionNotPerformed(5, listOf(Action.MOWING)))
        assertNextLine(harvestEstimate(5, 1_080_000, PlantType.GRAPE))
        assertNextLine(harvestEstimate(7, 114_819, PlantType.APPLE))
        skipUntilLogType(LogLevel.INFO, HARVEST_ESTIMATE) // Apr 2
        assertCurrentLine(harvestEstimate(2, 516_560, PlantType.WHEAT))
        assertNextLine(actionNotPerformed(3, listOf(Action.WEEDING)))
        assertNextLine(harvestEstimate(3, 573_956, PlantType.OAT))
        assertNextLine(harvestEstimate(7, 83_702, PlantType.APPLE))
    }

    suspend fun may() {
        skipUntilLogType(LogLevel.DEBUG, HARVEST_ESTIMATE) // May 1, 168h sunlight
        // potato needs weeding (missed)
        assertCurrentLine(actionNotPerformed(1, listOf(Action.WEEDING)))
        assertNextLine(harvestEstimate(1, 810_000, PlantType.POTATO))
        assertNextLine(harvestEstimate(2, 376_571, PlantType.WHEAT))
        // oat needs weeding (missed)
        assertNextLine(actionNotPerformed(3, listOf(Action.WEEDING)))
        assertNextLine(harvestEstimate(3, 376_571, PlantType.OAT))
        assertNextLine(harvestEstimate(6, 360_000, PlantType.ALMOND))
        assertNextLine(harvestEstimate(7, 54_915, PlantType.APPLE))
        assertNextLine(harvestEstimate(8, 540_000, PlantType.CHERRY))
        skipUntilLogType(LogLevel.IMPORTANT, FARM) // May 2
        assertCurrentLine(farmStartActions(1))
        assertNextLine(farmSowingPlans(1, listOf(3)))
        assertNextLine(machinePerformAction(2, Action.SOWING, 0, 7))
        skipUntilLogType(LogLevel.INFO, HARVEST_ESTIMATE)
        assertCurrentLine(harvestEstimate(0, 450_000, PlantType.PUMPKIN))
        assertNextLine(harvestEstimate(1, 729_000, PlantType.POTATO))
        assertNextLine(harvestEstimate(2, 274_518, PlantType.WHEAT))
        assertNextLine(harvestEstimate(3, 274_518, PlantType.OAT))
        assertNextLine(harvestEstimate(6, 324_000, PlantType.ALMOND))
        assertNextLine(harvestEstimate(7, 36_028, PlantType.APPLE))
        assertNextLine(harvestEstimate(8, 486_000, PlantType.CHERRY))
    }

    suspend fun june() {
        skipUntilLogType(LogLevel.INFO, HARVEST_ESTIMATE) // Jun_1
        assertCurrentLine(harvestEstimate(0, 405_000, PlantType.PUMPKIN))
        assertNextLine(actionNotPerformed(1, listOf(Action.WEEDING)))
        assertNextLine(harvestEstimate(1, 590_490, PlantType.POTATO))
        assertNextLine(harvestEstimate(2, 200_123, PlantType.WHEAT))
        assertNextLine(harvestEstimate(3, 200_123, PlantType.OAT))
        assertNextLine(actionNotPerformed(6, listOf(Action.MOWING)))
        assertNextLine(harvestEstimate(6, 262_440, PlantType.ALMOND))
        assertNextLine(actionNotPerformed(7, listOf(Action.MOWING)))
        assertNextLine(harvestEstimate(7, 21_272, PlantType.APPLE))
        assertNextLine(actionNotPerformed(8, listOf(Action.MOWING)))
        assertNextLine(harvestEstimate(8, 393_660, PlantType.CHERRY))
        skipUntilLogType(LogLevel.DEBUG, HARVEST_ESTIMATE) // Jun 2
        assertCurrentLine(actionNotPerformed(0, listOf(Action.WEEDING)))
        assertNextLine(harvestEstimate(0, 328_050, PlantType.PUMPKIN))
        assertNextLine(harvestEstimate(1, 531_441, PlantType.POTATO))
        assertNextLine(harvestEstimate(2, 145_889, PlantType.WHEAT))
        assertNextLine(harvestEstimate(3, 145_889, PlantType.OAT))
        assertNextLine(harvestEstimate(6, 236_196, PlantType.ALMOND))
        assertNextLine(harvestEstimate(7, 13_955, PlantType.APPLE))
        assertNextLine(harvestEstimate(8, 354_294, PlantType.CHERRY))
    }

    suspend fun july() {
        skipUntilLogType(LogLevel.INFO, HARVEST_ESTIMATE) // july 1
        assertCurrentLine(harvestEstimate(0, 295_245, PlantType.PUMPKIN))
        assertNextLine(actionNotPerformed(1, listOf(Action.WEEDING)))
        assertNextLine(harvestEstimate(1, 430_466, PlantType.POTATO))
        assertNextLine(actionNotPerformed(2, listOf(Action.HARVESTING)))
        assertNextLine(harvestEstimate(2, 85_082, PlantType.WHEAT))
        assertNextLine(harvestEstimate(3, 106_353, PlantType.OAT))
        assertNextLine(actionNotPerformed(5, listOf(Action.MOWING)))
        assertNextLine(harvestEstimate(5, 972_000, PlantType.GRAPE))
        assertNextLine(harvestEstimate(6, 212_576, PlantType.ALMOND))
        assertNextLine(harvestEstimate(7, 9_154, PlantType.APPLE))
        assertNextLine(harvestEstimate(8, 318_864, PlantType.CHERRY))
        skipUntilLogType(LogLevel.DEBUG, HARVEST_ESTIMATE) // july 2
        assertCurrentLine(actionNotPerformed(0, listOf(Action.WEEDING)))
        assertNextLine(harvestEstimate(0, 239_148, PlantType.PUMPKIN))
        assertNextLine(harvestEstimate(1, 387_419, PlantType.POTATO))
        assertNextLine(actionNotPerformed(2, listOf(Action.HARVESTING)))
        assertNextLine(harvestEstimate(2, 49_618, PlantType.WHEAT))
        assertNextLine(harvestEstimate(3, 77_530, PlantType.OAT))
        assertNextLine(harvestEstimate(6, 191_318, PlantType.ALMOND))
        assertNextLine(harvestEstimate(7, 6_004, PlantType.APPLE))
        assertNextLine(actionNotPerformed(8, listOf(Action.HARVESTING)))
        assertNextLine(harvestEstimate(8, 86_093, PlantType.CHERRY))
    }

    suspend fun august() {
        skipUntilLogType(LogLevel.INFO, HARVEST_ESTIMATE) // Aug 1, 154h
        assertCurrentLine(harvestEstimate(0, 215_233, PlantType.PUMPKIN))
        assertNextLine(actionNotPerformed(1, listOf(Action.WEEDING)))
        assertNextLine(harvestEstimate(1, 348_677, PlantType.POTATO))
        assertNextLine(actionNotPerformed(2, listOf(Action.HARVESTING)))
        assertNextLine(harvestEstimate(2, 0, PlantType.WHEAT))
        assertNextLine(harvestEstimate(3, 62_799, PlantType.OAT))
        assertNextLine(harvestEstimate(7, 3_937, PlantType.APPLE))
        assertNextLine(actionNotPerformed(8, listOf(Action.HARVESTING)))
        assertNextLine(harvestEstimate(8, 0, PlantType.CHERRY))
        skipUntilLogType(LogLevel.DEBUG, HARVEST_ESTIMATE)
        assertCurrentLine(actionNotPerformed(0, listOf(Action.WEEDING)))
        assertNextLine(harvestEstimate(0, 174_338, PlantType.PUMPKIN))
        assertNextLine(harvestEstimate(3, 45_780, PlantType.OAT))
        assertNextLine(actionNotPerformed(5, listOf(Action.CUTTING)))
        assertNextLine(harvestEstimate(5, 486_000, PlantType.GRAPE))
    }
}
