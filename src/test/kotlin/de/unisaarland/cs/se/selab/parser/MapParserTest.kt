package de.unisaarland.cs.se.selab.parser

import de.unisaarland.cs.se.selab.Constants
import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.Coordinate
import de.unisaarland.cs.se.selab.board.Direction
import de.unisaarland.cs.se.selab.board.Fertile
import de.unisaarland.cs.se.selab.board.Field
import de.unisaarland.cs.se.selab.board.Plantation
import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.board.TileType
import de.unisaarland.cs.se.selab.plants.Plant
import de.unisaarland.cs.se.selab.plants.PlantData
import de.unisaarland.cs.se.selab.plants.PlantType
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class MapParserTest {
    private companion object {
        const val PARSER_SUB_DIR = "parser"

        val fullValidT0 = Tile(0, Coordinate(-1, -1), Direction.NORTHWEST, true, 0, TileType.FARMSTEAD)
        val fullValidT1 = Field(
            1,
            Coordinate(0, 0),
            null,
            0,
            TileType.FIELD,
            1_000,
            Plant(PlantType.POTATO, Constants.potato, 21),
            setOf(
                PlantType.POTATO,
                PlantType.WHEAT,
                PlantType.OAT,
                PlantType.PUMPKIN
            )
        )
        val fullValidT2 = Tile(2, Coordinate(1, -1), null, false, null, TileType.FOREST)
        val fullValidT3 = Tile(3, Coordinate(3, -1), null, false, null, TileType.MEADOW)
        val fullValidT4 = Plantation(
            4,
            Coordinate(2, 0),
            Direction.SOUTH,
            0,
            TileType.PLANTATION,
            10_000,
            Plant(
                PlantType.APPLE,
                Constants.apple,
                21
            )
        )
        val fullValidT5 = Tile(5, Coordinate(4, 0), null, false, null, TileType.ROAD)
        val fullValidT6 = Tile(6, Coordinate(6, 0), null, false, null, TileType.VILLAGE)
    }

    private fun run(
        subDirPath: String,
        jsonFileName: String,
        yearTick: Int = 21
    ): Result<Pair<BoardData, Map<PlantType, PlantData>>> {
        return runCatching {
            MapParser(mutableMapOf()).parse(
                "src/systemtest/resources/$subDirPath/$jsonFileName",
                yearTick
            )
        }
    }

    fun testFertile(actual: Tile, expected: Fertile) {
        assertIs<Fertile>(actual)
        assertEquals(expected.getMoistureCapacity(), actual.getMoistureCapacity())
        when (expected) {
            is Plantation -> {
                assertIs<Plantation>(actual)
                assertEquals(expected.plant, actual.plant)
            }
            is Field -> {
                assertIs<Field>(actual)
                assertEquals(expected.possiblePlants, actual.possiblePlants)
            }
        }
    }

    fun testTile(actual: Tile?, expected: Tile) {
        assertNotNull(actual)
        assertEquals(expected.id, actual.id)
        assertEquals(expected.coord, actual.coord)
        assertEquals(expected.type, actual.type)
        assertEquals(expected.farmID, actual.farmID)
        assertEquals(expected.airflow, actual.airflow)
        assertEquals(expected.shed, actual.shed)
        if (expected is Fertile) {
            testFertile(actual, expected)
        }
    }

    @Test
    fun fullValid() {
        val res = run(PARSER_SUB_DIR, "mapFull.json", 21)
        val data = res.getOrNull()
        assertNotNull(data)
        val (boardData, _) = data
        testTile(boardData.getTileById(0), fullValidT0)
        testTile(boardData.getTileById(1), fullValidT1)
        testTile(boardData.getTileById(2), fullValidT2)
        testTile(boardData.getTileById(3), fullValidT3)
        testTile(boardData.getTileById(4), fullValidT4)
        testTile(boardData.getTileById(5), fullValidT5)
        testTile(boardData.getTileById(6), fullValidT6)
        // PlantData is not asserted, insertion order matters in map.equals (why? -.-)
    }

    @Test
    fun ownedRoad() {
        val res = run(PARSER_SUB_DIR, "mapOwnedRoad.json", 21)
        val ex = res.exceptionOrNull()
        assertNotNull(ex)
        assertIs<IllegalArgumentException>(ex)
        assertEquals("TileIDs have to be unique. 1 is not unique", ex.message)
    }

    @Test
    fun sameID() {
        val res = run(PARSER_SUB_DIR, "mapSameID.json", 21)
        val ex = res.exceptionOrNull()
        assertNotNull(ex)
        assertIs<IllegalArgumentException>(ex)
        assertEquals("TileIDs have to be unique. 0 is not unique", ex.message)
    }

    @Test
    fun villageDirection() {
        val res = run(PARSER_SUB_DIR, "mapVillageDirection.json", 21)
        val ex = res.exceptionOrNull()
        assertNotNull(ex)
        assertIs<IllegalArgumentException>(ex)
        assertEquals("Failed requirement.", ex.message)
    }

    @Test
    fun sameCoordinates() {
        val res = run(PARSER_SUB_DIR, "mapSameCoordinates.json", 21)
        val ex = res.exceptionOrNull()
        assertNotNull(ex)
        assertIs<IllegalArgumentException>(ex)
        assertEquals("Coordinate of tile 2 is already used", ex.message)
    }

    @Test
    fun invalidCategoryForCoordinate() {
        val res = run(PARSER_SUB_DIR, "mapInvalidCategoryForCoord.json", 21)
        val ex = res.exceptionOrNull()
        assertNotNull(ex)
        assertIs<IllegalArgumentException>(ex)
        assertEquals("invalid category MEADOW on tile with coordinates Coordinate(x=2, y=0)", ex.message)
    }

    @Test
    fun villageNearForest() {
        val res = run(PARSER_SUB_DIR, "mapVillageNearForest.json")
        val ex = res.exceptionOrNull()
        assertNotNull(ex)
        assertIs<IllegalArgumentException>(ex)
        assertEquals("Village shouldn't adjoin forest tiles", ex.message)
    }

    @Test
    fun shedWrongNeighbourhood() {
        val res = run(PARSER_SUB_DIR, "map2ShedWrongNeighbourhood.json")
        val ex = res.exceptionOrNull()
        assertNotNull(ex)
        assertIs<IllegalArgumentException>(ex)
        assertEquals("Farmstead cannot adjoin fields or plantations of different farms", ex.message)
    }
}
