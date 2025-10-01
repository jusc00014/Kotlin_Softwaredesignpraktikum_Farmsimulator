package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.board.Coordinate
import de.unisaarland.cs.se.selab.board.Tile
import de.unisaarland.cs.se.selab.board.TileType
import de.unisaarland.cs.se.selab.farms.Machine
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BrokenMachineTest {

    @Test
    fun test1() {
        val tile = Tile(0, Coordinate(0, 0), null, false, 0, TileType.ROAD)
        val machine = Machine(0, emptyList(), emptyList(), 10, tile, 0, false)
        val incident = BrokenMachine(0, 0, 3, machine)
        incident.execute()
        assertEquals(3, machine.brokenFor)
    }
}
