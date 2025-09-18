package de.unisaarland.cs.se.selab.clouds

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
         createCloud(min(cloud1.duration, cloud2.duration), cloud1.waterAmount + cloud2.waterAmount, cloud1.location,
            max(cloud1.stepsRemaining, cloud2.stepsRemaining)
        )
        clouds.remove(cloud1)
        clouds.remove(cloud2)
    }
    //
    fun createCloud(duration: Int, amount: Int, location: Int, stepsRemaining: Int) {
        val cloud = Cloud(maxId, duration, location, amount, stepsRemaining)
        maxId += 1
        clouds.add(cloud)
    }
    //
}