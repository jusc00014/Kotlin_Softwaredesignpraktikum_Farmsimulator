package de.unisaarland.cs.se.selab.board

import de.unisaarland.cs.se.selab.YEAR_TICK_MAX
import de.unisaarland.cs.se.selab.logger.Logger
import kotlin.collections.Map

const val THREE = 3
const val FOUR = 4
const val FIVE = 5
const val SIX = 6
const val SEVEN = 7
const val EIGHT = 8
const val NINE = 9
const val TEN = 10
const val ELEVEN = 11
const val TWELVE = 12
const val JANUARY_AND_NOVEMBER = 98
const val FEBRUARY_AND_OCTOBER = 112
const val MARCH_AND_SEPTEMBER = 126
const val APRIL = 140
const val MAY_AND_JUNE_AND_JULY = 168
const val AUGUST = 154
const val DECEMBER = 84

/**
 * BoardHandler holds map for sunlight and is used for moisture reduction phase and updateHarvestEstimate*/
class BoardHandler {
    val sunLight: Map<Int, Int> = mapOf(
        1 to JANUARY_AND_NOVEMBER,
        2 to FEBRUARY_AND_OCTOBER,
        3 to MARCH_AND_SEPTEMBER,
        FOUR to APRIL,
        FIVE to MAY_AND_JUNE_AND_JULY,
        SIX to MAY_AND_JUNE_AND_JULY,
        SEVEN to MAY_AND_JUNE_AND_JULY,
        EIGHT to AUGUST,
        NINE to MARCH_AND_SEPTEMBER,
        TEN to FEBRUARY_AND_OCTOBER,
        ELEVEN to JANUARY_AND_NOVEMBER,
        TWELVE to DECEMBER
    )

    /**
     * over all Fertiles on board, first sets all initial sunhours on tiles, then reduces moisture*/
    fun reduceSoil(yearTick: Int, board: BoardData) {
        val sunHours = getSunlightYearTick(yearTick) ?: return
        val toReduceSoil = board.getFertiles().values
        var amountFields = 0
        var amountPlantations = 0
        for (fertile in toReduceSoil) {
            fertile.prepareCurrentTick(sunHours, yearTick)
            val lm = fertile.loseMoisture()
            if (lm && fertile.type == TileType.FIELD) {
                amountFields++
            } else if (lm) {
                amountPlantations++
            }
        }
        Logger.soilMoistureBelowThreshold(amountFields, amountPlantations)
    }

    /**
     * updates harvest estimate for all Fertiles*/
    fun computeEstimate(yearTick: Int, board: BoardData) {
        val toAdaptEstimate = board.getFertiles().values
        for (fertile in toAdaptEstimate) {
            fertile.updateHarvestEstimate(yearTick)
        }
    }

    private fun getSunlightYearTick(yearTick: Int): Int? {
        assert(yearTick in 1..YEAR_TICK_MAX)
        val month = (yearTick + 1) / 2
        return sunLight[month]
    }
}
