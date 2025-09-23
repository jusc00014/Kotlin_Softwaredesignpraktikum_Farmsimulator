package general

import de.unisaarland.cs.se.selab.Constants
import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.BoardHandler
import de.unisaarland.cs.se.selab.board.Coordinate
import de.unisaarland.cs.se.selab.board.Direction
import de.unisaarland.cs.se.selab.board.Field
import de.unisaarland.cs.se.selab.board.Plantation
import de.unisaarland.cs.se.selab.board.TileType
import de.unisaarland.cs.se.selab.plants.Plant
import de.unisaarland.cs.se.selab.plants.PlantType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ReduceSoilTest {
    lateinit var wheatField: Field
    lateinit var pumpkinField: Field
    lateinit var almondPlantation: Plantation
    lateinit var grapePlantation: Plantation
    lateinit var futureVillage: Field
    lateinit var boardData: BoardData
    lateinit var boardHandler: BoardHandler

    @BeforeEach
    fun setUp() {
        val plantList = makeStuffUp().iterator()
        val wheatPlant = plantList.next()
        val pumpkinPlant = plantList.next()
        val almondPlant = plantList.next()
        val grapePlant = plantList.next()
        wheatField = Field(
            1,
            Coordinate(0, 0),
            Direction.NORTH,
            1,
            TileType.FIELD,
            400,
            wheatPlant,
            setOf(PlantType.WHEAT)
        )
        pumpkinField = Field(
            2,
            Coordinate(2, 0),
            Direction.NORTH,
            1,
            TileType.FIELD,
            1000,
            pumpkinPlant,
            setOf(PlantType.PUMPKIN)
        )
        almondPlantation = Plantation(
            3,
            Coordinate(4, 0),
            Direction.NORTH,
            1,
            TileType.PLANTATION,
            50,
            almondPlant
        )
        grapePlantation = Plantation(
            4,
            Coordinate(6, 0),
            Direction.NORTH,
            1,
            TileType.PLANTATION,
            450,
            grapePlant
        )
        futureVillage = Field(
            5,
            Coordinate(8, 0),
            Direction.NORTH,
            1,
            TileType.FIELD,
            100,
            pumpkinPlant,
            setOf(PlantType.PUMPKIN)
        )
    }

    fun makeStuffUp(): List<Plant> {
        val wheatPlant = Plant(
            PlantType.WHEAT,
            Constants.wheat,
            1
        )
        val pumpkinPlant = Plant(
            PlantType.PUMPKIN,
            Constants.pumpkin,
            1
        )
        val almondPlant = Plant(
            PlantType.ALMOND,
            Constants.almond,
            1
        )
        val grapePlant = Plant(
            PlantType.GRAPE,
            Constants.grape,
            1
        )
        boardData = BoardData(
            mapOf(
                1 to wheatField,
                2 to pumpkinField,
                3 to almondPlantation,
                4 to grapePlantation,
                5 to futureVillage
            )
        )
        boardHandler = BoardHandler()
        return listOf(wheatPlant, pumpkinPlant, almondPlant, grapePlant)
    }

    @Test
    fun reduceSoilTestVillageExpansion() {
        futureVillage.type = TileType.VILLAGE
        assertTrue(wheatField.loseMoisture())
        assertFalse(pumpkinField.loseMoisture())
        assertTrue(almondPlantation.loseMoisture())
        assertFalse(grapePlantation.loseMoisture())
        assertEquals(4, boardData.getFertiles().values.size)
    }
}
