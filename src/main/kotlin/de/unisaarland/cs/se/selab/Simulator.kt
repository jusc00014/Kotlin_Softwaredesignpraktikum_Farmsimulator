package de.unisaarland.cs.se.selab

import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.BoardHandler
import de.unisaarland.cs.se.selab.clouds.CloudHandler
import de.unisaarland.cs.se.selab.farms.FarmHandler
import de.unisaarland.cs.se.selab.incidents.IncidentHandler
import de.unisaarland.cs.se.selab.logger.Logger
import kotlin.math.max

/**
 * Heart of the Simulation
 */
class Simulator(
    private val boardHandler: BoardHandler,
    private val farmHandler: FarmHandler,
    private val cloudHandler: CloudHandler,
    private val incidentHandler: IncidentHandler,
    private val boardData: BoardData,
    private val startYearTick: Int,
    private val maxTick: Int
) {
    /**
     * Start the simulation loop and prints statistics on completion
     */
    fun start() {
        var yearTick: Int = startYearTick
        Logger.simulationStarted(yearTick)
        for (tick in 0..<maxTick) {
            Logger.tickStarted(tick, yearTick)
            boardHandler.reduceSoil(yearTick, boardData)
            cloudHandler.moveClouds()
            farmHandler.farmAction(tick, yearTick, boardData)
            incidentHandler.executeIncidents(tick)
            boardHandler.computeEstimate(yearTick, boardData)
            yearTick = (yearTick % YEAR_TICK_MAX) + 1
        }
        Logger.simulationEnded(max(maxTick, 0))
        Logger.statisticCalculated()
        val amount = boardData.getFertiles().values.sumOf { it.plant.getHarvestEstimate() }
        Logger.totalEstimateFertile(amount)
    }
}
