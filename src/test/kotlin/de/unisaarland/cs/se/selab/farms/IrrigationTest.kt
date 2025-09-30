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

class IrrigationTest {
    lateinit var field: Field
    lateinit var plantation: Plantation
    lateinit var boardData: BoardData
    val almondPlant = Plant(PlantType.ALMOND, Constants.almond, 2)
    val potatoPlant = Plant(PlantType.POTATO, Constants.potato, 2)
    lateinit var farmHandler: FarmHandler

    @BeforeEach
    fun setUp() {
        field = Field(0, Coordinate(0, 0), null, 1, TileType.FIELD, 770, potatoPlant, setOf(PlantType.POTATO))
        plantation = Plantation(1, Coordinate(2, 0), null, 1, TileType.PLANTATION, 700, almondPlant)
        val farmstead = Tile(2, Coordinate(1, 1), null, true, 1, TileType.FARMSTEAD)
        boardData = BoardData(mapOf(0 to field, 1 to plantation, 2 to farmstead))
        val machine =
            Machine(
                1,
                listOf(Action.IRRIGATING, Action.SOWING),
                listOf(PlantType.ALMOND, PlantType.POTATO),
                7,
                farmstead
            )
        val sowingPlan = SowingPlan(1, Constants.MAR_2, PlantType.POTATO, listOf(0))
        val farm = Farm(1, listOf(2), listOf(0), listOf(1), listOf(1), mutableListOf(sowingPlan))
        farmHandler = FarmHandler(
            mapOf(1 to farm),
            mapOf(PlantType.POTATO to Constants.potato, PlantType.ALMOND to Constants.almond),
            mapOf(1 to machine),
            PathFinder()
        )
    }

    @Test
    fun test() {
        val boardHandler = BoardHandler()
        farmHandler.farmAction(6, 6, boardData)
        boardHandler.reduceSoil(7, boardData) // 700 and 600
        farmHandler.farmAction(7, 7, boardData)
        boardHandler.reduceSoil(8, boardData) // 600 and 500
        farmHandler.farmAction(8, 8, boardData)
        boardHandler.reduceSoil(9, boardData) // 500 and 400
        farmHandler.farmAction(9, 9, boardData) // no irrigation
        boardHandler.reduceSoil(10, boardData) // 400 and 300 -> both logged
        farmHandler.farmAction(10, 10, boardData) // irrigate both
        boardHandler.reduceSoil(11, boardData)
    }
}
