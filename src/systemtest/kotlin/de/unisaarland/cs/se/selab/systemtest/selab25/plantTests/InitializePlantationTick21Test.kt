package de.unisaarland.cs.se.selab.systemtest.selab25.plantTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * Tests if the harvest estimate
 */
class InitializePlantationTick21Test : TestExtension() {
    override val name = "InitializePlantationTick21Test"
    override val description = "Tests if the harvest estimate is set correctly in tick 21."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "plantTests/farmsInitPlantation.json"
    override val scenario = "plantTests/scenario.json"
    override val map = "plantTests/mapInitPlantation.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 21

    override suspend fun run() {
        skipUntilLogType(LogLevel.IMPORTANT, "Simulation Statistics")
        assertCurrentLine("[IMPORTANT] Simulation Statistics: Farm 0 collected 0 g of harvest.")
        listOf("POTATO", "WHEAT", "OAT", "PUMPKIN", "APPLE", "GRAPE", "ALMOND", "CHERRY").forEach { plant ->
            assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of $plant harvested: 0 g.")
        }
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 4730000 g."
        )
    }
}
