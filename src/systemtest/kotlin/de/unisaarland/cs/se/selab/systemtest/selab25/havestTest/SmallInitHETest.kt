package de.unisaarland.cs.se.selab.systemtest.selab25.havestTest

import de.unisaarland.cs.se.selab.farms.Action
import de.unisaarland.cs.se.selab.plants.PlantType
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.SimulationTestExtension
/***/
class SmallInitHETest : SimulationTestExtension("smallInitHETest") {
    override val description = "bla"
    override val maxTicks = 1
    override val startYearTick = 16
    override suspend fun run() {
        skipUntilLogType(LogLevel.DEBUG, "Harvest Estimation")
        assertCurrentLine(actionNotPerformed(5, listOf(Action.CUTTING, Action.IRRIGATING)))
        assertNextLine(harvestEstimate(5, 0, PlantType.GRAPE))
        assertNextLine(actionNotPerformed(6, listOf(Action.IRRIGATING)))
        assertNextLine(harvestEstimate(6, 647_850, PlantType.ALMOND))
        assertNextLine(harvestEstimate(7, 1_115_370, PlantType.APPLE))
        assertNextLine(actionNotPerformed(8, listOf(Action.HARVESTING)))
        assertNextLine(harvestEstimate(8, 0, PlantType.CHERRY))
    }
}
