package de.unisaarland.cs.se.selab.board

open class Tile(
    val id: Int,
    val coord: Coordinate,
    val airflow: Direction?,
    val shed: Boolean = false,
    val farmID: Int? = null,
    val type: TileType
) {
    open fun isFertile(): Boolean {
        return false
    }
}
