package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Detekt
 */
class BeeHappyTest3Overlapping : TestExtension() {
    override val name = "BeeHappy3"
    override val description = "Tests BeeHappyImpactStacked."

    override val farms = "incidentTest/farms.json"
    override val scenario = "incidentTest/scenarioBeeHappy4.json"
    override val map = "incidentTest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 8

    override suspend fun run() {
        skipUntilLogType(LogLevel.IMPORTANT, "Incident:")
        assertCurrentLine("[IMPORTANT] Incident: Incident 0 of type BEE_HAPPY happened and affected tiles 5.")
        assertNextLine("[IMPORTANT] Incident: Incident 1 of type BEE_HAPPY happened and affected tiles 5.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 5 changed to 1499553 g of APPLE.")
        skipUntilLogType(LogLevel.IMPORTANT, "Incident:")
        assertCurrentLine("[IMPORTANT] Incident: Incident 2 of type BEE_HAPPY happened and affected tiles 5.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 5 changed to 1082240 g of APPLE.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 6 changed to 900000 g of POTATO.")
    }
}
