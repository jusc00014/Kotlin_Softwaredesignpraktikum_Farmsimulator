package de.unisaarland.cs.se.selab.clouds

import de.unisaarland.cs.se.selab.logger.Logger
import kotlin.math.max
import kotlin.math.min

/**
 * Stores clouds / Handles cloud interaction
 */
class CloudData(private var maxId: Int, val clouds: MutableList<Cloud>) {
    /**
     * returns MaxId of CloudData
     */
    fun getMaxId(): Int {
        return maxId
    }
    //
    /**
     * Responsible for overall merging logic
     */
    fun mergeIfNecessary(cloud: Cloud): Boolean {
        val possibleDupe = checkOccupied(cloud)
        if (possibleDupe == null) {
            return false
        } else {
            createMergedCloud(cloud, possibleDupe)
            return true
        }
    }
    //
    /**
     * Checks rather a tile is occupied by another cloud
     */
    private fun checkOccupied(cloud: Cloud): Cloud? {
        return clouds.firstOrNull { it.id != cloud.id && it.location == cloud.location }
    }
    //
    /**
     * Responsible for the merge event of two clouds
     */
    private fun createMergedCloud(cloud1: Cloud, cloud2: Cloud) {
        val newCloud = createCloud(
            min(cloud1.duration, cloud2.duration),
            cloud1.waterAmount + cloud2.waterAmount,
            cloud1.location,
            max(cloud1.stepsRemaining, cloud2.stepsRemaining)
        )
        Logger.logCloudUnion(
            cloud1.id,
            cloud2.id,
            newCloud.id,
            newCloud.waterAmount,
            newCloud.duration,
            newCloud.location
        )
        clouds.remove(cloud1)
        clouds.remove(cloud2)
    }
    //
    /**
     * Simply creates a new cloud
     */
    fun createCloud(duration: Int, amount: Int, location: Int, stepsRemaining: Int): Cloud {
        maxId += 1
        val cloud = Cloud(maxId, duration, location, amount, stepsRemaining)
        clouds.add(cloud)
        return cloud
    }
    //
    /**
     * Deletes a cloud when necessary due to raining or no more duration
     */
    fun dissipate(cloud: Cloud) {
        Logger.logCloudDissipation(cloud.id, cloud.location)
        clouds.remove(cloud)
    }
    //
    /**
     * Deletes a cloud when move onto Village tile
     */
    fun stuckOnVillage(cloud: Cloud) {
        Logger.logCloudStuck(cloud.id, cloud.location)
        clouds.remove(cloud)
    }
    //
    /**
     * checks rather a cloud is on a newly created village tile and deletes the cloud if found
     */
    fun checkIfCloudOnNewVillage(tileId: Int) {
        val cloud = clouds.firstOrNull { it.location == tileId }
        if (cloud != null) {
            stuckOnVillage(cloud)
        }
    }
}
