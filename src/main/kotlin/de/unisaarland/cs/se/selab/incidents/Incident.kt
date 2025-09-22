package de.unisaarland.cs.se.selab.incidents

/**
 * abstract Incident class being inherited from for every concrete Incident. Only common values are id and tick*/
abstract class Incident(val id: Int, val tick: Int) {

    /**
     * open function execute gets called by IncidentHandler on every Incident. overridden in every concrete Incident*/
    abstract fun execute()
}
