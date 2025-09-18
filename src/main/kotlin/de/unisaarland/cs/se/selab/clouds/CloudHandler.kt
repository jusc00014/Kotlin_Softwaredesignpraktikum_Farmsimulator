package de.unisaarland.cs.se.selab.clouds

import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.Tile


class CloudHandler {
    //
    fun moveClouds(cloudData: CloudData, board: BoardData) {
        TODO()
    }
    //
    private fun cloudAct() {
        TODO()
    }
    //
    private fun rain(cloud: Cloud, tile: Tile) {
        TODO()
    }
    //
    private fun moveOne(cloud: Cloud, board: BoardData) {
        TODO()
    }
    //
    private fun neighbor(tileId: Int, board: BoardData) : Tile? {
        TODO()
    }
    //
    private fun <T: Any> mutableIterate(list: MutableList<T>, action: (T) -> Unit) {
        // Necessary for List altering within their very own Iteration
        val processed = mutableSetOf<T>()
        while (true) {
            val next = list.firstOrNull { it !in processed } ?: break
            processed.add(next)
            action(next)
        }
    }
    //
}