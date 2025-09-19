package de.unisaarland.cs.se.selab.incidents

/**
 * holds and executes all Incidents created by parser. Executes only all Incidents for current tick*/
class IncidentHandler(private val upcomingIncidents: MutableMap<Int, List<Incident>>) {

    /**
     * called by Simulator every tick. Only executes the incidents, doesn't need to know anything else*/
    fun executeIncidents(tick: Int) {
        val incidents = upcomingIncidents[tick] ?: listOf()
        incidents.forEach { it.execute() }
    }
}