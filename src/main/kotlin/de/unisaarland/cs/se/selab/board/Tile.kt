package de.unisaarland.cs.se.selab.board

/**
 * Base class for every tile on the board
 */
open class Tile(
    val id: Int,
    val coord: Coordinate,
    val airflow: Direction?,
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

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is Tile) {
            val bool1 = this.id == other.id && this.coord == other.coord && this.airflow == other.airflow
            val bool2 = this.shed == other.shed && this.farmID == other.farmID && this.type == other.type
            return bool1 && bool2
        } else {
            return false
        }
    }
}
