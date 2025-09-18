package de.unisaarland.cs.se.selab.clouds

class CloudData(private var maxId: Int, public val idToCloud: MutableMap<Int, Cloud>) {
    //
    fun mergeIfNecessary(cloudId: Int) {
        TODO()
    }
    //
    private fun checkOccupied(cloud: Cloud) : Cloud? {
        TODO()
    }
    //
    private fun createMergedCloud(cloud1: Cloud, cloud2: Cloud) : Cloud {
        TODO()
    }
    //
    fun createCloud(duration: Int, amount: Int, location: Int) {
        TODO()
    }
    //
}