package de.unisaarland.cs.se.selab.clouds

class CloudData(private var maxId: Int, public val idToCloud: MutableMap<Int, Cloud>) {
    //
    public fun mergeIfNecessary(cloudId: Int) {
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
    public fun createCloud(duration: Int, amount: Int, location: Int) {
        TODO()
    }
    //
}