package de.unisaarland.cs.se.selab.board

import de.unisaarland.cs.se.selab.Constants
import de.unisaarland.cs.se.selab.farms.Action
import de.unisaarland.cs.se.selab.farms.Farm
import de.unisaarland.cs.se.selab.farms.FarmHandler
import de.unisaarland.cs.se.selab.farms.Machine
import de.unisaarland.cs.se.selab.farms.PathFinder
import de.unisaarland.cs.se.selab.plants.Plant
import de.unisaarland.cs.se.selab.plants.PlantType
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class LowMoistureAndLateHarvestTest {
    lateinit var tile: Plantation
    lateinit var farmstead: Tile
    val grape = Plant(PlantType.GRAPE, Constants.grape, 19)
    lateinit var farmHandler: FarmHandler

    @BeforeEach
    fun setUp() {
        tile = Plantation(0, Coordinate(0, 0), null, 1, TileType.PLANTATION, 250, grape)
        farmstead = Tile(1, Coordinate(1, 1), null, true, 1, TileType.FARMSTEAD)
        val machine = Machine(1, listOf(Action.SOWING), listOf(PlantType.GRAPE), 12, farmstead)
        val farm = Farm(1, listOf(1), emptyList(), listOf(0), listOf(1), mutableListOf())
        farmHandler = FarmHandler(
            mapOf(1 to farm), mapOf(PlantType.GRAPE to Constants.grape), mapOf(1 to machine),
            PathFinder()
        )
    }

    @Test
    fun bababoi() {
        val bh = BoardHandler()
        val board = BoardData(mapOf(0 to tile, 1 to farmstead))
        bh.reduceSoil(19, board)
        farmHandler.farmAction(1, 19, board)
        bh.computeEstimate(19, board)
    }
}
