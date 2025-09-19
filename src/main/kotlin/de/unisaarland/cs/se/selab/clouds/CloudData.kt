package de.unisaarland.cs.se.selab.clouds

import de.unisaarland.cs.se.selab.logger.Logger
import kotlin.math.max
import kotlin.math.min

class CloudData(private var maxId: Int, val clouds: MutableList<Cloud>) {
    //
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
    private fun checkOccupied(cloud: Cloud) : Cloud? {
        return (clouds.firstOrNull { (it.id != cloud.id) && (it.location == cloud.location) })
    }
    //
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
    fun createCloud(duration: Int, amount: Int, location: Int, stepsRemaining: Int) : Cloud {
        maxId += 1
        val cloud = Cloud(maxId, duration, location, amount, stepsRemaining)
        clouds.add(cloud)
        return cloud
    }
    //
    fun dissipate(cloud: Cloud) {
        clouds.remove(cloud)
    }
    //
}