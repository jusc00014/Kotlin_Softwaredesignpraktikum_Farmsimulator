package de.unisaarland.cs.se.selab.systemtest.selab25.plantTests

import de.unisaarland.cs.se.selab.systemtest.selab25.farmactiontests.ANIR
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Test
 */
abstract class PlantationTestBase : TestExtension() {
    override val farms = "plantTests/PlantationTest/farm.json"
    override val scenario = ANIR.sc
    override val map = "plantTests/PlantationTest/map.json"

    override val logLevel = ANIR.deb
    override val maxTicks = 20
    override val startYearTick = 5

    protected abstract val step: Int

    private val steps: List<suspend PlantationTestBase.() -> Unit> = listOf(
        { skipUntilString("[IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 1 for 1 days.") },
        { assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 5 for 1 days.") },
        { assertNextLine("[IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 4 for 1 days.") },
        { skipUntilString("[IMPORTANT] Farm Action: Machine 2 performs IRRIGATING on tile 3 for 1 days.") },
        { assertNextLine("[IMPORTANT] Farm Action: Machine 2 performs IRRIGATING on tile 2 for 1 days.") }
    )

    override suspend fun run() {
        // Führe nur die ersten [step] Schritte aus
        steps.take(step).forEach { this.it() }
    }
}

/**
 * Test
 */
class PlantationTest1 : PlantationTestBase() {
    override val name = "PlantationTest1"
    override val description = "Checks first statement"
    override val step = 1
}
