package de.unisaarland.cs.se.selab.farms

import de.unisaarland.cs.se.selab.Constants
import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.Coordinate
import de.unisaarland.cs.se.selab.board.Field
import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.board.TileType
import de.unisaarland.cs.se.selab.plants.Plant
import de.unisaarland.cs.se.selab.plants.PlantType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class QuickTestForDuration {
    lateinit var boardData: BoardData
    val wheatPlant = Plant(PlantType.WHEAT, Constants.wheat, 1)
    val wheatPlant2 = Plant(PlantType.WHEAT, Constants.wheat, 2)
    val pds = mapOf(PlantType.WHEAT to Constants.wheat)
    lateinit var farmHandler: FarmHandler
    lateinit var tile1: Field
    lateinit var tile2: Field

    @BeforeEach
    fun setUp() {
        tile1 = Field(0, Coordinate(0, 0), null, 1, TileType.FIELD, 1000, wheatPlant, setOf(PlantType.WHEAT))
        tile2 = Field(1, Coordinate(2, 0), null, 1, TileType.FIELD, 1000, wheatPlant2, setOf(PlantType.WHEAT))
        val farmstead = Tile(2, Coordinate(1, 1), null, true, 1, TileType.FARMSTEAD)
        val machine = Machine(1, listOf(Action.SOWING), listOf(PlantType.WHEAT), 7, farmstead)
        val sowingPlan = SowingPlan(1, 0, PlantType.WHEAT, listOf(0, 1))
        val farm = Farm(1, listOf(2), listOf(0, 1), emptyList(), listOf(1), mutableListOf(sowingPlan))
        boardData = BoardData(mapOf(0 to tile1, 1 to tile2))
        farmHandler = FarmHandler(mapOf(1 to farm), pds, mapOf(1 to machine), PathFinder())
    }

    @Test
    fun test() {
        farmHandler.farmAction(0, Constants.OCT_1, boardData)
        assert(tile1.plant.getHarvestEstimate() != 0)
        assert(tile2.plant.getHarvestEstimate() != 0)
    }
}
