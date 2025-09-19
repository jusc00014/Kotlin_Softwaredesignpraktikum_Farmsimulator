package de.unisaarland.cs.se.selab.clouds

import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.Tile

class CloudHandler (private val cloudData: CloudData, private val board: BoardData) {
    //
    fun moveClouds() {
        mutableIterate(cloudData.clouds, ::cloudAct)
        postMovement()
    }
    //
    private fun cloudAct(cloud: Cloud) {
        while (cloud.stepsRemaining > 0) {
            cloud.stepsRemaining -= 1
            if (rainIfPossible(cloud) || moveOneIfPossible(cloud)) {break}
        }
    }
    //
    private fun rainIfPossible(cloud: Cloud) : Boolean {
        // Returns true if Dissipate
        var amount = cloud.waterAmount
        if (amount < 5000) {return false}
        else {
            TODO()
        }
    }
    //
    private fun moveOneIfPossible(cloud: Cloud) : Boolean {
        // Returns true if hitsVillage or Merged or CantMove
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