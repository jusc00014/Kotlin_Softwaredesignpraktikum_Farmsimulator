package de.unisaarland.cs.se.selab.board

open class Tile(
    val id: Int,
    val coord: Coordinate,
    val airflow: Direction?,
    val shed: Boolean = false,
    val farmID: Int? = null,
    val type: TileType
) {
    /**
     * Checks if the Tile is Fertile
     */
    open fun isFertile(): Boolean {
        return false
    }

    /**
     * Increases moisture until amount is used or capacity is reached
     * @return the remaining amount
     */
    open fun rain(amount: Int): Int {
        return 0
    }
}
