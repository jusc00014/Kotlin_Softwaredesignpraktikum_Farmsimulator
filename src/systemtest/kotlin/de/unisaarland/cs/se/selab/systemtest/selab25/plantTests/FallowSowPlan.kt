package de.unisaarland.cs.se.selab.systemtest.selab25.plantTests

import de.unisaarland.cs.se.selab.Constants
import de.unisaarland.cs.se.selab.farms.Action
import de.unisaarland.cs.se.selab.plants.PlantType
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.SimulationTestExtension

/**
 *
 */
abstract class FallowSowPlanTestExtension(mapFileName: String, farmsFileName: String) : SimulationTestExtension(
    "plantTests",
    mapFileName = mapFileName,
    farmsFileName = farmsFileName,
    scenarioFileName = "scenarioFallowT0.json"
) {
    protected suspend fun assertSowing(plant: PlantType, sowingPlantID: Int) {
        assertNextLine(farmStartActions(0))
        assertNextLine(farmSowingPlans(0, listOf(sowingPlantID)))
        assertNextLine(machinePerformAction(0, Action.SOWING, 1, 1))
        assertNextLine(machineSowed(0, plant, sowingPlantID))
        assertNextLine(machineReturnShed(0, 0))
        assertNextLine(farmFinishedActions(0))
    }
}

/**
 * Late sowing after Fallow of Potato
 */
class FallowSowPlanPotato : FallowSowPlanTestExtension(
    mapFileName = "mapField.json",
    farmsFileName = "farmsFallowSowPlanPotato.json",
) {
    override val maxTicks: Int = 6
    override val startYearTick: Int = Constants.APR_1
    override val description: String = "Sowing after Fallow of Potato"

    override suspend fun run() {
        // region Tick 0
        skipUntilString(tickStarted(0, startYearTick))
        // moisture: 930
        assertNextLine(soilMoisture(0, 0))
        assertSowing(PlantType.POTATO, 0)
        assertNextLine(incidentOccured(0, "DROUGHT", listOf(1)))
        assertNextLine(harvestEstimate(1, 0, PlantType.POTATO))
        // endregion Tick 0
        // Tick 1: moisture: 860
        // Tick 2: moisture: 790
        // Tick 3: moisture: 720
        // Tick 4: moisture: 650
        // region Tick 5
        skipUntilString(tickStarted(5, Constants.JUN_2))
        assertNextLine(soilMoisture(0, 0))
        assertSowing(PlantType.POTATO, 1)
        // Init: 1'000'000
        // Sow Late (2x 80%): 640'000
        // Sunlight (max:130h, current:168h) (90%, 1x): 576'000
        // Moisture (min:500L, current: 580L) (-50g, 0x): 576'000
        assertNextLine(actionNotPerformed(1, listOf(Action.IRRIGATING)))
        assertNextLine(harvestEstimate(1, 0, PlantType.POTATO))
        // endregion Tick 5
        assertNextLine(simulationEnd(5))
        assertNextLine(statisticsCalculated)
        assertNextLine(statisticFarmCollected(0, 0))
        assertStatisticPlantHarvested(mapOf(PlantType.POTATO to 0))
        assertNextLine(statisticHarvestLeft(0))
        assertEnd()
    }
}

/**
 * Late sowing after Fallow of Wheat
 */
class FallowSowPlanWheat : SimulationTestExtension(
    "plantTests",
    farmsFileName = "farmsFallowSowPlanWheat.json"
) {
    override val maxTicks: Int = 11
    override val startYearTick: Int = Constants.APR_1
    override val description: String = "Sowing after Fallow of Wheat"
    override suspend fun run() {
        TODO("Not yet implemented")
    }
}
