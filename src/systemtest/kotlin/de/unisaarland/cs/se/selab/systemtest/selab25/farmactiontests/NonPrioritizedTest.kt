package de.unisaarland.cs.se.selab.systemtest.selab25.farmactiontests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Gemeinsame Basis für alle NonPrioritizedTests
 */
abstract class NonPrioritizedBaseTest : TestExtension() {

    override val farms = "farmActionTests/farm.json"
    override val scenario = "onefieldtest/noscenario.json"
    override val map = "farmActionTests/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 3
    override val startYearTick = 9

    protected val sownstr =
        "[IMPORTANT] Farm Sowing: Machine 4 has sowed POTATO according to sowing plan 1."

    /**
     * Jede Subklasse legt fest, wie viele Schritte geprüft werden.
     */
    protected abstract val step: Int

    private val steps: List<suspend NonPrioritizedBaseTest.() -> Unit> = listOf(
        { skipUntilString(sownstr) },
        { assertCurrentLine("[IMPORTANT] Farm Sowing: Machine 4 has sowed POTATO according to sowing plan 1.") },
        { assertNextLine("[IMPORTANT] Farm Action: Machine 4 performs SOWING on tile 3 for 4 days.") },
        { assertNextLine(sownstr) },
        { assertNextLine("[IMPORTANT] Farm Action: Machine 4 performs SOWING on tile 5 for 4 days.") },
        { assertNextLine(sownstr) },
        { assertNextLine("[IMPORTANT] Farm Machine: Machine 4 is finished and returns to the shed at 7.") },
        { assertNextLine("[IMPORTANT] Farm Action: Machine 2 performs SOWING on tile 6 for 6 days.") },
        { skipUntilString("[IMPORTANT] Farm Action: Machine 4 performs WEEDING on tile 1 for 4 days.") },
        { assertCurrentLine("[IMPORTANT] Farm Action: Machine 4 performs WEEDING on tile 1 for 4 days.") },
        { assertNextLine("[IMPORTANT] Farm Action: Machine 4 performs WEEDING on tile 3 for 4 days.") },
        { assertNextLine("[IMPORTANT] Farm Action: Machine 4 performs WEEDING on tile 5 for 4 days.") },
        { assertNextLine("[IMPORTANT] Farm Machine: Machine 4 is finished and returns to the shed at 7.") },
        { assertNextLine("[IMPORTANT] Farm Action: Machine 2 performs IRRIGATING on tile 6 for 6 days.") },
        { assertNextLine("[IMPORTANT] Farm Action: Machine 2 performs IRRIGATING on tile 4 for 6 days.") },
        { assertNextLine("[IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 7.") },
        { assertNextLine("[IMPORTANT] Farm Action: Machine 3 performs MOWING on tile 2 for 8 days.") },
        { skipUntilString("[DEBUG] Harvest Estimate: Required actions on tile 3 were not performed: IRRIGATING.") },
        { assertCurrentLine("[DEBUG] Harvest Estimate: Required actions on tile 3 were not performed: IRRIGATING.") }
    )

    override suspend fun run() {
        // Führe nur die ersten [step] Schritte aus
        steps.take(step).forEach { this.it() }
    }
}

/**
 * Beispiel-Subklassen
 */
class NonPrioritizedTest1 : NonPrioritizedBaseTest() {
    override val name = "NonPrioritizedTest1"
    override val description = "Checks first statement"
    override val step = 1
}

/**
 * 2
 */
class NonPrioritizedTest2 : NonPrioritizedBaseTest() {
    override val name = "NonPrioritizedTest2"
    override val description = "Checks first two statements"
    override val step = 2
}

/**
 * 3
 */
class NonPrioritizedTest3 : NonPrioritizedBaseTest() {
    override val name = "NonPrioritizedTest3"
    override val description = "Checks first three statements"
    override val step = 3
}

/**
 * 4
 */
class NonPrioritizedTest4 : NonPrioritizedBaseTest() {
    override val name = "NonPrioritizedTest4"
    override val description = "Checks first 4 statements"
    override val step = 4
}

/**
 * 5
 */
class NonPrioritizedTest5 : NonPrioritizedBaseTest() {
    override val name = "NonPrioritizedTest5"
    override val description = "Checks first 5 statements"
    override val step = 5
}

/**
 * 6
 */
class NonPrioritizedTest6 : NonPrioritizedBaseTest() {
    override val name = "NonPrioritizedTest6"
    override val description = "Checks first 6 statements"
    override val step = 6
}

/**
 * 7
 */
class NonPrioritizedTest7 : NonPrioritizedBaseTest() {
    override val name = "NonPrioritizedTest7"
    override val description = "Checks first 7 statements"
    override val step = 7
}

/**
 * 8
 */
class NonPrioritizedTest8 : NonPrioritizedBaseTest() {
    override val name = "NonPrioritizedTest8"
    override val description = "Checks first 8 statements"
    override val step = 8
}

/**
 * 9
 */
class NonPrioritizedTest9 : NonPrioritizedBaseTest() {
    override val name = "NonPrioritizedTest9"
    override val description = "Checks first 9 statements"
    override val step = 9
}

/**
 * 10
 */
class NonPrioritizedTest10 : NonPrioritizedBaseTest() {
    override val name = "NonPrioritizedTest10"
    override val description = "Checks first 10 statements"
    override val step = 10
}

/**
 * 11
 */
class NonPrioritizedTest11 : NonPrioritizedBaseTest() {
    override val name = "NonPrioritizedTest11"
    override val description = "Checks first 11 statements"
    override val step = 11
}

/**
 * 12
 */
class NonPrioritizedTest12 : NonPrioritizedBaseTest() {
    override val name = "NonPrioritizedTest12"
    override val description = "Checks first 12 statements"
    override val step = 12
}

/**
 * 13
 */
class NonPrioritizedTest13 : NonPrioritizedBaseTest() {
    override val name = "NonPrioritizedTest13"
    override val description = "Checks first 13 statements"
    override val step = 13
}

/**
 * 14
 */
class NonPrioritizedTest14 : NonPrioritizedBaseTest() {
    override val name = "NonPrioritizedTest14"
    override val description = "Checks first 14 statements"
    override val step = 14
}

/**
 * 15
 */
class NonPrioritizedTest15 : NonPrioritizedBaseTest() {
    override val name = "NonPrioritizedTest15"
    override val description = "Checks first 15 statements"
    override val step = 15
}

/**
 * 16
 */
class NonPrioritizedTest16 : NonPrioritizedBaseTest() {
    override val name = "NonPrioritizedTest16"
    override val description = "Checks first 16 statements"
    override val step = 16
}

/**
 * 17
 */
class NonPrioritizedTest17 : NonPrioritizedBaseTest() {
    override val name = "NonPrioritizedTest17"
    override val description = "Checks first 17 statements"
    override val step = 17
}

/**
 * 18
 */
class NonPrioritizedTest18 : NonPrioritizedBaseTest() {
    override val name = "NonPrioritizedTest18"
    override val description = "Checks first 18 statements"
    override val step = 18
}

/**
 * 19
 */
class NonPrioritizedTest19 : NonPrioritizedBaseTest() {
    override val name = "NonPrioritizedTest19"
    override val description = "Checks all statements"
    override val step = 19
}

/**
 * Tests non Prio
 */
class NonPrioritizedTestPerformsIrrigatingOnTile3 : TestExtension() {
    override val name = "NonPrioritizedTestPerformsIrrigatingOnTile3"
    override val description = "Tests nonPrioActions"

    override val farms = "farmActionTests/farm.json"
    override val scenario = "onefieldtest/noscenario.json"
    override val map = "farmActionTests/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 3
    override val startYearTick = 9

    val qwertz = "[IMPORTANT] Farm Action: Machine 2 performs IRRIGATING on tile 3 for 6 days."

    override suspend fun run() {
        skipUntilString(qwertz)
        assertCurrentLine(qwertz)
    }
}
