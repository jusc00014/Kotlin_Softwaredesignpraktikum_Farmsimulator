package de.unisaarland.cs.se.selab.incidents

abstract class Incident(val id: Int, private val tick: Int) {
    open fun execute() {}
}