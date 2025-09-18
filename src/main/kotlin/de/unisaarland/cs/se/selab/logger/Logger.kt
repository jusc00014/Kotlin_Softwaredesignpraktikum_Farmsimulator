package de.unisaarland.cs.se.selab.logger

import de.unisaarland.cs.se.selab.plants.PlantType
import de.unisaarland.cs.se.selab.farms.Action
import de.unisaarland.cs.se.selab.incidents.Incident

class Logger (
    private val logLevel: LogLevel,
    private var farmHarvest: MutableMap<Int, Int>,
    private var totalHarvestPlant: MutableMap<PlantType, Int>
) {
    private fun logPrint(level: LogLevel, title: String, message: String) {
        TODO()
    }

    fun fileParsed(filename: String) {
        TODO()
    }

    fun invalidFile(filename: String) {
        TODO()
    }

    fun simulationStarted(yearTick: Int) {
        TODO()
    }

    fun simulationEnded(tick: Int) {
        TODO()
    }

    fun tickStarted(tick: Int, yearTick: Int) {
        TODO()
    }

    fun soilMoistureBelowThreshold(amountField: Int, amountPlantation: Int) {
        TODO()
    }

    fun cloudRained(cloudId: Int, tileId: Int, amountFluid: Int) {
        TODO()
    }

    fun cloudMovedTo(cloudId: Int, amountFluid: Int, startTile: Int, endTile: Int) {
        TODO()
    }

    fun sunAfterCloudMove(startTile: Int, amountSunlight: Int) {
        TODO()
    }

    fun cloudMerged(cloudIdFromTile: Int, cloudIdMovingToTile: Int, cloudIdNew: Int, amountFluid: Int, duration: Int, tileId: Int) {
        TODO()
    }

    fun cloudStucked(cloudId: Int, tileId: Int) {
        TODO()
    }

    fun cloudDissipated(cloudId: Int, tileId: Int) {
        TODO()
    }

    fun cloudPosition(cloudId: Int, tileId: Int, amountSunlight: Int) {
        TODO()
    }

    fun farmStartAction(farmId: Int) {
        TODO()
    }

    fun farmSowingPlan(farmId: Int, sowingPlanIds: List<Int>) {
        TODO()
    }

    fun machinePerform(machineId: Int, action: Action, tileId: Int, duration: Int) {
        TODO()
    }

    fun machineSowed(machineId: Int, plant: PlantType, sowingPlanId: Int) {
        TODO()
    }

    fun machineCollected(farmId: Int, machineId: Int, amount: Int, plant: PlantType) {
        TODO()
    }

    fun machineFinished(machineId: Int, tileId: Int) {
        TODO()
    }

    fun farmFinishedAction(farmId: Int) {
        TODO()
    }

    fun machineFinishedActionNoReturn(machineId: Int) {
        TODO()
    }

    fun machineUnloads(machineId: Int, amount: Int, plant: PlantType) {
        TODO()
    }

    fun incidentExecuted(incidentId: Int, incident: Incident, tileIds: List<Int>) {
        TODO()
    }

    fun actionNotPerformed(tileId: Int, actions: List<Action>) {
        TODO()
    }

    fun changedHarvestEstimate(tileId: Int, amount: Int, plant: PlantType) {
        TODO()
    }

    fun statisticCalculated() {
        TODO()
    }

    private fun farmCollected(farmId: Int, amount: Int) {
        TODO()
    }

    private fun totalHarvestedOf(plant: PlantType, amount: Int) {
        TODO()
    }

    fun totalEstimateFertile(amount: Int) {
        TODO()
    }
}