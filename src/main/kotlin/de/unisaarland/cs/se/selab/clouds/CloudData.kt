package de.unisaarland.cs.se.selab.clouds

class CloudData(private var maxId: Int, public val idToCloud: MutableMap<Int, Cloud>) {
    //
    fun mergeIfNecessary(cloud: Cloud) : Boolean {
        val possibleDupe = checkOccupied(cloud)
        if(possibleDupe == null) {
            return false
        }
        else {
            val newCloud = createMergedCloud(cloud, possibleDupe)
            idToCloud[newCloud.id] = newCloud
            idToCloud.remove(cloud.id)
            idToCloud.remove(possibleDupe.id)
            return true
        }
    }
    //
    private fun checkOccupied(cloud: Cloud) : Cloud? {
        return (idToCloud.values.firstOrNull { (it.id != cloud.id) && (it.location == cloud.location) })
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