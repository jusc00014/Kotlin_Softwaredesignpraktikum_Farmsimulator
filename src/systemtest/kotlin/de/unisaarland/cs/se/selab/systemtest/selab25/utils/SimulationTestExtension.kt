package de.unisaarland.cs.se.selab.systemtest.selab25.utils

/**
 * Test extension for Simulations with sane defaults
 */
abstract class SimulationTestExtension(
    folder: String,
) : TestExtension() {
    open val mapFileName: String = "map.json"
    open val farmsFileName: String = "farms.json"
    open val scenarioFileName: String = "scenario.json"
    override val map: String = "$folder/$mapFileName"
    override val farms: String = "$folder/$farmsFileName"
    override val scenario: String = "$folder/$scenarioFileName"
    override val maxTicks: Int = 1
    override val startYearTick: Int = 1
    override val logLevel: String = "DEBUG"
    override val name: String = "${this.javaClass.simpleName}"
}
