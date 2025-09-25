package de.unisaarland.cs.se.selab.systemtest.selab25

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Base for all Parser Tests
 */
abstract class ParserTest(
    val failingFile: ParserFile?,
    val mapFileName: String = "validMap.json",
    val farmsFileName: String = "validFarms.json",
    val scenarioFileName: String = "validScenario.json"
) : ExampleSystemTestExtension() {
    override val map: String = "$PARSER_FILES_DIRECTORY_PATH/$mapFileName"
    override val farms: String = "$PARSER_FILES_DIRECTORY_PATH/$farmsFileName"
    override val scenario: String = "$PARSER_FILES_DIRECTORY_PATH/$scenarioFileName"
    override val maxTicks: Int = 1
    override val startYearTick: Int = 1
    override val logLevel: String = "DEBUG"
    override val name: String = "Parser.${this.javaClass.name}"

    override suspend fun run() {
        if (failingFile == ParserFile.MAP) {
            fileIsInValid(mapFileName)
            assertEnd()
            return
        }
        fileIsValid(mapFileName)
        if (failingFile == ParserFile.FARMS) {
            fileIsInValid(farmsFileName)
            assertEnd()
            return
        }
        fileIsValid(farmsFileName)
        if (failingFile == ParserFile.SCENARIO) {
            fileIsInValid(scenarioFileName)
            assertEnd()
            return
        }
        fileIsValid(scenarioFileName)
        assertNextLine("[INFO] Simulation Info: Simulation started at tick $startYearTick within the year.")
    }

    protected suspend fun fileIsValid(filename: String) {
        assert(
            skipUntilLogType(LogLevel.INFO, LogType.INITIALIZATION_INFO) ==
                "[INFO] Initialization Info: $filename successfully parsed and validated."
        )
    }

    protected suspend fun fileIsInValid(filename: String) {
        assert(
            skipUntilLogType(LogLevel.IMPORTANT, LogType.INITIALIZATION_INFO) ==
                "[IMPORTANT] Initialization Info: $filename is invalid."
        )
    }

    protected companion object ParserTestCompanion {
        const val PARSER_FILES_DIRECTORY_PATH: String = "parser"
    }
}

/**
 * Enum used to declare which parsed file should fail.
 */
enum class ParserFile {
    MAP,
    FARMS,
    SCENARIO
}

/**
 * Test the parser with everything
 */
class ValidParser : ParserTest(null) {
    override val description: String = "Test the parser with everything"
}

/**
 * Test the parser for Farm 0 without a shed
 */
class ShedlessFarm : ParserTest(ParserFile.MAP, mapFileName = "invalidMapNoShed.json") {
    override val description: String = "Farm 0 without a shed"
}

/**
 * Test the parser for a Village with airflow == true and a direction
 */
class WindyVillage : ParserTest(ParserFile.MAP, mapFileName = "invalidMapVillageDirection.json") {
    override val description: String = "Village with airflow == true and a direction"
}

/**
 * Test the parser for a Village near a Forest
 */
class ForestVillage : ParserTest(ParserFile.MAP, mapFileName = "invalidMapVillageNearForest.json") {
    override val description: String = "Village near a Forest"
}

/**
 * Test the parser for a Farm 0 without Sowing Machine with Sowing Plan
 */
class FarmSowless : ParserTest(ParserFile.FARMS, farmsFileName = "invalidFarmsNoSowingMachine.json") {
    override val description: String = "Farm 0 without Sowing Machine with Sowing Plan"
}

/**
 * Test the parser for a Cloud on a Village
 */
class CloudyVillage : ParserTest(ParserFile.SCENARIO, scenarioFileName = "invalidScenarioCloudOnVillage.json") {
    override val description: String = "Cloud on a Village"
}
