package de.unisaarland.cs.se.selab.systemtest.selab25.plantTests

import de.unisaarland.cs.se.selab.farms.Action
import de.unisaarland.cs.se.selab.plants.PlantType
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.SimulationTestExtension

/**
 * Tests apples
 */
class BadAppleDetailedTest : SimulationTestExtension(
    "plantTests",
    farmsFileName = "farmsBadAppleHarvest.json",
    mapFileName = "mapBadAppleLowMoisture.json"
) {
    override val name = "BadAppleDetailedTest"
    override val description = "Tests what an apple is and how it works."

    override val logLevel = "DEBUG"
    override val maxTicks = 24
    override val startYearTick = 21
    val estimateMap = mapOf(
        1 to 1377000,
        2 to 1239300,
        3 to 1115370,
        4 to 1003833,
        5 to 903449,
        6 to 731793,
        7 to 592751,
        8 to 432114,
        9 to 315009,
        10 to 229641,
        11 to 167407,
        12 to 109835,
        13 to 72061,
        14 to 47277,
        15 to 31017,
        16 to 20349,
        17 to 13349,
        18 to 8757,
        19 to 5743
    )

    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, "Simulation Info")
        assertCurrentLine("[INFO] Simulation Info: Simulation started at tick 21 within the year.")
        assertNextLine(tickStarted(0, 21))
        assertNextLine(soilMoisture(0, 0))
        assertNextLine(farmStartActions(0))
        assertNextLine(farmSowingPlans(0, emptyList()))
        assertNextLine(machinePerformAction(0, Action.CUTTING, 1, 1))
        assertNextLine(machineReturnShed(0, 0))
        assertNextLine(farmFinishedActions(0))
        assertNextLine(harvestEstimate(1, 1530000, PlantType.APPLE))
        loop(1, 2)
        assertNextLine(tickStarted(3, 24))
        assertNextLine(soilMoisture(0, 1))
        assertNextLine(farmStartActions(0))
        assertNextLine(farmSowingPlans(0, emptyList()))
        assertNextLine(machinePerformAction(0, Action.IRRIGATING, 1, 1))
        assertNextLine(machineReturnShed(0, 0))
        assertNextLine(farmFinishedActions(0))
        assertNextLine(harvestEstimate(1, estimateMap[3] ?: 0, PlantType.APPLE))
        loop(4, 6)
        assertNextLine(tickStarted(7, 4))
        assertNextLine(soilMoisture(0, 1))
        assertNextLine(farmStartActions(0))
        assertNextLine(farmSowingPlans(0, emptyList()))
        assertNextLine(machinePerformAction(0, Action.IRRIGATING, 1, 1))
        assertNextLine(machineReturnShed(0, 0))
        assertNextLine(farmFinishedActions(0))
        assertNextLine(harvestEstimate(1, estimateMap[7] ?: 0, PlantType.APPLE))
        loop(8, 10)
        assertNextLine(tickStarted(11, 8))
        assertNextLine(soilMoisture(0, 1))
        assertNextLine(farmStartActions(0))
        assertNextLine(farmSowingPlans(0, emptyList()))
        assertNextLine(machinePerformAction(0, Action.IRRIGATING, 1, 1))
        assertNextLine(machineReturnShed(0, 0))
        assertNextLine(farmFinishedActions(0))
        assertNextLine(harvestEstimate(1, estimateMap[11] ?: 0, PlantType.APPLE))
        loop(12, 13)
        midApple()
        loop(16, 18)
        lateApple()
        loopNoHarvestChange(21, 22)

        skipUntilLogType(LogLevel.IMPORTANT, "Simulation Statistics")
        assertCurrentLine("[IMPORTANT] Simulation Statistics: Farm 0 collected 5743 g of harvest.")
        listOf("POTATO", "WHEAT", "OAT", "PUMPKIN").forEach { plant ->
            assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of $plant harvested: 0 g.")
        }
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of APPLE harvested: 5743 g.")
        listOf("GRAPE", "ALMOND", "CHERRY").forEach { plant ->
            assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of $plant harvested: 0 g.")
        }
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 0 g."
        )
    }

    private suspend fun loop(start: Int, end: Int) {
        for (i in start..end) {
            assertNextLine(tickStarted(i, (20 + i) % 24 + 1))
            assertNextLine(soilMoisture(0, 0))
            assertNextLine(farmStartActions(0))
            assertNextLine(farmSowingPlans(0, emptyList()))
            assertNextLine(farmFinishedActions(0))
            assertNextLine(harvestEstimate(1, estimateMap[i] ?: 0, PlantType.APPLE))
        }
    }

    private suspend fun loopNoHarvestChange(start: Int, end: Int) {
        for (i in start..end) {
            assertNextLine(tickStarted(i, (20 + i) % 24 + 1))
            assertNextLine(soilMoisture(0, 0))
            assertNextLine(farmStartActions(0))
            assertNextLine(farmSowingPlans(0, emptyList()))
            assertNextLine(farmFinishedActions(0))
        }
    }

    private suspend fun midApple() {
        assertNextLine(tickStarted(14, 11))
        assertNextLine(soilMoisture(0, 0))
        assertNextLine(farmStartActions(0))
        assertNextLine(farmSowingPlans(0, emptyList()))
        assertNextLine(machinePerformAction(0, Action.MOWING, 1, 1))
        assertNextLine(machineReturnShed(0, 0))
        assertNextLine(farmFinishedActions(0))
        assertNextLine(harvestEstimate(1, estimateMap[14] ?: 0, PlantType.APPLE))
        assertNextLine(tickStarted(15, 12))
        assertNextLine(soilMoisture(0, 1))
        assertNextLine(farmStartActions(0))
        assertNextLine(farmSowingPlans(0, emptyList()))
        assertNextLine(machinePerformAction(0, Action.IRRIGATING, 1, 1))
        assertNextLine(machineReturnShed(0, 0))
        assertNextLine(farmFinishedActions(0))
        assertNextLine(harvestEstimate(1, estimateMap[15] ?: 0, PlantType.APPLE))
    }

    private suspend fun lateApple() {
        assertNextLine(tickStarted(19, 16))
        assertNextLine(soilMoisture(0, 1))
        assertNextLine(farmStartActions(0))
        assertNextLine(farmSowingPlans(0, emptyList()))
        assertNextLine(machinePerformAction(0, Action.IRRIGATING, 1, 1))
        assertNextLine(machineReturnShed(0, 0))
        assertNextLine(farmFinishedActions(0))
        assertNextLine(harvestEstimate(1, estimateMap[19] ?: 0, PlantType.APPLE))

        assertNextLine(tickStarted(20, 17))
        assertNextLine(soilMoisture(0, 0))
        assertNextLine(farmStartActions(0))
        assertNextLine(farmSowingPlans(0, emptyList()))
        assertNextLine(machinePerformAction(0, Action.HARVESTING, 1, 1))
        assertNextLine(machineCollectedHarvest(0, 5743, PlantType.APPLE))
        assertNextLine(machineReturnShed(0, 0))
        assertNextLine(machineUnloaded(0, 5743, PlantType.APPLE))
        assertNextLine(farmFinishedActions(0))
    }
}
