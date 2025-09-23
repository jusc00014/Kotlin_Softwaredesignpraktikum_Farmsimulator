package general

import de.unisaarland.cs.se.selab.Constants
import de.unisaarland.cs.se.selab.board.Coordinate
import de.unisaarland.cs.se.selab.board.Direction
import de.unisaarland.cs.se.selab.board.Field
import de.unisaarland.cs.se.selab.board.Plantation
import de.unisaarland.cs.se.selab.board.TileType
import de.unisaarland.cs.se.selab.farms.Action
import de.unisaarland.cs.se.selab.plants.Plant
import de.unisaarland.cs.se.selab.plants.PlantType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PlantFallowTest {
    var wheatField: Field? = null
    var pumpkinField: Field? = null
    var almondPlantation: Plantation? = null
    var grapePlantation: Plantation? = null

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
            1000,
            almondPlant
        )
        grapePlantation = Plantation(
            4,
            Coordinate(6, 0),
            Direction.NORTH,
            1,
            TileType.PLANTATION,
            1000,
            grapePlant
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
        return listOf(wheatPlant, pumpkinPlant, almondPlant, grapePlant)
    }

    @Test
    fun fallowTest() {
        wheatField?.plant?.sow(PlantType.WHEAT, wheatField?.plant!!.data, 20)
        wheatField?.updateHarvestEstimate(20)
        wheatField?.performAction(Action.HARVESTING, Constants.JUN_1)
        for (i in 12..15) {
            assertTrue(wheatField?.plant!!.isFallow(i))
            wheatField?.updateHarvestEstimate(i)
        }
        wheatField?.updateHarvestEstimate(16)
        assertFalse(wheatField?.plant!!.isFallow(16))

        pumpkinField?.plant?.sow(PlantType.PUMPKIN, pumpkinField?.plant!!.data, Constants.JUN_1)
        pumpkinField?.updateHarvestEstimate(11)
        pumpkinField?.performAction(Action.HARVESTING, Constants.OCT_1)
        for (i in 20..23) {
            assertTrue(pumpkinField?.plant!!.isFallow(i))
            pumpkinField?.updateHarvestEstimate(i)
        }
        pumpkinField?.updateHarvestEstimate(24)
        assertFalse(pumpkinField?.plant!!.isFallow(24))

        wheatField?.plant?.sow(PlantType.WHEAT, wheatField?.plant!!.data, 20)
        wheatField?.updateHarvestEstimate(20)
        wheatField?.drought = true
        wheatField?.updateHarvestEstimate(22)
        assertTrue(wheatField?.plant!!.isFallow(23))
        assertTrue(wheatField?.plant!!.isFallow(24))
        assertTrue(wheatField?.plant!!.isFallow(1))
        assertFalse(wheatField?.plant!!.isFallow(3))
    }
}
