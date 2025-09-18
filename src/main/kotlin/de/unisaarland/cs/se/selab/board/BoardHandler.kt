package de.unisaarland.cs.se.selab.board

class BoardHandler (
    val sunLight: Map<Int, Int> = mapOf(1 to 98, 2 to 112, 3 to 126, 4 to 140, 5 to 168, 6 to 168,
        7 to 168, 8 to 154, 9 to 126, 10 to 112, 11 to 98, 12 to 84)
) {
    fun reduceSoil (yearTick: Int, board: BoardData): Unit {
        TODO()
    }

    fun computeEstimate(yearTick: Int, board: BoardData): Unit {
        TODO()
    }

    private fun getSunlightYearTick(yearTick: Int): Int? {
        assert((yearTick > 0) && (yearTick < 25))
        val month = (yearTick + 1) / 2
        return sunLight[month]
    }
}