package general

import de.unisaarland.cs.se.selab.Constants
import de.unisaarland.cs.se.selab.board.Coordinate
import de.unisaarland.cs.se.selab.board.Direction
import de.unisaarland.cs.se.selab.board.Field
import de.unisaarland.cs.se.selab.board.Plantation
import de.unisaarland.cs.se.selab.board.TileType
import de.unisaarland.cs.se.selab.farms.Action
import de.unisaarland.cs.se.selab.incidents.AnimalAttack
import de.unisaarland.cs.se.selab.incidents.BeeHappy
import de.unisaarland.cs.se.selab.plants.Plant
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
    fun testing() {
        wheatField?.plant?.sow(PlantType.WHEAT, wheatField?.plant!!.data, 20)
        wheatField?.updateHarvestEstimate(20)
        assertEquals(1_500_000, wheatField?.plant?.getHarvestEstimate())
        wheatField?.loseMoisture()
        wheatField?.sunhours = 140
        wheatField?.stampede(AnimalAttack(1, 14, emptySet()))
        wheatField?.updateHarvestEstimate(14)
        assertEquals(485980, wheatField?.plant?.getHarvestEstimate())
        wheatField?.sunhours = 80
        wheatField?.performAction(Action.IRRIGATING, 15)
        wheatField?.updateHarvestEstimate(15)
        assertEquals(388_784, wheatField?.plant?.getHarvestEstimate())
        wheatField?.performAction(Action.HARVESTING, 13)
        wheatField?.plant?.sow(PlantType.WHEAT, wheatField?.plant!!.data, 21)
        wheatField?.sunhours = 115
        repeat(3) {
            wheatField?.loseMoisture()
        }
        wheatField?.stampede(AnimalAttack(2, 21, emptySet()))
        wheatField?.updateHarvestEstimate(21)
        assertEquals(539_925, wheatField?.plant?.getHarvestEstimate())
        wheatField?.drought = true
        wheatField?.updateHarvestEstimate(22)
        assertEquals(0, wheatField?.plant?.getHarvestEstimate())

        pumpkinField?.plant?.sow(PlantType.PUMPKIN, pumpkinField?.plant!!.data, 11)
        pumpkinField?.updateHarvestEstimate(11)
        pumpkinField?.stampede(AnimalAttack(3, 13, emptySet()))
        pumpkinField?.plant?.addPollination(BeeHappy(5, 13, emptySet(), 2.0, 13))
        pumpkinField?.updateHarvestEstimate(13)
        // 500'000 * 0.9 = 450'000
        // 450'000 * 0.5 = 225'000
        // 225'000 + 225'000 * 2.0 = 675'000
        // assertEquals(227_250, pumpkinField?.plant?.getHarvestEstimate())
        assertEquals(675_000, pumpkinField?.plant?.getHarvestEstimate())
        repeat(10) {
            pumpkinField?.loseMoisture()
        }
        pumpkinField?.updateHarvestEstimate(11)
        assertEquals(0, pumpkinField?.plant?.getHarvestEstimate())

        almondPlantation?.plant?.prepareCurrentTick(21)
        almondPlantation?.plant?.addPollination(BeeHappy(6, 5, emptySet(), 2.0, 5))
        almondPlantation?.updateHarvestEstimate(5)
        // Cutting is skipped!
        // 800'000 + 800'000 * 2.0 = 2'400'000
        // assertEquals(408_000, almondPlantation?.plant?.getHarvestEstimate())
        assertEquals(2_400_000, almondPlantation?.plant?.getHarvestEstimate())

        grapePlantation?.prepareCurrentTick(180, 20)
        grapePlantation?.updateHarvestEstimate(18)
        // Cutting is skipped!
        // We never enter the cuttable ticks!
        // assertEquals(461_700, grapePlantation?.plant?.getHarvestEstimate())
        assertEquals(1_026_000, grapePlantation?.plant?.getHarvestEstimate())
        grapePlantation?.updateHarvestEstimate(20)
        assertEquals(877_230, grapePlantation?.plant?.getHarvestEstimate())
    }
}
