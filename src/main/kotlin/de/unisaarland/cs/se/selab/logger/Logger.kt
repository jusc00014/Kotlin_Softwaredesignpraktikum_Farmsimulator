package de.unisaarland.cs.se.selab.logger

import de.unisaarland.cs.se.selab.farms.Action
import de.unisaarland.cs.se.selab.incidents.Incident
import de.unisaarland.cs.se.selab.plants.PlantType
import java.io.FileWriter
import java.io.OutputStreamWriter
import java.io.Writer

/**
 * Global Logger object handling every logger call with internal statistics Tracking
 */
object Logger {
    private var logLevel: LogLevel? = null
    private var writer: Writer? = null
    private var farmHarvest: MutableMap<Int, Int> = mutableMapOf()
    private var totalHarvestPlant: MutableMap<PlantType, Int> = mutableMapOf()

    /**
     * Initially sets up the Logger
     * ONLY CALLED BY MAIN
     */
    fun initLogger(logLevel: LogLevel, output: String?) {
        this.logLevel = logLevel
        this.writer = if (output != null) FileWriter(output) else OutputStreamWriter(System.out)
    }

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

    // Clouds
    /**
     * Logs when a cloud rains on a tile.
     * [IMPORTANT] Cloud Rain: Cloud $cloudID on tile $tileID rained down $amount L water.
     */
    fun logCloudRain(cloudId: Int, tileId: Int, amount: Int) {
        // Implement logging logic here
        TODO()
    }

    /**
     * Logs when a cloud moves from one tile to another.
     * [INFO] Cloud Movement: Cloud $cloudID with $amountFluid L water moved from tile $startTileID to tile $endTileID.
     */
    fun logCloudMovement(cloudId: Int, amount: Int, startTileId: Int, endTileId: Int) {
        // Implement logging logic here
        TODO()
    }

    /**
     * Logs the amount of sunlight on a tile a cloud just moved from.
     * Only applies to FIELD and PLANTATION tiles.
     * [DEBUG] Cloud Movement: On tile $startTileID, the amount of sunlight is $amountSunlight.
     */
    fun logSunlightOnTile(startTileId: Int, amountSunlight: Int) {
        // Implement logging logic here
        TODO()
    }

    /**
     * Logs when two clouds unite into a new one on a tile.
     * [IMPORTANT] Cloud Union: Clouds $cloudIDFromTile and $cloudIDMovingToTile united to cloud $cloudIDNew
     * with $amount L water and duration $duration on tile $tileID.
     */
    fun logCloudUnion(
        cloudIdFrom: Int,
        cloudIdTo: Int,
        newCloudId: Int,
        amount: Int,
        duration: Int,
        tileId: Int
    ) {
        // Implement logging logic here
        TODO()
    }

    /**
     * Logs when a cloud gets stuck on a village tile.
     * [INFO] Cloud Dissipation: Cloud $cloudID got stuck on tile $tileID.
     */
    fun logCloudStuck(cloudId: Int, tileId: Int) {
        // Implement logging logic here
        TODO()
    }

    /**
     * Logs when a cloud dissipates on a tile.
     * This may happen due to raining all water or reaching maximum duration.
     * [INFO] Cloud Dissipation: Cloud $cloudID dissipates on tile $tileID.
     */
    fun logCloudDissipation(cloudId: Int, tileId: Int) {
        // Implement logging logic here
        TODO()
    }

    /**
     * Logs the position and sunlight of a cloud on a FIELD or PLANTATION tile after all cloud movements.
     * [DEBUG] Cloud Position: Cloud $cloudID is on tile $tileID, where the amount of sunlight is $amountSunlight.
     */
    fun logCloudPosition(cloudId: Int, tileId: Int, amountSunlight: Int) {
        // Implement logging logic here
        TODO()
    }
    //
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
