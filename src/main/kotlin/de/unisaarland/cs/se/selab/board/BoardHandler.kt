package de.unisaarland.cs.se.selab.board

import de.unisaarland.cs.se.selab.logger.Logger
import kotlin.collections.Map

class BoardHandler {
    private val sunLight: Map<Int, Int> = mapOf(1 to 98, 2 to 112, 3 to 126, 4 to 140, 5 to 168, 6 to 168,
        7 to 168, 8 to 154, 9 to 126, 10 to 112, 11 to 98, 12 to 84)

    fun reduceSoil(year_tick: Int, board: BoardData) {
        val toReduceSoil = board.getFertiles().values
        var amountFields = 0
        var amountPlantations = 0
        for (fertile in toReduceSoil) {
            if (fertile.loseMoisture() && fertile.getTileType() == TileType.FIELD) {
                amountFields++
            } else if (fertile.loseMoisture()) {
                amountPlantations++
            }
        }
        Logger.soilMoistureBelowThreshold(amountFields, amountPlantations)
    }

    fun computeEstimate(year_tick: Int, board: BoardData) {
        val toAdaptEstimate = board.getFertiles().values
        for (fertile in toAdaptEstimate) {
            fertile.updateHarvestEstimate(year_tick)
        }
    }

    private fun getSunlightYearTick(yearTick: Int): Int? {
        assert((yearTick > 0) && (yearTick < 25))
        val month = (yearTick + 1) / 2
        return sunLight[month]
    }
}