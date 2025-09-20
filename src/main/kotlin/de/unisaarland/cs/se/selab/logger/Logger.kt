package de.unisaarland.cs.se.selab.logger

import de.unisaarland.cs.se.selab.farms.Action
import de.unisaarland.cs.se.selab.incidents.Incident
import de.unisaarland.cs.se.selab.plants.PlantType
import java.io.FileWriter
import java.io.PrintWriter
import java.io.Writer

/**
 * Global Logger object handling every logger call with internal statistics Tracking
 */
object Logger {
    private var logLevel: LogLevel = LogLevel.DEBUG
    private var writer: Writer = PrintWriter(System.out, true)
    private var farmToHarvest = mutableMapOf<Int, Int>()
    private var plantToHarvest = mutableMapOf<PlantType, Int>()

    /**
     * Initially sets up the Logger
     * ONLY CALLED BY MAIN
     */
    fun initLogger(logLevel: LogLevel, output: String?) {
        this.logLevel = logLevel
        if (output != null) {
            this.writer = PrintWriter(FileWriter(output, true), true)
        }
    }

    /**
     * Responsible for printing depending on logLevel
     */
    private fun logPrint(level: LogLevel, message: String) {
        if (level.ordinal >= logLevel.ordinal) {
            writer.write("[${level.name}] $message")
        }
    }

    // ----Parsing----

    /**
     * Logs when a configuration file has been successfully parsed and validated.
     * [INFO] Initialization Info: $filename successfully parsed and validated.
     */
    fun fileParsed(filename: String) {
        logPrint(LogLevel.INFO,
            "Initialization Info: $filename successfully parsed and validated.")
    }

    /**
     * Logs when a configuration file is invalid and cannot be used.
     * [IMPORTANT] Initialization Info: $filename is invalid.
     */
    fun invalidFile(filename: String) {
        logPrint(LogLevel.IMPORTANT,
            "Initialization Info: $filename is invalid.")
    }

    // ----Simulation----

    /**
     * Logs when the simulation successfully starts, indicating the year tick.
     * [INFO] Simulation Info: Simulation started at tick $yearTick within the year.
     */
    fun simulationStarted(yearTick: Int) {
        logPrint(LogLevel.INFO,
            "Simulation Info: Simulation started at tick $yearTick within the year.")
    }

    /**
     * Logs when the simulation ends, showing the final tick.
     * [IMPORTANT] Simulation Info: Simulation ended at tick $tick.
     */
    fun simulationEnded(tick: Int) {
        logPrint(LogLevel.IMPORTANT,
            "Simulation Info: Simulation ended at tick $tick.")
    }

    /**
     * Logs the start of a simulation tick, including its global and yearly tick values.
     * [INFO] Simulation Info: Tick $tick started at tick $yearTick within the year.
     */
    fun tickStarted(tick: Int, yearTick: Int) {
        logPrint(LogLevel.INFO,
            "Simulation Info: Tick $tick started at tick $yearTick within the year.")
    }

    /**
     * Logs when soil moisture is below the required threshold in FIELD and PLANTATION tiles.
     * This is only relevant when tiles are planted and moisture is too low.
     * [INFO] Soil Moisture: The soil moisture is below threshold in §amountField FIELD
     * and $amountPlantation PLANTATION tiles.
     */
    fun soilMoistureBelowThreshold(amountField: Int, amountPlantation: Int) {
        logPrint(LogLevel.INFO,
            "Soil Moisture: The soil moisture is below threshold in " +
                    "$amountField FIELD and $amountPlantation PLANTATION tiles.")
    }

    // ----Clouds----

    /**
     * Logs when a cloud rains on a tile.
     * [IMPORTANT] Cloud Rain: Cloud $cloudID on tile $tileID rained down $amount L water.
     */
    fun logCloudRain(cloudId: Int, tileId: Int, amount: Int) {
        logPrint(LogLevel.IMPORTANT,
            "Cloud Rain: Cloud $cloudId on tile $tileId rained down $amount L water.")
    }

    /**
     * Logs when a cloud moves from one tile to another.
     * [INFO] Cloud Movement: Cloud $cloudID with $amountFluid L water moved from tile $startTileID to tile $endTileID.
     */
    fun logCloudMovement(cloudId: Int, amount: Int, startTileId: Int, endTileId: Int) {
        logPrint(LogLevel.INFO,
            "Cloud Movement: Cloud $cloudId with $amount L water moved from tile $startTileId to tile $endTileId.")
    }

    /**
     * Logs the amount of sunlight on a tile a cloud just moved from.
     * Only applies to FIELD and PLANTATION tiles.
     * [DEBUG] Cloud Movement: On tile $startTileID, the amount of sunlight is $amountSunlight.
     */
    fun logSunlightOnTile(startTileId: Int, amountSunlight: Int) {
        logPrint(LogLevel.DEBUG,
            "Cloud Movement: On tile $startTileId, the amount of sunlight is $amountSunlight.")
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
        logPrint(LogLevel.IMPORTANT,
            "Cloud Union: Clouds $cloudIdFrom and $cloudIdTo united to cloud $newCloudId" +
                    " with $amount L water and duration $duration on tile $tileId.")
    }

    /**
     * Logs when a cloud gets stuck on a village tile.
     * [INFO] Cloud Dissipation: Cloud $cloudID got stuck on tile $tileID.
     */
    fun logCloudStuck(cloudId: Int, tileId: Int) {
        logPrint(LogLevel.INFO,
            "Cloud Dissipation: Cloud $cloudId got stuck on tile $tileId.")
    }

    /**
     * Logs when a cloud dissipates on a tile.
     * This may happen due to raining all water or reaching maximum duration.
     * [INFO] Cloud Dissipation: Cloud $cloudID dissipates on tile $tileID.
     */
    fun logCloudDissipation(cloudId: Int, tileId: Int) {
        logPrint(LogLevel.INFO,
            "Cloud Dissipation: Cloud $cloudId dissipates on tile $tileId.")
    }

    /**
     * Logs the position and sunlight of a cloud on a FIELD or PLANTATION tile after all cloud movements.
     * [DEBUG] Cloud Position: Cloud $cloudID is on tile $tileID, where the amount of sunlight is $amountSunlight.
     */
    fun logCloudPosition(cloudId: Int, tileId: Int, amountSunlight: Int) {
        logPrint(LogLevel.DEBUG,
            "Cloud Position: Cloud $cloudId is on tile $tileId, where the amount of sunlight is $amountSunlight.")
    }

    // ----Farms----

    /**
     * D
     */
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
