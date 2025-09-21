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
    private var plantToHarvest =
        mapOf(PlantType.POTATO to 0, PlantType.WHEAT to 0, PlantType.OAT to 0, PlantType.PUMPKIN to 0,
            PlantType.APPLE to 0, PlantType.GRAPE to 0, PlantType.ALMOND to 0, PlantType.CHERRY to 0)

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
            writer.write(message)
        }
    }

    // ----Parsing----

    /**
     * Logs when a configuration file has been successfully parsed and validated.
     * \[INFO] Initialization Info: $filename successfully parsed and validated.
     */
    fun fileParsed(filename: String) {
        logPrint(LogLevel.INFO,
            "Initialization Info: $filename successfully parsed and validated.")
    }

    /**
     * Logs when a configuration file is invalid and cannot be used.
     * \[IMPORTANT] Initialization Info: $filename is invalid.
     */
    fun invalidFile(filename: String) {
        logPrint(LogLevel.IMPORTANT,
            "Initialization Info: $filename is invalid.")
    }

    // ----Simulation----

    /**
     * Logs when the simulation successfully starts, indicating the year tick.
     * \[INFO] Simulation Info: Simulation started at tick $yearTick within the year.
     */
    fun simulationStarted(yearTick: Int) {
        logPrint(LogLevel.INFO,
            "Simulation Info: Simulation started at tick $yearTick within the year.")
    }

    /**
     * Logs when the simulation ends, showing the final tick.
     * \[IMPORTANT] Simulation Info: Simulation ended at tick $tick.
     */
    fun simulationEnded(tick: Int) {
        logPrint(LogLevel.IMPORTANT,
            "Simulation Info: Simulation ended at tick $tick.")
    }

    /**
     * Logs the start of a simulation tick, including its global and yearly tick values.
     * \[INFO] Simulation Info: Tick $tick started at tick $yearTick within the year.
     */
    fun tickStarted(tick: Int, yearTick: Int) {
        logPrint(LogLevel.INFO,
            "Simulation Info: Tick $tick started at tick $yearTick within the year.")
    }

    /**
     * Logs when soil moisture is below the required threshold in FIELD and PLANTATION tiles.
     * This is only relevant when tiles are planted and moisture is too low.
     * \[INFO] Soil Moisture: The soil moisture is below threshold in §amountField FIELD
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
     * \[IMPORTANT] Cloud Rain: Cloud $cloudID on tile $tileID rained down $amount L water.
     */
    fun logCloudRain(cloudId: Int, tileId: Int, amount: Int) {
        logPrint(LogLevel.IMPORTANT,
            "Cloud Rain: Cloud $cloudId on tile $tileId rained down $amount L water.")
    }

    /**
     * Logs when a cloud moves from one tile to another.
     * \[INFO] Cloud Movement: Cloud $cloudID with $amountFluid L water moved from tile $startTileID to tile $endTileID.
     */
    fun logCloudMovement(cloudId: Int, amount: Int, startTileId: Int, endTileId: Int) {
        logPrint(LogLevel.INFO,
            "Cloud Movement: Cloud $cloudId with $amount L water moved from tile $startTileId to tile $endTileId.")
    }

    /**
     * Logs the amount of sunlight on a tile a cloud just moved from.
     * Only applies to FIELD and PLANTATION tiles.
     * \[DEBUG] Cloud Movement: On tile $startTileID, the amount of sunlight is $amountSunlight.
     */
    fun logSunlightOnTile(startTileId: Int, amountSunlight: Int) {
        logPrint(LogLevel.DEBUG,
            "Cloud Movement: On tile $startTileId, the amount of sunlight is $amountSunlight.")
    }

    /**
     * Logs when two clouds unite into a new one on a tile.
     * \[IMPORTANT] Cloud Union: Clouds $cloudIDFromTile and $cloudIDMovingToTile united to cloud $cloudIDNew
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
     * \[INFO] Cloud Dissipation: Cloud $cloudID got stuck on tile $tileID.
     */
    fun logCloudStuck(cloudId: Int, tileId: Int) {
        logPrint(LogLevel.INFO,
            "Cloud Dissipation: Cloud $cloudId got stuck on tile $tileId.")
    }

    /**
     * Logs when a cloud dissipates on a tile.
     * This may happen due to raining all water or reaching maximum duration.
     * \[INFO] Cloud Dissipation: Cloud $cloudID dissipates on tile $tileID.
     */
    fun logCloudDissipation(cloudId: Int, tileId: Int) {
        logPrint(LogLevel.INFO,
            "Cloud Dissipation: Cloud $cloudId dissipates on tile $tileId.")
    }

    /**
     * Logs the position and sunlight of a cloud on a FIELD or PLANTATION tile after all cloud movements.
     * \[DEBUG] Cloud Position: Cloud $cloudID is on tile $tileID, where the amount of sunlight is $amountSunlight.
     */
    fun logCloudPosition(cloudId: Int, tileId: Int, amountSunlight: Int) {
        logPrint(LogLevel.DEBUG,
            "Cloud Position: Cloud $cloudId is on tile $tileId, where the amount of sunlight is $amountSunlight.")
    }

    // ----Farms----

    /**
     * \[IMPORTANT] Farm: Farm $farmID starts its actions.
     */
    fun farmStartAction(farmId: Int) {
        logPrint(LogLevel.IMPORTANT,
            "Farm: Farm $farmId starts its actions.")
    }

    /**
     * \[DEBUG] Farm: Farm $farmID has the following active sowing plans it
     * intends to pursue in this tick: $sowingPlanIDs.
     */
    fun farmSowingPlan(farmId: Int, sowingPlanIds: List<Int>) {
        val sortedPlans = sowingPlanIds.sortedBy { it }
        logPrint(LogLevel.DEBUG,
            "Farm: Farm $farmId has the following active sowing plans it intends to pursue in this tick: $sortedPlans.")
    }

    /**
     * \[IMPORTANT] Farm Action: Machine $machineID performs $actionType on
     * tile $tileID for $duration days.
     */
    fun machinePerformedAction(machineId: Int, action: Action, tileId: Int, duration: Int) {
        logPrint(LogLevel.IMPORTANT,
            "Farm Action: Machine $machineId performs $action on tile $tileId for $duration days.")
    }

    /**
     * \[IMPORTANT] Farm Sowing: Machine $machineID has sowed $plant according
     * to sowing plan $sowingPlanID.
     */
    fun machineSowed(machineId: Int, plant: PlantType, sowingPlanId: Int) {
        logPrint(LogLevel.IMPORTANT,
            "Farm Sowing: Machine $machineId has sowed $plant according to sowing plan $sowingPlanId.")
    }

    /**
     * \[IMPORTANT] Farm Harvest: Machine $machineID has collected $amount g of
     * $plant harvest.
     */
    fun machineCollected(farmId: Int, machineId: Int, amount: Int, plant: PlantType) {
        logPrint(LogLevel.IMPORTANT,
            "Farm Harvest: Machine $machineId has collected $amount g of $plant harvest.")
    }

    /**
     * \[IMPORTANT] Farm Machine: Machine $machineID is finished and returns to the shed at $tileID.
     */
    fun machineFinished(machineId: Int, tileId: Int) {
        logPrint(LogLevel.IMPORTANT,
            "Farm Machine: Machine $machineId is finished and returns to the shed at $tileId.")
    }

    /**
     * \[IMPORTANT] Farm Machine: Machine $machineID is finished but failed to return.
     */
    fun machineFinishedNoReturn(machineId: Int) {
        logPrint(LogLevel.IMPORTANT,
            "Farm Machine: Machine $machineId is finished but failed to return.")
    }

    /**
     * \[IMPORTANT] Farm Machine: Machine $machineID unloads $amount g of $plant harvest in the shed.
     */
    fun machineUnloads(machineId: Int, amount: Int, plant: PlantType) {
        logPrint(LogLevel.IMPORTANT,
            "Farm Machine: Machine $machineId unloads $amount g of $plant harvest in the shed.")
    }

    /**
     * \[IMPORTANT] Farm: Farm $farmID finished its actions.
     */
    fun farmFinishedAction(farmId: Int) {
        logPrint(LogLevel.IMPORTANT,
            "Farm: Farm $farmId finished its actions.")
    }

    // ----Incidents----

    /**
     * \[IMPORTANT] Incident: Incident $incidentID of type $incidentType happened and affected tiles $tileIDs.
     */
    fun incidentExecuted(incidentId: Int, incident: Incident, tileIds: List<Int>) {
        val sortedTileIds = tileIds.sortedBy { it }
        logPrint(LogLevel.IMPORTANT,
            "Incident: Incident $incidentId of type $incident happened and affected tiles $sortedTileIds.")
    }

    // ----Estimate----

    /**
     * \[DEBUG] Harvest Estimate: Required actions on tile $tileID were not performed: $actions.
     */
    fun actionNotPerformed(tileId: Int, actions: List<Action>) {
        val sortedActions=  actions.sortedBy { it.ordinal }
        logPrint(LogLevel.DEBUG,
            "Harvest Estimate: Required actions on tile $tileId were not performed: $sortedActions.")
    }

    /**
     * \[INFO] Harvest Estimate: Harvest estimate on tile $tileID changed to $amount g of $plant.
     */
    fun changedHarvestEstimate(tileId: Int, amount: Int, plant: PlantType) {
        logPrint(LogLevel.INFO,
            "Harvest Estimate: Harvest estimate on tile $tileId changed to $amount g of $plant.")
    }

    // ----Statistics----

    /**
     * \[IMPORTANT] Simulation Info: Simulation statistics are calculated.
     */
    fun statisticCalculated() {
        logPrint(LogLevel.IMPORTANT,
            "Simulation Info: Simulation statistics are calculated.")
    }

    /**
     * Responsible for calling Statistic logs
     */
    fun logCollected() {
        farmToHarvest.toSortedMap().forEach { farmCollected(it.key, it.value) }
        plantToHarvest.forEach { totalPlantHarvest(it.key, it.value) }
    }

    /**
     * \[IMPORTANT] Simulation Statistics: Farm $farmID collected $amount g of harvest.
     */
    private fun farmCollected(farmId: Int, amount: Int) {
        logPrint(LogLevel.IMPORTANT,
            "Simulation Statistics: Farm $farmId collected $amount g of harvest.")
    }

    /**
     * \[IMPORTANT] Simulation Statistics: Total amount of $plant harvested: $amount g.
     */
    private fun totalPlantHarvest(plant: PlantType, amount: Int) {
        logPrint(LogLevel.IMPORTANT,
            "Simulation Statistics: Total amount of $plant harvested: $amount g.")
    }

    /**
     * \[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: $amount g.
     */
    fun totalEstimateFertile(amount: Int) {
        logPrint(LogLevel.IMPORTANT,
            "Simulation Statistics: Total harvest estimate still in fields and plantations: $amount g.")
    }
}
