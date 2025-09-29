package de.unisaarland.cs.se.selab.plants

import de.unisaarland.cs.se.selab.board.Field
import de.unisaarland.cs.se.selab.incidents.AnimalAttack
import de.unisaarland.cs.se.selab.incidents.BeeHappy
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock

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
    val cuttedField: java.lang.reflect.Field = Plant::class.java.getDeclaredField("cutted")

    @BeforeEach
    fun setup() {
        /*
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
        cuttedField.isAccessible = true
        sowTimeField.set(oatPlant, 6)
        sowTimeField.set(potatoPlant, 7)
        harvestEstimateField.set(potatoPlant, 10)
         */
    }
    /*
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
        assertTrue(potatoPlant.pollinateable(10), "Potato should be pollinateable during YearTick 10")
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
        cuttedField.set(grapePlant, true)
        assertFalse(grapePlant.cuttable(14))
    }

    @Test
    fun cuttableNoHarvestEstimate() {
        harvestEstimateField.set(almondPlant, 0)
        assertFalse(almondPlant.cuttable(21))
    }

    @Test
    fun cuttableHarvestedPlant() {
        harvestTimeField.set(grapePlant, 17)
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
        assertTrue(cuttedField.get(grapePlant) == true)
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
     */
}
