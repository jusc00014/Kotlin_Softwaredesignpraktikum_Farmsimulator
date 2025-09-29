package de.unisaarland.cs.se.selab.farms

import de.unisaarland.cs.se.selab.Constants
import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.board.BoardHandler
import de.unisaarland.cs.se.selab.board.Coordinate
import de.unisaarland.cs.se.selab.board.Field
import de.unisaarland.cs.se.selab.board.Plantation
import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.board.TileType
import de.unisaarland.cs.se.selab.plants.Plant
import de.unisaarland.cs.se.selab.plants.PlantType
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class MoreFarmHandlerTests {
    lateinit var board: BoardData
    val potatoPlant = Plant(PlantType.POTATO, Constants.potato, 1)
    val potatoPlant2 = Plant(PlantType.POTATO, Constants.potato, 2)
    lateinit var farmHandler: FarmHandler
    lateinit var field1: Field
    lateinit var field2: Field
    lateinit var plantation: Plantation
    lateinit var plantation2: Plantation
    val almondPlant = Plant(PlantType.ALMOND, Constants.almond, 1)
    val almondPlant2 = Plant(PlantType.ALMOND, Constants.almond, 2)

    @BeforeEach
    fun setUp() {
        field1 = Field(0, Coordinate(0, 0), null, 1, TileType.FIELD, 600, potatoPlant, setOf(PlantType.POTATO))
        field2 = Field(1, Coordinate(2, 0), null, 1, TileType.FIELD, 600, potatoPlant2, setOf(PlantType.POTATO))
        plantation = Plantation(3, Coordinate(4, 0), null, 1, TileType.PLANTATION, 600, almondPlant)
        plantation2 = Plantation(4, Coordinate(6, 0), null, 1, TileType.PLANTATION, 600, almondPlant2)
        val farmstead = Tile(2, Coordinate(1, 1), null, true, 1, TileType.FARMSTEAD)
        board = BoardData(mapOf(0 to field1, 1 to field2, 2 to farmstead, 3 to plantation, 4 to plantation2))
        val farm = Farm(
            1,
            listOf(2),
            listOf(0, 1),
            listOf(3, 4),
            listOf(1, 2),
            mutableListOf(
                SowingPlan(0, 1, PlantType.POTATO, listOf(0, 1)),
                SowingPlan(1, 0, PlantType.POTATO, listOf(0, 1))
            )
        )
        val machine1 = Machine(
            1,
            listOf(Action.SOWING, Action.CUTTING, Action.HARVESTING),
            listOf(PlantType.POTATO, PlantType.ALMOND),
            10,
            farmstead
        )
        val machine2 = Machine(
            2,
            listOf(Action.HARVESTING, Action.CUTTING),
            listOf(PlantType.POTATO, PlantType.ALMOND),
            3,
            farmstead
        )
        val pds = mapOf(PlantType.POTATO to Constants.potato)
        farmHandler = FarmHandler(mapOf(1 to farm), pds, mapOf(1 to machine1, 2 to machine2), PathFinder())
    }

    @Test
    fun noContinue() {
        BoardHandler().computeEstimate(21, board)
        farmHandler.farmAction(4, board)
        farmHandler.farmAction(Constants.APR_1, board)
        assert(field1.plant.getHarvestEstimate() != 0)
        assert(field2.plant.getHarvestEstimate() == 0)
        farmHandler.farmAction(Constants.APR_2, board)
        assert(field1.plant.getHarvestEstimate() != 0)
        assert(field2.plant.getHarvestEstimate() != 0)
        BoardHandler().computeEstimate(Constants.SEP_1, board)
        farmHandler.farmAction(Constants.SEP_2, board)
        farmHandler.farmAction(Constants.OCT_1, board)
        assert(field1.plant.getHarvestEstimate() == 0)
        assert(field2.plant.getHarvestEstimate() == 0)
    }
}
