package de.unisaarland.cs.se.selab.clouds

import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.Tile


class CloudHandler (val cloudData: CloudData, val board: BoardData) {
    //
    fun moveClouds() {
        mutableIterate(cloudData.clouds, ::cloudAct)
        postMovement()
    }
    //
    private fun cloudAct(cloud: Cloud) {
        TODO()
    }
    //
    private fun rainIfPossible(cloud: Cloud) {
        TODO()
    }
    //
    private fun moveOneIfPossible(cloud: Cloud) {
        TODO()
    }
    //
    private fun getNeighbor(tileId: Int) : Tile? {
        TODO()
    }
    //
    private fun postMovement() {
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