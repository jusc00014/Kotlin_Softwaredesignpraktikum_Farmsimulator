package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Detekt
 */
class AnimalAttackTestStacking : TestExtension() {
    override val name = "AnimalAttack2"
    override val description = "Tests AnimalAttack and Estimate."

    override val farms = "incidentTest/farms.json"
    override val scenario = "incidentTest/scenarioAnimalAttackStacking.json"
    override val map = "incidentTest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 1

    override suspend fun run() {
        skipUntilLogType(LogLevel.IMPORTANT, "Incident:")
        assertCurrentLine(
            "[IMPORTANT] Incident: Incident 0 of type ANIMAL_ATTACK happened and affected tiles 5,6,7,12."
        )
        assertNextLine("[IMPORTANT] Incident: Incident 1 of type ANIMAL_ATTACK happened and affected tiles 5,6,7,12.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 5 changed to 1239300 g of APPLE.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 7 changed to 300000 g of GRAPE.")
    }
}
