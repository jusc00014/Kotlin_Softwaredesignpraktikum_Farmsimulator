package de.unisaarland.cs.se.selab.systemtest.selab25.utils

import de.unisaarland.cs.se.selab.farms.Action
import de.unisaarland.cs.se.selab.plants.PlantType

/**
 * Test extension for Simulations with sane defaults
 */
abstract class SimulationTestExtension(
    folder: String,
    val mapFileName: String = "map.json",
    val farmsFileName: String = "farms.json",
    val scenarioFileName: String = "scenario.json",
) : TestExtension() {
    override val map: String = "$folder/$mapFileName"
    override val farms: String = "$folder/$farmsFileName"
    override val scenario: String = "$folder/$scenarioFileName"
    override val maxTicks: Int = 1
    override val startYearTick: Int = 1
    override val logLevel: String = "DEBUG"
    override val name: String = "${this.javaClass.simpleName}"

    protected fun formatArray(arr: List<*>): String = arr.joinToString(",")

    protected fun tickStarted(tick: Int, yearTick: Int): String =
        "[INFO] Simulation Info: Tick $tick started at tick $yearTick within the year."
    protected fun soilMoisture(amountField: Int, amountPlantation: Int): String =
        "[INFO] Soil Moisture: The soil moisture is below threshold in $amountField FIELD and $amountPlantation " +
            "PLANTATION tiles."
    protected fun farmStartActions(farmID: Int): String =
        "[IMPORTANT] Farm: Farm $farmID starts its actions."
    protected fun farmSowingPlans(farmID: Int, sowingPlanIDs: List<Int>): String =
        "[DEBUG] Farm: Farm $farmID has the following active sowing plans it intends to pursue in this tick: " +
            "${formatArray(sowingPlanIDs)}."
    protected fun machinePerformAction(machineID: Int, actionType: Action, tileID: Int, duration: Int): String =
        "[IMPORTANT] Farm Action: Machine $machineID performs ${actionType.name} on tile $tileID for $duration days."
    protected fun machineSowed(machineID: Int, plant: PlantType, sowingPlanID: Int): String =
        "[IMPORTANT] Farm Sowing: Machine $machineID has sowed ${plant.name} according to sowing plan $sowingPlanID."
    protected fun machineCollectedHarvest(machineID: Int, amount: Int, plant: PlantType): String =
        "[IMPORTANT] Farm Harvest: Machine $machineID has collected $amount g of ${plant.name} harvest."
    protected fun machineReturnShed(machineID: Int, tileID: Int): String =
        "[IMPORTANT] Farm Machine: Machine $machineID is finished and returns to the shed at $tileID."
    protected fun machineFailedReturn(machineID: Int): String =
        "[IMPORTANT] Farm Machine: Machine $machineID is finished but failed to return."
    protected fun machineUnloaded(machineID: Int, amount: Int, plant: PlantType): String =
        "[IMPORTANT] Farm Machine: Machine $machineID unloads $amount g of ${plant.name} harvest in the shed."
    protected fun farmFinishedActions(farmID: Int): String =
        "[IMPORTANT] Farm: Farm $farmID finished its actions."
    protected fun incidentOccured(incidentID: Int, incidentType: String, tileIDs: List<Int>) =
        "[IMPORTANT] Incident: Incident $incidentID of type $incidentType happened and affected tiles " +
            "${formatArray(tileIDs.sortedBy { it })}."
    protected fun actionNotPerformed(tileID: Int, actions: List<Action>): String =
        "[DEBUG] Harvest Estimate: Required actions on tile $tileID were not performed: ${actions.joinToString(",")}."
    protected fun harvestEstimate(tileID: Int, amount: Int, plant: PlantType) =
        "[INFO] Harvest Estimate: Harvest estimate on tile $tileID changed to $amount g of ${plant.name}."
    protected fun simulationEnd(tick: Int): String =
        "[IMPORTANT] Simulation Info: Simulation ended at tick $tick."
    protected val statisticsCalculated: String =
        "[IMPORTANT] Simulation Info: Simulation statistics are calculated."
    protected fun statisticFarmCollected(farmID: Int, amount: Int): String =
        "[IMPORTANT] Simulation Statistics: Farm $farmID collected $amount g of harvest."
    protected fun statisticPlantHarvested(plant: PlantType, amount: Int): String =
        "[IMPORTANT] Simulation Statistics: Total amount of ${plant.name} harvested: $amount g."
    protected fun statisticHarvestLeft(amount: Int): String =
        "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: $amount g."
    protected suspend fun assertStatisticPlantHarvested(plantMap: Map<PlantType, Int>) {
        for (plant in arrayOf(
            PlantType.POTATO,
            PlantType.WHEAT,
            PlantType.OAT,
            PlantType.PUMPKIN,
            PlantType.APPLE,
            PlantType.GRAPE,
            PlantType.ALMOND,
            PlantType.CHERRY
        )) {
            assertNextLine(statisticPlantHarvested(plant, plantMap[plant] ?: 0))
        }
    }
}
