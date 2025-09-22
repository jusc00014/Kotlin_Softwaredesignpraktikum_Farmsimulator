package de.unisaarland.cs.se.selab.farms
import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.plants.PlantType
const val NUM = 15

/**
 * Machines that the farms can use*/
class Machine(
    val id: Int,
    val actions: List<Action>,
    val plants: List<PlantType>,
    val duration: Int,
    var location: Tile?,
    var brokenFor: Int = 0,
    var stuck: Boolean = false
) {
    /**
     * If machine can't get to shed after harvesting*/
    fun setStuck() {
        stuck = true
    }

    /**
     * Machine can perform an action*/
    fun isUsable(): Boolean {
        return brokenFor == 0 && !stuck && duration < NUM
    }

    /**
     * After a tick has passed*/
    fun decreaseBrokenFor() {
        if (brokenFor > 0) {
            brokenFor--
        }
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is Machine) {
            val bool1 = this.id == other.id && this.actions == other.actions && this.duration == other.duration
            val bool2 = this.location!!.equals(other.location) && this.brokenFor == other.brokenFor
            return bool1 && bool2 && this.stuck == other.stuck
        } else {
            return false
        }
    }
}
