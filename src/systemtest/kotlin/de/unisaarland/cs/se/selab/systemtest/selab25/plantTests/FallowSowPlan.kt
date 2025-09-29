package de.unisaarland.cs.se.selab.systemtest.selab25.plantTests

import de.unisaarland.cs.se.selab.Constants
import de.unisaarland.cs.se.selab.farms.Action
import de.unisaarland.cs.se.selab.plants.PlantType
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.SimulationTestExtension

/**
 *
 */
abstract class FallowSowPlanTestExtension(farmsFileName: String) : SimulationTestExtension(
    "plantTests",
    farmsFileName = farmsFileName,
    scenarioFileName = "scenarioFallowT0.json"
) {
    override val maxTicks: Int = 11

    protected suspend fun assertSowing(plant: PlantType, sowingPlantID: Int) {
        assertNextLine(farmStartActions(0))
        assertNextLine(farmSowingPlans(0, listOf(sowingPlantID)))
        assertNextLine(machinePerformAction(0, Action.SOWING, 1, 0))
        assertNextLine(machineSowed(0, plant, sowingPlantID))
        assertNextLine(machineReturnShed(0, 0))
        assertNextLine(farmFinishedActions(0))
    }
}

/**
 * Late sowing after Fallow of Potato
 */
class FallowSowPlanPotato : FallowSowPlanTestExtension(
    farmsFileName = "farmsFallowSowPlanPotato.json",
) {
    override val startYearTick: Int = Constants.APR_1
    override val description: String = "Sowing after Fallow of Potato"

    override suspend fun run() {
        // region Tick 0
        skipUntilString(tickStarted(0, startYearTick))
        // moisture: 930
        assertNextLine(soilMoisture(0, 0))
        assertSowing(PlantType.POTATO, 0)
        assertNextLine(incidentOccured(0, "DROUGHT", listOf(1)))
        // Drought -> no Log, as what Plant?
        // endregion Tick 0
        // Tick 1: moisture: 860
        // Tick 2: moisture: 790
        // Tick 3: moisture: 720
        // region Tick 4
        assertNextLine(tickStarted(4, startYearTick + 4)) // == JUN_1
        assertNextLine(soilMoisture(0, 0))
        assertSowing(PlantType.POTATO, 0)
        // Init: 1'000'000
        // Sow Late (80%): 800'000
        // Sunlight (max:130h, current:168h) (90%, 1x): 720'000
        // Moisture (min:500L, current: 650L) (-50g, 0x): 720'000
        assertNextLine(harvestEstimate(0, 720_000, PlantType.POTATO))
        // endregion Tick 4
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
