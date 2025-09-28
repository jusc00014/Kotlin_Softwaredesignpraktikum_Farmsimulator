package de.unisaarland.cs.se.selab.parser

import de.unisaarland.cs.se.selab.Constants
import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.Coordinate
import de.unisaarland.cs.se.selab.board.Field
import de.unisaarland.cs.se.selab.board.Plantation
import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.board.TileType
import de.unisaarland.cs.se.selab.clouds.CloudData
import de.unisaarland.cs.se.selab.farms.Action
import de.unisaarland.cs.se.selab.farms.Farm
import de.unisaarland.cs.se.selab.farms.Machine
import de.unisaarland.cs.se.selab.farms.SowingPlan
import de.unisaarland.cs.se.selab.incidents.Incident
import de.unisaarland.cs.se.selab.plants.Plant
import de.unisaarland.cs.se.selab.plants.PlantType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import kotlin.test.assertIs

class ScenarioParserTest {
    private fun field(id: Int, x: Int, y: Int) =
        Field(
            id = id,
            coord = Coordinate(x, y),
            airflow = null,
            farmID = 0,
            type = TileType.FIELD,
            moistureCapacity = 6000,
            plant = Plant(
                type = PlantType.POTATO,
                data = Constants.potato,
                yearTick = Constants.APR_1
            ),
            possiblePlants = setOf(PlantType.POTATO)
        )

    private fun plantation(id: Int, x: Int, y: Int) =
        Plantation(
            id = id,
            coord = Coordinate(x, y),
            airflow = null,
            farmID = 0,
            type = TileType.PLANTATION,
            moistureCapacity = 6000,
            plant = Plant(
                type = PlantType.APPLE,
                data = Constants.apple,
                yearTick = Constants.NOV_1
            )
        )

    private fun farmstead(id: Int, x: Int, y: Int) =
        Tile(
            id = id,
            coord = Coordinate(x, y),
            airflow = null,
            farmID = 0,
            shed = true,
            type = TileType.FARMSTEAD
        )

    private fun miscTile(id: Int, x: Int, y: Int, type: TileType) =
        Tile(
            id = id,
            coord = Coordinate(x, y),
            airflow = null,
            type = type,
        )

    fun data(): Triple<Map<Int, Tile>, Map<Int, Machine>, List<Farm>> {
        val farmstead = farmstead(0, -1, -1)
        val map = listOf(
            farmstead,
            field(1, 0, 0),
            miscTile(2, 1, -1, TileType.MEADOW),
            plantation(3, 2, 0),
            miscTile(4, 1, 1, TileType.FOREST),
            miscTile(5, 0, 2, TileType.ROAD),
            miscTile(6, -1, 1, TileType.VILLAGE),
            field(7, 2, 2)
        ).associateBy { it.id }
        val machines = listOf(
            Machine(0, actions = listOf(Action.SOWING), listOf(PlantType.POTATO), 1, farmstead)
        ).associateBy { it.id }
        val farms = listOf(
            Farm(
                id = 0,
                farmsteads = listOf(0),
                fields = listOf(1, 7),
                plantages = listOf(3),
                machines = listOf(0),
                plans = mutableListOf(
                    SowingPlan(
                        id = 0,
                        tick = 0,
                        plant = PlantType.POTATO,
                        fields = listOf(1, 7)
                    )
                )
            )
        )
        return Triple(map, machines, farms)
    }

    private fun runCustom(
        jsonFileName: String,
        board: BoardData,
        maxTick: Int,
        machines: Map<Int, Machine>,
        farms: List<Farm>,
        yearTick: Int
    ): Result<Pair<List<Incident>, CloudData>> {
        return runCatching {
            ScenarioParser().parse(
                "src/test/resources/scenarioParserTest/$jsonFileName",
                board,
                maxTick,
                machines,
                farms,
                yearTick
            )
        }
    }

    private fun run(
        jsonFileName: String,
        maxTick: Int = 1,
        yearTick: Int = Constants.APR_2
    ): Result<Pair<List<Incident>, CloudData>> {
        val (map, machines, farms) = data()
        return runCustom(
            jsonFileName,
            maxTick = maxTick,
            yearTick = yearTick,
            board = BoardData(map),
            machines = machines,
            farms = farms,
        )
    }

    @Test
    fun cloudsSameTileID() {
        val r = run(jsonFileName = "cloudsSameTileID.json")
        val ex = r.exceptionOrNull()
        assertNotNull(ex)
        assertIs<IllegalArgumentException>(ex)
        assertEquals("Failed requirement.", ex.message)
    }

    @Test
    fun cloudsSameID() {
        val r = run(jsonFileName = "cloudsSameID.json")
        val ex = r.exceptionOrNull()
        assertNotNull(ex)
        assertIs<IllegalArgumentException>(ex)
        assertEquals("Failed requirement.", ex.message)
    }

    @Test
    fun cloudOnVillage() {
        val r = run(jsonFileName = "cloudOnVillage.json")
        val ex = r.exceptionOrNull()
        assertNotNull(ex)
        assertIs<IllegalArgumentException>(ex)
        assertEquals("Failed requirement.", ex.message)
    }

    @Test
    fun cloudOnInvalidTile() {
        val r = run(jsonFileName = "cloudOnInvalidTile.json")
        val ex = r.exceptionOrNull()
        assertNotNull(ex)
        assertIs<IllegalArgumentException>(ex)
        assertEquals("Failed requirement.", ex.message)
    }
}
