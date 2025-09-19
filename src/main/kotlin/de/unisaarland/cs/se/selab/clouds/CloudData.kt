package de.unisaarland.cs.se.selab.clouds

import de.unisaarland.cs.se.selab.logger.Logger
import kotlin.math.max
import kotlin.math.min

/**
 * Detekt
 */
class CloudData(private var maxId: Int, val clouds: MutableList<Cloud>) {
    //
    /**
     * Detekt
     */
    fun mergeIfNecessary(cloud: Cloud) : Boolean {
        val possibleDupe = checkOccupied(cloud)
        if(possibleDupe == null) {
            return false
        }
        else {
            createMergedCloud(cloud, possibleDupe)
            return true
        }
    }
    //
    /**
     * Detekt
     */
    private fun checkOccupied(cloud: Cloud) : Cloud? {
        return clouds.firstOrNull { it.id != cloud.id && it.location == cloud.location }
    }
    //
    /**
     * Detekt
     */
    private fun createMergedCloud(cloud1: Cloud, cloud2: Cloud) {
        val newCloud = createCloud(min(cloud1.duration, cloud2.duration),
            cloud1.waterAmount + cloud2.waterAmount, cloud1.location,
            max(cloud1.stepsRemaining, cloud2.stepsRemaining)
        )
        Logger.logCloudUnion(cloud1.id, cloud2.id,
            newCloud.id, newCloud.waterAmount, newCloud.duration, newCloud.location)
        clouds.remove(cloud1)
        clouds.remove(cloud2)
    }
    //
    /**
     * Detekt
     */
    fun createCloud(duration: Int, amount: Int, location: Int, stepsRemaining: Int) : Cloud {
        maxId += 1
        val cloud = Cloud(maxId, duration, location, amount, stepsRemaining)
        clouds.add(cloud)
        return cloud
    }
    //
    /**
     * Detekt
     */
    fun dissipate(cloud: Cloud) {
        Logger.logCloudDissipation(cloud.id, cloud.location)
        clouds.remove(cloud)
    }
    //
    /**
     * Detekt
     */
    fun stuckOnVillage(cloud: Cloud) {
        Logger.logCloudStuck(cloud.id, cloud.location)
        clouds.remove(cloud)
    }
    //
    /**
     * Detekt
     */
    fun checkIfCloudOnNewVillage(tileId: Int) {
        val cloud = clouds.firstOrNull { it.location == tileId }
        if (cloud != null) {
            stuckOnVillage(cloud)
        }
    }
    //
}