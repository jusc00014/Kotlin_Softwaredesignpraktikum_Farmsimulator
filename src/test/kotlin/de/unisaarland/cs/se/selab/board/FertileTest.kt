package de.unisaarland.cs.se.selab.board

import de.unisaarland.cs.se.selab.Constants
import de.unisaarland.cs.se.selab.farms.Action
import de.unisaarland.cs.se.selab.plants.Plant
import de.unisaarland.cs.se.selab.plants.PlantType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class FertileTest {
    @Test
    fun prepareCurrentTick() {
        val yearTick = Constants.NOV_2
        val fertile: Fertile = Plantation(
            id = 420,
            coord = Coordinate(420, 420),
            airflow = null,
            farmID = 69,
            type = TileType.PLANTATION,
            moistureCapacity = 1_000,
            plant = Plant(
                PlantType.APPLE,
                Constants.apple,
                yearTick
            )
        )
        fertile.prepareCurrentTick(100, yearTick)
        assertEquals(100, fertile.sunhours)
    }

    @Test
    fun loseMoisture() {
        val yearTick = Constants.OCT_1
        val fertileAboveMin: Fertile = Plantation(
            id = 420,
            coord = Coordinate(420, 420),
            airflow = null,
            farmID = 69,
            type = TileType.PLANTATION,
            moistureCapacity = 1_000,
            plant = Plant(
                PlantType.APPLE,
                Constants.apple,
                yearTick
            )
        )
        assertFalse(fertileAboveMin.loseMoisture())

        val fertileBelowMin: Fertile = Plantation(
            id = 420,
            coord = Coordinate(420, 420),
            airflow = null,
            farmID = 69,
            type = TileType.PLANTATION,
            moistureCapacity = 1,
            plant = Plant(
                PlantType.APPLE,
                Constants.apple,
                yearTick
            )
        )
        assertTrue(fertileBelowMin.loseMoisture())
    }

    @Test
    fun irrigatable() {
        val yearTick: Int = Constants.OCT_1
        val fertile: Fertile = Plantation(
            id = 420,
            coord = Coordinate(420, 420),
            airflow = null,
            farmID = 69,
            type = TileType.PLANTATION,
            moistureCapacity = 120,
            plant = Plant(
                PlantType.APPLE,
                Constants.apple,
                yearTick
            )
        )
        assertFalse(fertile.irrigatable())
        fertile.loseMoisture()
        assertTrue(fertile.irrigatable())
    }

    @Test
    fun rain() {
        val yearTick: Int = Constants.OCT_1
        val fertile: Fertile = Plantation(
            id = 420,
            coord = Coordinate(420, 420),
            airflow = null,
            farmID = 69,
            type = TileType.PLANTATION,
            moistureCapacity = 500,
            plant = Plant(
                PlantType.APPLE,
                Constants.apple,
                yearTick
            )
        )
        fertile.loseMoisture()
        assertEquals(900, fertile.rain(1_000))
        assertEquals(1_000, fertile.rain(1_000))
    }

    @Test
    fun updateHarvestEstimate() {
        val yearTick: Int = Constants.OCT_1
        val plantation: Fertile = Plantation(
            id = 420,
            coord = Coordinate(420, 420),
            airflow = null,
            farmID = 69,
            type = TileType.PLANTATION,
            moistureCapacity = 500,
            plant = Plant(
                PlantType.APPLE,
                Constants.apple,
                yearTick
            )
        )
        plantation.drought = true
        plantation.updateHarvestEstimate(yearTick)
        assertTrue(plantation.drought)
        val field: Fertile = Field(
            id = 420,
            coord = Coordinate(420, 420),
            airflow = null,
            farmID = 69,
            type = TileType.FIELD,
            moistureCapacity = 500,
            possiblePlants = setOf(PlantType.POTATO),
            plant = Plant(
                PlantType.POTATO,
                Constants.potato,
                yearTick
            )
        )
        field.drought = true
        field.updateHarvestEstimate(yearTick)
        assertFalse(field.drought)
    }

    @Test
    fun performAction() {
        val yearTick: Int = Constants.OCT_1
        val fertile: Fertile = Plantation(
            id = 420,
            coord = Coordinate(420, 420),
            airflow = null,
            farmID = 69,
            type = TileType.PLANTATION,
            moistureCapacity = 120,
            plant = Plant(
                PlantType.APPLE,
                Constants.apple,
                yearTick
            )
        )
        fertile.loseMoisture()
        assertTrue(fertile.irrigatable())
        var r = fertile.performAction(Action.IRRIGATING, yearTick)
        assertNull(r)
        assertFalse(fertile.irrigatable())
        fertile.updateHarvestEstimate(yearTick)
        r = fertile.performAction(Action.HARVESTING, yearTick)
        assertNotNull(r)
        assertEquals(Constants.apple.initialHarvestEstimate, r)
    }
}
