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
import kotlin.test.Test

class MediumFarmHarvestTest {
    val pumpkinPlant = Plant(PlantType.PUMPKIN, Constants.pumpkin, Constants.OCT_1)
    val potatoPlant = Plant(PlantType.POTATO, Constants.potato, Constants.OCT_1)
    lateinit var board: BoardData
    lateinit var farmHandler: FarmHandler
    lateinit var firstFieldTile1Pumpkin: Field
    lateinit var secondFieldTile1Potato: Field

    @BeforeEach
    fun setUpTiles() {
        val mapping = mutableMapOf<Int, Tile>()
        firstFieldTile1Pumpkin = makeFirstField1Pumpkin()
        mapping[firstFieldTile1Pumpkin.id] = firstFieldTile1Pumpkin
        val farmStead1 = Tile(7, Coordinate(1, 1), null, true, 1, TileType.FARMSTEAD)
        mapping[farmStead1.id] = farmStead1
        secondFieldTile1Potato = makeSecondField1Potato()
        mapping[secondFieldTile1Potato.id] = secondFieldTile1Potato
        val thirdFieldPumpkin2 = makeThirdField1Pumpkin()
        mapping[thirdFieldPumpkin2.id] = thirdFieldPumpkin2
        val road = Tile(3, Coordinate(6, 0), null, false, null, TileType.ROAD)
        mapping[road.id] = road
        val firstField2Pumpkin = makeFirstField2Pumpkin()
        mapping[firstField2Pumpkin.id] = firstField2Pumpkin
        val village = Tile(5, Coordinate(10, 0), null, false, null, TileType.VILLAGE)
        mapping[village.id] = village
        val secondField2Pumpkin = Field(
            6,
            Coordinate(12, 0),
            null,
            2,
            TileType.FIELD,
            2000,
            pumpkinPlant,
            setOf(
                PlantType.PUMPKIN,
                PlantType.POTATO
            )
        )
        mapping[secondField2Pumpkin.id] = secondField2Pumpkin
        val farmStead3 = Tile(9, Coordinate(11, 1), null, true, 2, TileType.FARMSTEAD)
        mapping[farmStead3.id] = farmStead3
        val farmStead2 = Tile(8, Coordinate(9, 1), null, false, 2, TileType.FARMSTEAD)
        mapping[farmStead2.id] = farmStead2
        val forest1 = Tile(10, Coordinate(6, 2), null, false, null, TileType.FOREST)
        mapping[forest1.id] = forest1
        val forest2 = Tile(11, Coordinate(8, 2), null, false, null, TileType.FOREST)
        mapping[forest2.id] = forest2
        val forest3 = Tile(10, Coordinate(10, 2), null, false, null, TileType.FOREST)
        mapping[forest3.id] = forest3
        val thirdField2Pumpkin = Field(
            13,
            Coordinate(12, 2),
            null,
            2,
            TileType.FIELD,
            2000,
            pumpkinPlant,
            setOf(
                PlantType.PUMPKIN,
                PlantType.POTATO
            )
        )
        mapping[thirdField2Pumpkin.id] = thirdField2Pumpkin
        board = BoardData(mapping)
    }

    @BeforeEach
    fun setFarmsUp() {
        val farmMapping = mutableMapOf<Int, Farm>()
        val farm1 = Farm(
            1,
            listOf(7),
            listOf(0, 1, 2),
            emptyList(),
            listOf(1),
            mutableListOf(SowingPlan(0, 99, PlantType.PUMPKIN, listOf(0, 2)))
        )
        farmMapping[farm1.id] = farm1
        val farm2 = Farm(
            2,
            listOf(8, 9),
            listOf(4, 6, 13),
            emptyList(),
            listOf(2),
            mutableListOf()
        )
        farmMapping[farm2.id] = farm2
        val machine1 = Machine(
            1,
            listOf(Action.HARVESTING),
            listOf(PlantType.POTATO, PlantType.PUMPKIN),
            3,
            board.getTileById(7) ?: return,
        )
        val machine2 = Machine(
            2,
            listOf(Action.HARVESTING),
            listOf(PlantType.POTATO, PlantType.PUMPKIN),
            3,
            board.getTileById(9) ?: return,
        )
        val machineMapping = mutableMapOf<Int, Machine>(machine1.id to machine1, machine2.id to machine2)
        val pathFinder = PathFinder()
        farmHandler = FarmHandler(
            farmMapping,
            mapOf(
                PlantType.PUMPKIN to Constants.pumpkin,
                PlantType.POTATO to Constants.potato
            ),
            machineMapping, pathFinder
        )
    }

    fun makeFirstField1Pumpkin(): Field {
        return Field(
            0,
            Coordinate(0, 0),
            null,
            1,
            TileType.FIELD,
            2000,
            pumpkinPlant,
            setOf(
                PlantType.PUMPKIN,
                PlantType.POTATO
            )
        )
    }

    fun makeSecondField1Potato(): Field {
        return Field(
            1,
            Coordinate(2, 0),
            null,
            1,
            TileType.FIELD,
            2000,
            potatoPlant,
            setOf(
                PlantType.PUMPKIN,
                PlantType.POTATO
            )
        )
    }

    fun makeThirdField1Pumpkin(): Field {
        return Field(
            2,
            Coordinate(4, 0),
            null,
            1,
            TileType.FIELD,
            2000,
            pumpkinPlant,
            setOf(
                PlantType.PUMPKIN,
                PlantType.POTATO
            )
        )
    }

    fun makeFirstField2Pumpkin(): Field {
        return Field(
            4,
            Coordinate(8, 0),
            null,
            2,
            TileType.FIELD,
            2000,
            pumpkinPlant,
            setOf(
                PlantType.PUMPKIN,
                PlantType.POTATO
            )
        )
    }

    @Test
    fun harvestingTest() {
        return
    }
}
