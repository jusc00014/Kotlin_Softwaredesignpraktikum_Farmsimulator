package de.unisaarland.cs.se.selab.farms

import de.unisaarland.cs.se.selab.board.BoardHandler
import de.unisaarland.cs.se.selab.parser.FarmParser
import de.unisaarland.cs.se.selab.parser.MapParser
import kotlin.test.Test
import kotlin.test.assertTrue

class NonPrioTest {

    private val mapJson: String = "src/systemtest/resources/farmActionTests/map.json"
    private val farmJson: String = "src/systemtest/resources/farmActionTests/farm.json"

    @Test
    fun nonPriortizedActionsTest() {
        val mapParser = MapParser(mutableMapOf())
        val (board, plantMap) = mapParser.parse(mapJson, 9)

        val farmParser = FarmParser()
        val (farms, machines) = farmParser.parse(farmJson, board)
        val boardHandler = BoardHandler()
        val farmes = mapOf(1 to farms[0])
        val farmHandler = FarmHandler(farmes, plantMap, machines, PathFinder())
        for (i in listOf(0, 1, 2)) {
            boardHandler.reduceSoil(9, board)
            farmHandler.farmAction(i, 9 + i, board)
            val fertiles = board.getFertiles()
            when (i) {
                0 -> assertTrue {
                    fertiles[1]?.plant?.isSown() == true &&
                        fertiles[3]?.plant?.isSown() == true &&
                        fertiles[5]?.plant?.isSown() == true &&
                        fertiles[6]?.plant?.isSown() == true
                }
                2 -> assertTrue {
                    fertiles[6]?.irrigatable() == false &&
                        fertiles[4]?.irrigatable() == false &&
                        fertiles[3]?.irrigatable() == true
                }
            }
        }
    }
}
