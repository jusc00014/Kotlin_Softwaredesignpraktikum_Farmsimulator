package general

import de.unisaarland.cs.se.selab.board.Coordinate
import de.unisaarland.cs.se.selab.board.Direction
import de.unisaarland.cs.se.selab.board.Field
import de.unisaarland.cs.se.selab.board.Plantation
import de.unisaarland.cs.se.selab.board.TileType
import de.unisaarland.cs.se.selab.farms.Action
import de.unisaarland.cs.se.selab.incidents.AnimalAttack
import de.unisaarland.cs.se.selab.incidents.BeeHappy
import de.unisaarland.cs.se.selab.plants.Plant
import de.unisaarland.cs.se.selab.plants.PlantData
import de.unisaarland.cs.se.selab.plants.PlantTile
import de.unisaarland.cs.se.selab.plants.PlantType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PlantHarvestEstimateBigTest {
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
            PlantData(
                450, 90, 1500000, IntRange(0, 0), false, IntRange(11, 13), 2,
                IntRange(19, 20), listOf(3, 9), listOf(), listOf(), PlantTile.FIELD
            ),
            1
        )
        val pumpkinPlant = Plant(
            PlantType.PUMPKIN,
            PlantData(
                600, 120, 500000, IntRange(2, 3), true, IntRange(17, 20), 0,
                IntRange(9, 12), (2..24 step 2).toList(), listOf(), listOf(), PlantTile.FIELD
            ),
            1
        )
        val almondPlant = Plant(
            PlantType.ALMOND,
            PlantData(
                400, 130, 800000, IntRange(4, 5), true, IntRange(16, 19), 1,
                IntRange(0, 0), listOf(), listOf(21, 22, 3, 4), listOf(11, 17), PlantTile.FIELD
            ),
            1
        )
        val grapePlant = Plant(
            PlantType.GRAPE,
            PlantData(
                250, 150, 1200000, IntRange(12, 16), false, IntRange(18, 18), 3,
                IntRange(0, 0), listOf(), listOf(14, 15, 16), listOf(7, 13), PlantTile.PLANTATION
            ),
            1
        )
        return listOf(wheatPlant, pumpkinPlant, almondPlant, grapePlant)
    }

    @Test
    fun testing() {
        wheatField?.plant?.sow(PlantType.WHEAT, wheatField?.plant!!.data, 20)
        wheatField?.updateHarvestEstimate(20)
        assertEquals(1_500_000, wheatField?.plant?.getHarvestEstimate())
        wheatField?.loseMoisture()
        wheatField?.sunhours = 140
        wheatField?.stampede(AnimalAttack(1, 14, setOf()))
        wheatField?.updateHarvestEstimate(14)
        assertEquals(485980, wheatField?.plant?.getHarvestEstimate())
        wheatField?.sunhours = 80
        wheatField?.performAction(Action.IRRIGATING, 15)
        assertEquals(388_784, wheatField?.plant?.getHarvestEstimate())
        wheatField?.performAction(Action.HARVESTING, 13)
        wheatField?.plant?.sow(PlantType.WHEAT, wheatField?.plant!!.data, 21)
        wheatField?.sunhours = 115
        repeat(3) {
            wheatField?.loseMoisture()
        }
        wheatField?.stampede(AnimalAttack(2, 21, setOf()))
        wheatField?.updateHarvestEstimate(21)
        assertEquals(539_950, wheatField?.plant?.getHarvestEstimate())
        wheatField?.drought = true
        wheatField?.updateHarvestEstimate(22)
        assertEquals(0, wheatField?.plant?.getHarvestEstimate())

        pumpkinField?.plant?.sow(PlantType.PUMPKIN, pumpkinField?.plant!!.data, 11)
        pumpkinField?.updateHarvestEstimate(11)
        pumpkinField?.stampede(AnimalAttack(3, 13, setOf()))
        pumpkinField?.plant?.addPollination(BeeHappy(5, 13, setOf(), 2.0, 13))
        pumpkinField?.updateHarvestEstimate(13)
        assertEquals(227_250, pumpkinField?.plant?.getHarvestEstimate())
        repeat(10) {
            pumpkinField?.loseMoisture()
        }
        pumpkinField?.updateHarvestEstimate(11)
        assertEquals(0, pumpkinField?.plant?.getHarvestEstimate())

        almondPlantation?.updateHarvestEstimate(21)
        pumpkinField?.plant?.addPollination(BeeHappy(6, 5, setOf(), 2.0, 5))
        pumpkinField?.updateHarvestEstimate(5)
        assertEquals(408_000, pumpkinField?.plant?.getHarvestEstimate())

        grapePlantation?.updateHarvestEstimate(21)
        grapePlantation?.sunhours = 180
        grapePlantation?.updateHarvestEstimate(18)
        assertEquals(461_700, grapePlantation?.plant?.getHarvestEstimate())
        grapePlantation?.updateHarvestEstimate(21)
        assertEquals(0, grapePlantation?.plant?.getHarvestEstimate())
    }
}
