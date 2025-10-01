package de.unisaarland.cs.se.selab.plants

import de.unisaarland.cs.se.selab.Constants
import de.unisaarland.cs.se.selab.board.Coordinate
import de.unisaarland.cs.se.selab.board.Field
import de.unisaarland.cs.se.selab.board.TileType
import de.unisaarland.cs.se.selab.farms.Action
import de.unisaarland.cs.se.selab.incidents.AnimalAttack
import de.unisaarland.cs.se.selab.incidents.BeeHappy
import de.unisaarland.cs.se.selab.incidents.Incident
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import kotlin.test.assertNull

class PlantTest {
    lateinit var grapePlant: Plant
    lateinit var almondPlant: Plant
    lateinit var potatoPlant: Plant
    lateinit var oatPlant: Plant
    val beeHappy4 = BeeHappy(0, 1, setOf(mock<Field>()), 1.2, 4)
    val beeHappy10 = BeeHappy(1, 1, setOf(mock<Field>()), 1.2, 10)
    val animalAttack = AnimalAttack(2, 1, setOf(mock<Field>()))
    val sowTimeField: java.lang.reflect.Field = Plant::class.java.getDeclaredField("sowTime")
    val harvestTimeField: java.lang.reflect.Field = Plant::class.java.getDeclaredField("harvestTime")
    val harvestEstimateField: java.lang.reflect.Field = Plant::class.java.getDeclaredField("harvestEstimate")
    val incidentField: java.lang.reflect.Field = Plant::class.java.getDeclaredField("incidents")
    val mowedForField: java.lang.reflect.Field = Plant::class.java.getDeclaredField("mowedFor")
    val actionPerformedField: java.lang.reflect.Field = Plant::class.java.getDeclaredField("actionPerformed")
    val cutPerformedField: java.lang.reflect.Field = Plant::class.java.getDeclaredField("cutPerformed")

    @BeforeEach
    fun setup() {
        grapePlant = Plant(PlantType.GRAPE, Constants.grape, 21)
        almondPlant = Plant(PlantType.ALMOND, Constants.almond, 21)
        potatoPlant = Plant(PlantType.POTATO, Constants.potato, 0)
        oatPlant = Plant(PlantType.OAT, Constants.oat, 0)
        sowTimeField.isAccessible = true
        harvestTimeField.isAccessible = true
        incidentField.isAccessible = true
        harvestEstimateField.isAccessible = true
        mowedForField.isAccessible = true
        actionPerformedField.isAccessible = true
        cutPerformedField.isAccessible = true
        sowTimeField.set(oatPlant, 6)
        sowTimeField.set(potatoPlant, 7)
        harvestEstimateField.set(potatoPlant, 10)
    }

    @Test
    fun getHarvestEstimate() {
        assertTrue(10 == potatoPlant.getHarvestEstimate())
    }

    @Test
    fun getMinMoisture() {
        assertTrue(400 == almondPlant.getMinMoisture())
    }

    @Test
    fun pollinateableInitPlant() {
        sowTimeField.set(potatoPlant, 0)
        assertFalse(potatoPlant.pollinateable(10), "Potato shouldn't be pollinateable if sowTime == 0")
    }

    @Test
    fun pollinateableFieldEasy() {
        assertTrue(potatoPlant.pollinateable(11), "Potato should be pollinateable during YearTick 11")
        assertFalse(oatPlant.pollinateable(0), "Oat should never be pollinateable")
    }

    @Test
    fun pollinateablePlantationEasy() {
        assertTrue(almondPlant.pollinateable(4), "Almond should be pollinateable during YearTick 4")
        assertFalse(almondPlant.pollinateable(6), "Almond shouldn't be pollinateable during YearTick 6")
        assertFalse(grapePlant.pollinateable(14), "Grape should never be pollinateable")
    }

    @Test
    fun addPollination() {
        potatoPlant.addPollination(beeHappy4)
        potatoPlant.addPollination(beeHappy10)
        assertTrue(mutableListOf(beeHappy4, beeHappy10) == incidentField.get(potatoPlant))
    }

    @Test
    fun addStampedeField() {
        assertTrue(potatoPlant.addStampede(animalAttack))
        assertFalse(oatPlant.addStampede(animalAttack))
        assertTrue(mutableListOf<Incident>() == incidentField.get(oatPlant))
    }

    @Test
    fun addStampedePlantation() {
        assertTrue(almondPlant.addStampede(animalAttack))
        assertTrue(grapePlant.addStampede(animalAttack))
        assertTrue(mutableListOf(animalAttack) == incidentField.get(grapePlant))
        assertTrue(2 == mowedForField.get(almondPlant))
        assertTrue(0 == mowedForField.get(grapePlant))
    }

    @Test
    fun isFallowPlantation() {
        assertFalse(grapePlant.isFallow(22))
    }

    @Test
    fun isFallowNotHarvested() {
        assertFalse(potatoPlant.isFallow(1))
    }

    @Test
    fun isFallowEasy() {
        harvestTimeField.set(potatoPlant, 20)
        harvestTimeField.set(almondPlant, 19)
        harvestTimeField.set(oatPlant, 18)
        assertFalse(potatoPlant.isFallow(1))
        assertFalse(almondPlant.isFallow(20))
        assertTrue(oatPlant.isFallow(22))
    }

    @Test
    fun harvestablePreviouslyHarvested() {
        harvestTimeField.set(potatoPlant, 21)
        assertFalse(potatoPlant.harvestable(22))
    }

    @Test
    fun harvestableCurrentTickHarvested() {
        actionPerformedField.set(potatoPlant, Action.HARVESTING)
        assertFalse(potatoPlant.harvestable(20))
    }

    @Test
    fun harvestableNoHarvestEstimate() {
        assertFalse(oatPlant.harvestable(18))
    }

    @Test
    fun harvestableEasy() {
        assertTrue(potatoPlant.harvestable(17))
        assertTrue(potatoPlant.harvestable(20))
        assertFalse(potatoPlant.harvestable(16))
        assertFalse(potatoPlant.harvestable(21))
    }

    @Test
    fun harvestableInLateTimeframe() {
        harvestEstimateField.set(oatPlant, 69)
        assertTrue(oatPlant.harvestable(18))
        assertFalse(oatPlant.harvestable(11))
    }

    @Test
    fun cuttableAlreadyCutted() {
        cutPerformedField.set(grapePlant, true)
        assertFalse(grapePlant.cuttable(14))
    }

    @Test
    fun cuttableNoHarvestEstimate() {
        harvestEstimateField.set(almondPlant, 0)
        assertFalse(almondPlant.cuttable(21))
    }

    @Test
    fun cuttableHarvestedPlant() {
        // @note harvestTime is not checked in cuttable
        grapePlant.performAction(Action.HARVESTING, 17)
        assertFalse(grapePlant.cuttable(14))
    }

    @Test
    fun cuttableEasy() {
        assertTrue(grapePlant.cuttable(14))
        assertFalse(grapePlant.cuttable(17))
    }

    @Test
    fun mowableField() {
        assertFalse(potatoPlant.mowable(12))
    }

    @Test
    fun mowableCurrentTickMowedOrMowedFor() {
        actionPerformedField.set(grapePlant, Action.MOWING)
        assertFalse(grapePlant.mowable(7))
        mowedForField.set(almondPlant, 1)
        assertFalse(almondPlant.mowable(11))
    }

    @Test
    fun mowableNoHarvestEstimate() {
        harvestEstimateField.set(grapePlant, 0)
        assertFalse(grapePlant.mowable(7))
    }

    @Test
    fun mowableAfterHarvest() {
        harvestTimeField.set(grapePlant, 14)
        assertFalse(grapePlant.harvestable(7))
    }

    @Test
    fun mowableEasy() {
        assertTrue(grapePlant.mowable(7))
        assertFalse(almondPlant.mowable(12))
    }

    @Test
    fun weedablePlantation() {
        assertFalse(grapePlant.weedable(12))
    }

    @Test
    fun weedableCurrentTickWeeded() {
        actionPerformedField.set(potatoPlant, Action.WEEDING)
        assertFalse(potatoPlant.weedable(9))
    }

    @Test
    fun weedableHarvestedPlant() {
        sowTimeField.set(potatoPlant, 0)
        assertFalse(potatoPlant.weedable(9))
    }

    @Test
    fun weedableEasy() {
        assertTrue(potatoPlant.weedable(9))
        assertFalse(potatoPlant.weedable(10))
    }

    @Test
    fun performActionCut() {
        assertNull(grapePlant.performAction(Action.CUTTING, 14))
        assertTrue(cutPerformedField.get(grapePlant) == true)
    }

    @Test
    fun performActionWeedingMowing() {
        assertNull(grapePlant.performAction(Action.MOWING, 12))
        assertNull(potatoPlant.performAction(Action.WEEDING, 21))
    }

    @Test
    fun performActionHarvest() {
        harvestEstimateField.set(grapePlant, 419)
        assertTrue(419 == grapePlant.performAction(Action.HARVESTING, 17))
        harvestEstimateField.set(potatoPlant, 421)
        assertTrue(421 == potatoPlant.performAction(Action.HARVESTING, 18))
    }

    @Test
    fun initApplePlantationNoPenalty() {
        val applePlant = Plant(PlantType.APPLE, Constants.apple, 19)
        assertTrue(applePlant.getHarvestEstimate() == Constants.APPLE_HE)
    }

    @Test
    fun initAlmondPlantationNoPenalty() {
        val almondPlant = Plant(PlantType.ALMOND, Constants.almond, 19)
        assertTrue(almondPlant.getHarvestEstimate() == Constants.ALMOND_HE)
    }

    @Test
    fun initCherryPlantationNoPenalty() {
        val cherryPlant = Plant(PlantType.CHERRY, Constants.cherry, 14)
        assertTrue(cherryPlant.getHarvestEstimate() == Constants.CHERRY_HE)
    }

    @Test
    fun initGrapePlantationNoPenalty() {
        val grapePlant = Plant(PlantType.GRAPE, Constants.grape, 17)
        assertTrue(grapePlant.getHarvestEstimate() == Constants.GRAPE_HE)
    }

    @Test
    fun initApplePlantationPenalty() {
        val applePlant = Plant(PlantType.APPLE, Constants.apple, 20)
        assertTrue(applePlant.getHarvestEstimate() == (Constants.APPLE_HE * 0.5).toInt())
    }

    @Test
    fun initAlmondPlantationPenalty() {
        val almondPlant = Plant(PlantType.ALMOND, Constants.almond, 20)
        assertTrue(almondPlant.getHarvestEstimate() == (Constants.ALMOND_HE * 0.9).toInt())
    }

    @Test
    fun initCherryPlantationPenalty() {
        val cherryPlant = Plant(PlantType.CHERRY, Constants.cherry, 15)
        assertTrue(cherryPlant.getHarvestEstimate() == (Constants.CHERRY_HE * 0.3).toInt())
    }

    @Test
    fun initGrapePlantationMultiplePenalties() {
        val grapePlant1 = Plant(PlantType.GRAPE, Constants.grape, 18)
        assertTrue(grapePlant1.getHarvestEstimate() == (Constants.GRAPE_HE * 0.95).toInt())
        val grapePlant2 = Plant(PlantType.GRAPE, Constants.grape, 19)
        assertTrue(grapePlant2.getHarvestEstimate() == (Constants.GRAPE_HE * 0.95 * 0.95).toInt())
        val grapePlant3 = Plant(PlantType.GRAPE, Constants.grape, 20)
        assertTrue(grapePlant3.getHarvestEstimate() == (Constants.GRAPE_HE * 0.95 * 0.95 * 0.95).toInt())
    }

    @Test
    fun initCherryPlantationAfterPossibleHarvest() {
        val cherryPlant1 = Plant(PlantType.CHERRY, Constants.cherry, 16)
        val cherryPlant2 = Plant(PlantType.CHERRY, Constants.cherry, 17)
        val cherryPlant3 = Plant(PlantType.CHERRY, Constants.cherry, 18)
        val cherryPlant4 = Plant(PlantType.CHERRY, Constants.cherry, 19)
        val cherryPlant5 = Plant(PlantType.CHERRY, Constants.cherry, 20)
        assertTrue(cherryPlant1.getHarvestEstimate() == 0)
        assertTrue(cherryPlant2.getHarvestEstimate() == 0)
        assertTrue(cherryPlant3.getHarvestEstimate() == 0)
        assertTrue(cherryPlant4.getHarvestEstimate() == 0)
        assertTrue(cherryPlant5.getHarvestEstimate() == 0)
    }

    @Test
    fun checkWeedableWheat() {
        val field = Field(
            0,
            Coordinate(0, 0),
            null,
            0,
            TileType.FIELD,
            100000,
            Plant(PlantType.WHEAT, Constants.wheat, 1),
            setOf(PlantType.WHEAT)
        )
        field.plant.sow(PlantType.WHEAT, Constants.wheat, 19)
        assertFalse(field.plant.weedable(20))
        assertFalse(field.plant.weedable(21))
        assertTrue(field.plant.weedable(22))
        assertFalse(field.plant.weedable(23))
        assertFalse(field.plant.weedable(24))
        assertFalse(field.plant.weedable(1))
        assertFalse(field.plant.weedable(2))
        assertFalse(field.plant.weedable(3))
        assertTrue(field.plant.weedable(4))
        field.plant.sow(PlantType.WHEAT, Constants.wheat, 20)
        assertFalse(field.plant.weedable(21))
        assertFalse(field.plant.weedable(22))
        assertTrue(field.plant.weedable(23))
        assertFalse(field.plant.weedable(24))
        assertFalse(field.plant.weedable(1))
        assertFalse(field.plant.weedable(2))
        assertFalse(field.plant.weedable(3))
        assertFalse(field.plant.weedable(4))
        assertTrue(field.plant.weedable(5))
    }

    @Test
    fun checkBeeHappyOnPotato() {
        val bee = BeeHappy(0, 4, emptySet(), 1.0, 11)
        harvestEstimateField.set(potatoPlant, 100)
        if (potatoPlant.pollinateable(11)) potatoPlant.addPollination(bee)
        potatoPlant.updateHarvestEstimate(11, 5, 1000, 1)
        assertTrue(potatoPlant.getHarvestEstimate() == 90)
    }
}
