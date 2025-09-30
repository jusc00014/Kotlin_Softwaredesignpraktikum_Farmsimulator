package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Detekt
 */
class DroughtTestDryDryDesert : TestExtension() {
    override val name = "DroughtTest2"
    override val description = "Tests Drought moisture and Estimate impact."

    override val farms = "incidentTest/farms.json"
    override val scenario = "incidentTest/scenarioDrought2.json"
    override val map = "incidentTest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 1

    override suspend fun run() {
        skipUntilLogType(LogLevel.IMPORTANT, "Incident")
        assertCurrentLine("[IMPORTANT] Incident: Incident 0 of type DROUGHT happened and affected tiles 6.")
        assertNextLine(
            "[IMPORTANT] Incident: Incident 1 of type DROUGHT happened and affected tiles 6,7."
        )
        skipUntilLogType(LogLevel.INFO, "Harvest Estimate")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 7 changed to 0 g of GRAPE.")
        skipUntilLogType(LogLevel.INFO, "Soil Moisture")
        assertCurrentLine(
            "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles."
        )
        assertNextLine("[IMPORTANT] Cloud Rain: Cloud 0 on tile 6 rained down 2500 L water.")
    }
}
