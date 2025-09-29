package de.unisaarland.cs.se.selab.plants

import kotlin.math.max

/**
 * Wrapper for Harvest Estimate which count the modifications
 */
class HarvestEstimate(private var harvestEstimate: Int) {
    private var changed: Boolean = false

    /**
     * Resets the change counter
     */
    fun resetFlag() { changed = false }

    /**
     * Getter for HarvestEstimate
     */
    fun get(): Int = harvestEstimate

    /**
     * Sets the HarvestEstimate to this value
     */
    fun set(harvestEstimate: Int) {
        require(harvestEstimate >= 0)
        changed = true
        this.harvestEstimate = harvestEstimate
    }

    /**
     * Check if the HarvestEstimate was changed from the last reset
     */
    fun hasChanged(): Boolean = changed

    /**
     * subtract the given amount from the harvestEstimate
     */
    fun reduceBy(amount: Int) {
        changed = false
        harvestEstimate = max(0, harvestEstimate - amount)
    }

    /**
     * Multiply the harvestEstimate by the given amount
     */
    fun multipliedBy(amount: Double) {
        changed = false
        harvestEstimate = max(0, (harvestEstimate * amount).toInt())
    }
}
