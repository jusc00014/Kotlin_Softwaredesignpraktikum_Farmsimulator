package de.unisaarland.cs.se.selab.board

open class Tile(
    val id: Int,
    val coord: Coordinate,
    var airflow: Direction?,
    val shed: Boolean = false,
    val farmID: Int? = null,
    var type: TileType
) {
    /**
     * Checks if the Tile is Fertile
     * @return itself as Fertile if it is Fertile else null
     */
    open fun asFertile(): Fertile? {
        return null
    }

    /**
     * Increases moisture until amount is used or capacity is reached
     * @return the remaining amount
     */
    open fun rain(amount: Int): Int {
        return 0
    }
}
