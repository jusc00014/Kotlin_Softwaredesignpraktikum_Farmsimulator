package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.farms.Machine
import de.unisaarland.cs.se.selab.logger.Logger
import kotlin.math.max

/**
 * BrokenMachine inherits from incident and overrides Incident*/
class BrokenMachine(id: Int, tick: Int, private val duration: Int, private val machine: Machine) : Incident(id, tick) {

    override fun execute() {
        if (duration > 0) {
            machine.brokenFor = max(machine.brokenFor, duration)
        } else {
            machine.brokenFor = -1
        }
        Logger.incidentExecuted(id, this, listOf(machine.location))
    }
    override fun toString(): String {
        return "BROKEN_MACHINE"
    }
}
