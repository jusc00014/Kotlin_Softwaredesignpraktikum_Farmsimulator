package de.unisaarland.cs.se.selab.systemtest.selab25.farmactiontests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Stupid
 */
object ANIR {
    const val farmm = "farmActionTests/AnotherIrrigation/farm.json"
    const val sc = "onefieldtest/noscenario.json"
    const val mapp = "farmActionTests/AnotherIrrigation/map.json"
    const val deb = "DEBUG"
    const val qwe = "[IMPORTANT] Farm Action: Machine 2 performs IRRIGATING on tile 2 for 1 days."
}

/**
 * Test
 */
class AnotherIrrigationEstimate : TestExtension() {
    override val name = "AnotherIrrigationEstimate"
    override val description = "Tests harvest estimate"

    override val farms = ANIR.farmm
    override val scenario = ANIR.sc
    override val map = ANIR.mapp

    override val logLevel = ANIR.deb
    override val maxTicks = 2
    override val startYearTick = 6

    override suspend fun run() {
        skipUntilString("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 1080000 g of OAT.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 2 changed to 1239300 g of APPLE.")
        skipUntilString("[INFO] Harvest Estimate: Harvest estimate on tile 1 changed to 787320 g of OAT.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 2 changed to 903449 g of APPLE.")
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 3 were not performed: WEEDING.")
    }
}

/**
 * Test
 */
class AnotherIrrigationTrial3m1 : TestExtension() {
    override val name = "AnotherIrrigationTrial3m1"
    override val description = "Tests machine 1 works tile3"

    override val farms = ANIR.farmm
    override val scenario = ANIR.sc
    override val map = ANIR.mapp

    override val logLevel = ANIR.deb
    override val maxTicks = 2
    override val startYearTick = 6

    override suspend fun run() {
        skipUntilString("[IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 3 for 1 days.")
    }
}

/**
 * Test
 */
class AnotherIrrigationTrial2m1 : TestExtension() {
    override val name = "AnotherIrrigationTrial2m1"
    override val description = "Tests machine 1 works tile2"

    override val farms = ANIR.farmm
    override val scenario = ANIR.sc
    override val map = ANIR.mapp

    override val logLevel = ANIR.deb
    override val maxTicks = 2
    override val startYearTick = 6

    override suspend fun run() {
        skipUntilString("[IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 2 for 1 days.")
    }
}

/**
 * Test
 */
class AnotherIrrigationTrial2m2 : TestExtension() {
    override val name = "AnotherIrrigationTrial2m2"
    override val description = "Tests machine 2 works tile2"

    override val farms = ANIR.farmm
    override val scenario = ANIR.sc
    override val map = ANIR.mapp

    override val logLevel = ANIR.deb
    override val maxTicks = 2
    override val startYearTick = 6

    override suspend fun run() {
        skipUntilString(ANIR.qwe)
    }
}

/**
 * Test
 */
class AnotherIrrigationTrial3noone : TestExtension() {
    override val name = "AnotherIrrigationTrial2noone"
    override val description = "Tests noone works tile3"

    override val farms = ANIR.farmm
    override val scenario = ANIR.sc
    override val map = ANIR.mapp

    override val logLevel = ANIR.deb
    override val maxTicks = 2
    override val startYearTick = 6

    override suspend fun run() {
        skipUntilString("[IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 4 for 1 days.")
        assertNextLine(ANIR.qwe)
    }
}

/**
 * Test
 */
abstract class AnotherIrrigationTrialBase : TestExtension() {
    override val farms = ANIR.farmm
    override val scenario = ANIR.sc
    override val map = ANIR.mapp

    override val logLevel = ANIR.deb
    override val maxTicks = 2
    override val startYearTick = 6

    protected abstract val step: Int

    private val steps: List<suspend AnotherIrrigationTrialBase.() -> Unit> = listOf(
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
class AnotherIrrigationTrial1 : AnotherIrrigationTrialBase() {
    override val name = "AnotherIrrigationTrial1"
    override val description = "Checks first statement"
    override val step = 1
}

/**
 * Test
 */
class AnotherIrrigationTrial2 : AnotherIrrigationTrialBase() {
    override val name = "AnotherIrrigationTrial2"
    override val description = "Checks first 2 statements"
    override val step = 2
}

/**
 * Test
 */
class AnotherIrrigationTrial3 : AnotherIrrigationTrialBase() {
    override val name = "AnotherIrrigationTrial3"
    override val description = "Checks first 3 statements"
    override val step = 3
}

/**
 * Test
 */
class AnotherIrrigationTrial4 : AnotherIrrigationTrialBase() {
    override val name = "AnotherIrrigationTrial4"
    override val description = "Checks first 4 statements"
    override val step = 4
}

/**
 * Test
 */
class AnotherIrrigationTrial5 : AnotherIrrigationTrialBase() {
    override val name = "AnotherIrrigationTrial5"
    override val description = "Checks first 5 statements"
    override val step = 5
}
