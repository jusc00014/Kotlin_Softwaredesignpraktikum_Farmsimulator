package de.unisaarland.cs.se.selab.systemtest.selab25

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

const val PARSER_FILES_DIRECTORY_PATH: String = "parser"
const val MAP_2: String = "map2.json"
const val FARMS_2: String = "farms2.json"

/**
 * Base for all Parser Tests
 */
abstract class ParserTest(
    val failingFile: ParserFile?,
    val mapFileName: String = "map.json",
    val farmsFileName: String = "farms.json",
    val scenarioFileName: String = "scenario.json"
) : ExampleSystemTestExtension() {
    override val map: String = "$PARSER_FILES_DIRECTORY_PATH/$mapFileName"
    override val farms: String = "$PARSER_FILES_DIRECTORY_PATH/$farmsFileName"
    override val scenario: String = "$PARSER_FILES_DIRECTORY_PATH/$scenarioFileName"
    override val maxTicks: Int = 0
    override val startYearTick: Int = 1
    override val logLevel: String = "DEBUG"
    override val name: String = "Parser.${this.javaClass.simpleName}"

    override suspend fun run() {
        if (failingFile == ParserFile.MAP) {
            fileIsInValid(mapFileName)
            assertEnd()
            return
        }
        fileIsValid(mapFileName)
        if (failingFile == ParserFile.FARMS) {
            fileIsInValid(farmsFileName)
            return
        }
        fileIsValid(farmsFileName)
        if (failingFile == ParserFile.SCENARIO) {
            fileIsInValid(scenarioFileName)
            return
        }
        fileIsValid(scenarioFileName)
        assertNextLine("[INFO] Simulation Info: Simulation started at tick $startYearTick within the year.")
    }

    protected suspend fun fileIsValid(filename: String) {
        assertNextLine("[INFO] Initialization Info: $filename successfully parsed and validated.")
    }

    protected suspend fun fileIsInValid(filename: String) {
        assertNextLine("[IMPORTANT] Initialization Info: $filename is invalid.")
        assertEnd()
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
class FullData : ParserTest(
    null,
    mapFileName = "mapFull.json",
    farmsFileName = "farmsFull.json",
    scenarioFileName = "scenarioFull.json"
) {
    override val description: String = "Everything"
    override val maxTicks: Int = 1
}

/**
 * Test the parser with 2 Farms
 */
class FarmsTimes2 : ParserTest(null, mapFileName = MAP_2, farmsFileName = FARMS_2) {
    override val description: String = "2 Farms"
}

// region Map
/**
 * Test the parser for a Village with airflow == true and a direction
 */
class WindyVillage : ParserTest(ParserFile.MAP, mapFileName = "mapVillageDirection.json") {
    override val description: String = "Village with airflow == true and a direction"
}

/**
 * Test the parser for a Village near a Forest
 */
class ForestVillage : ParserTest(ParserFile.MAP, mapFileName = "mapVillageNearForest.json") {
    override val description: String = "Village near a Forest"
}

/**
 * Test the parser for a Shed of Farm 1 near a Plantation of Farm 0
 */
class ShedWrongNeighbourhood : ParserTest(
    ParserFile.MAP,
    mapFileName = "map2ShedWrongNeighbourhood.json",
    farmsFileName = FARMS_2
) {
    override val description: String = "Shed of Farm 1 near a Plantation of Farm 0"
}

/**
 * Test the parser for a Road with farm id
 */
class OwnedRoad : ParserTest(ParserFile.MAP, mapFileName = "mapOwnedRoad.json") {
    override val description: String = "Road with farm id"
}

/**
 * Test the parser for Tiles with same ID
 */
class TileSameID : ParserTest(ParserFile.MAP, mapFileName = "mapSameID.json") {
    override val description: String = "Tiles with same ID"
}

/**
 * Test the parser for Tile with same Coordinates
 */
class TileSameCoordinates : ParserTest(ParserFile.MAP, mapFileName = "mapSameCoordinates.json") {
    override val description: String = "Tiles with same coordinates"
}

/**
 * Test the parser for a Meadow as an Octagonal Tile
 */
class InvalidCategoryForCoord : ParserTest(ParserFile.MAP, mapFileName = "mapInvalidCategoryForCoord.json") {
    override val description: String = "Meadow as an Octagonal Tile"
}

/**
 * Test the parser for a Village with airflow = true
 */
class VillageWithAirflow : ParserTest(ParserFile.MAP, mapFileName = "mapVillageWithAirflow.json") {
    override val description: String = "Village with airflow = true"
}

// endregion

// region Farms
/**
 * Test the parser for Farm 0 without a shed
 */
class ShedlessFarm : ParserTest(ParserFile.FARMS, mapFileName = "mapNoShed.json") {
    override val description: String = "Farm 0 without a shed"
}

/**
 * Test the parser for a Machine Clone
 */
class MachineClone : ParserTest(ParserFile.FARMS, mapFileName = MAP_2, farmsFileName = "farms2Clone.json") {
    override val description: String = "Machine Clone"
}

/**
 * Test the parser for Machines with the same name
 */
class MachineDoppelganger : ParserTest(
    ParserFile.FARMS,
    mapFileName = MAP_2,
    farmsFileName = "farms2Doppelganger.json"
) {
    override val description: String = "Machines with the same name"
}

/**
 * Test the parser for a Machine at the wrong shed
 */
class MachineWrongShed : ParserTest(
    ParserFile.FARMS,
    mapFileName = MAP_2,
    farmsFileName = "farms2WrongShed.json"
) {
    override val description: String = "Machine at the wrong shed"
}

/**
 * Test the parser for a Machine which location is not a shed
 */
class MachineHomeless : ParserTest(
    ParserFile.FARMS,
    mapFileName = MAP_2,
    farmsFileName = "farmsHomelessMachine.json"
) {
    override val description: String = "Machine which location is not a shed"
}

/**
 * Test the parser for Farms with the same name
 */
class FarmImposter : ParserTest(
    ParserFile.FARMS,
    mapFileName = MAP_2,
    farmsFileName = "farms2Imposter.json"
) {
    override val description: String = "Farms with the same name"
}

/**
 * Test the parser for Farms with shared Farmstead
 */
class FarmSplitCustody : ParserTest(
    ParserFile.FARMS,
    mapFileName = MAP_2,
    farmsFileName = "farms2SplitCustody.json"
) {
    override val description: String = "Farms with shared Farmstead"
}

/**
 * SowingPlan after maxTick
 */
class FarmSowingAfterTheEnd : ParserTest(
    ParserFile.FARMS,
    mapFileName = "mapWField.json",
    farmsFileName = "farmsSowingAfterTheEnd.json",
) {
    override val description: String = "SowingPlan after maxTick"
}

/**
 * Invalid Plantation Tile
 */
class FarmInvalidPlantationTile : ParserTest(
    ParserFile.FARMS,
    farmsFileName = "farmsInvalidPlantationTile.json"
) {
    override val description: String = "Invalid plantation tile"
}

/**
 * Plantation as Field declared
 */
class FarmPlantationIdentifiesAsField : ParserTest(ParserFile.FARMS, farmsFileName = "farmsPlantationAsField.json") {
    override val description: String = "Plantation as Field declared"
}

/**
 * Sowing plan without Field
 */
class FarmNoFieldsWSowingPlan : ParserTest(
    ParserFile.FARMS,
    farmsFileName = "farmsNoFieldsWSowingPlan.json"
) {
    override val description: String = "No fields in farm with sowing plan"
}

/**
 * Farm without Plantations and Fields
 */
class FarmWOPlantationsAndFields : ParserTest(
    ParserFile.FARMS,
    farmsFileName = "farmsNoPlantations.json"
) {
    override val description: String = "Farm without Plantations and Fields"
}

/**
 * Machine with the same Action twice
 */
class MachineDuplicateActions : ParserTest(
    null,
    farmsFileName = "farmsDuplicateActions.json"
) {
    override val description: String = "Machine with the same Action twice"
}
// endregion

// region Scenario
/**
 * Test the parser for a Cloud on a Village
 */
class CloudyVillage : ParserTest(ParserFile.SCENARIO, scenarioFileName = "scenarioCloudOnVillage.json") {
    override val description: String = "Cloud on a Village"
}

/**
 * Test the parser for a 2 Clouds on the same Tile
 */
class TwinClouds : ParserTest(ParserFile.SCENARIO, scenarioFileName = "scenarioTwinCloud.json") {
    override val description: String = "2 Clouds on the same Tile"
}

/**
 * Test the parser for CityExpansion expanding near a Forest
 */
class VillageExpansionNearForest : ParserTest(
    ParserFile.SCENARIO,
    mapFileName = "mapVillageExpansionNearForest.json",
    scenarioFileName = "scenarioVillageExpansionNearForest.json"
) {
    override val description: String = "CityExpansion expanding near a Forest"
}

/**
 * Test the parser for CityExpansion expanding on a Plantation
 */
class VillagePlantationTakeover : ParserTest(
    ParserFile.SCENARIO,
    mapFileName = "mapVillagePlantationTakeover.json",
    scenarioFileName = "scenarioVillagePlantationTakeover.json"
) {
    override val description: String = "CityExpansion expanding on a Plantation"
}
//endregion
