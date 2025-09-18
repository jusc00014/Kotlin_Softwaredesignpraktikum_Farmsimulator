package de.unisaarland.cs.se.selab.incidents

abstract class Incident(val id: Int, protected val tick: Int) {
    open fun execute() {}
}