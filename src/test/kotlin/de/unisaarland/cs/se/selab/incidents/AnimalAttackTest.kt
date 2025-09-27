package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.Constants
import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.BoardHandler
import de.unisaarland.cs.se.selab.board.Coordinate
import de.unisaarland.cs.se.selab.board.Plantation
import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.board.TileType
import de.unisaarland.cs.se.selab.farms.Action
import de.unisaarland.cs.se.selab.farms.Farm
import de.unisaarland.cs.se.selab.farms.Machine
import de.unisaarland.cs.se.selab.parser.MapParser
import de.unisaarland.cs.se.selab.parser.ScenarioParser
import de.unisaarland.cs.se.selab.plants.Plant
import de.unisaarland.cs.se.selab.plants.PlantType
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class AnimalAttackTest {
    private val tile1 = Plantation(
        1,
        Coordinate(0, 2),
        null,
        1,
        TileType.FIELD,
        30000,
        Plant(PlantType.GRAPE, Constants.grape, 1)
    )
    private val tile2 = Plantation(
        2,
        Coordinate(2, 4),
        null,
        1,
        TileType.FIELD,
        30000,
        Plant(PlantType.GRAPE, Constants.grape, 1)
    )
    private val tile5 = Tile(
        5,
        Coordinate(0, 4),
        null,
        false,
        null,
        TileType.FOREST
    )
    private val tile7 = Tile(
        7,
        Coordinate(2, 2),
        null,
        false,
        null,
        TileType.FOREST
    )
    private val tile9 = Tile(
        9,
        Coordinate(1, 3),
        null,
        true,
        1,
        TileType.FARMSTEAD
    )
    private val machines =
        mapOf(
            Pair(1, Machine(1, listOf(Action.SOWING), listOf(PlantType.POTATO), duration = 13, tile9, 0)),
            Pair(
                2,
                Machine(
                    2,
                    listOf(Action.IRRIGATING),
                    listOf(PlantType.PUMPKIN, PlantType.APPLE, PlantType.POTATO),
                    duration = 1,
                    tile9,
                    0
                )
            )
        )
    private val farm = Farm(
        1,
        listOf(9),
        emptyList(),
        listOf(1, 2),
        listOf(1, 2),
        mutableListOf()
    )
    val board = BoardData(mapOf(1 to tile1, 2 to tile2, 5 to tile5, 7 to tile7, 9 to tile9))
    private val mapParser = MapParser(mutableMapOf())
    private val plantData = mapParser.parse("src/systemtest/resources/example/map.json", 0).second
    val init = plantData[PlantType.GRAPE]?.initialHarvestEstimate

    @Test
    fun animalAttackSimple() {
        val attack = AnimalAttack(1, 1, setOf(tile1))
        val incidentHandler = IncidentHandler(mutableMapOf(1 to listOf(attack)))
        incidentHandler.executeIncidents(1)
        val boardHandler = BoardHandler()
        boardHandler.computeEstimate(1, board)
        assertTrue { tile1.plant.getHarvestEstimate() * 2 == init }
    }

    @Test
    fun twoAnimalAttacks() {
        val attack1 = AnimalAttack(1, 1, setOf(tile1))
        val attack2 = AnimalAttack(2, 1, setOf(tile1, tile2))
        val incidentHandler = IncidentHandler(mutableMapOf(1 to listOf(attack1, attack2)))
        incidentHandler.executeIncidents(1)
        val boardHandler = BoardHandler()
        boardHandler.computeEstimate(1, board)
        assertTrue { init == 4 * tile1.plant.getHarvestEstimate() }
        assertTrue { init == 2 * tile2.plant.getHarvestEstimate() }
    }

    @Test
    fun animalAttackWithParsing() {
        val json = "src/systemtest/resources/incidentTest/scenario.json"
        val scenarioParser = ScenarioParser()
        val incidents = scenarioParser.parse(json, board, 2, machines, listOf(farm), 1).first
        val incidentHandler = IncidentHandler(mutableMapOf(1 to listOf(incidents[0])))
        incidentHandler.executeIncidents(1)
        val boardHandler = BoardHandler()
        boardHandler.computeEstimate(1, board)
        assertTrue { tile1.plant.getHarvestEstimate() * 2 == init }
    }
}
