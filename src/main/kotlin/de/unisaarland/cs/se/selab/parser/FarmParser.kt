package de.unisaarland.cs.se.selab.parser

import org.json.JSONArray
import org.json.JSONObject
import de.unisaarland.cs.se.selab.board.BoardData
import de.unisaarland.cs.se.selab.farms.Farm
import de.unisaarland.cs.se.selab.farms.Action
import de.unisaarland.cs.se.selab.farms.Machine
import de.unisaarland.cs.se.selab.plants.PlantType

class FarmParser {
    private val machine_names = mutableListOf<String>()
    private val finished_machines = mutableMapOf<Int, String>()

    fun parse(jsonFile: String, board: BoardData): (List<Farm>, Map<Int, Machine>) {

    }


    private fun vallidate_farm_id_and_name(id: Int, name: String): (Int, String) {

    }

    private fun validate_farmstead(id_list: MutableList<Int>, farm_id: Int): MutableList<Int> {

    }

    private fun validate_fields(id_list: MutableList<Int>, farm_id: Int): MutableList<Int> {

    }

    private fun validate_plantations(id_list: MutableList<Int>, farm_id: Int): MutableList<Int> {

    }

    private fun validate_at_least_one_field_or_plantation_tile(fields: MutableList<Int>, plantations: MutableList<Int>) {

    }

    private fun validate_sowing_plan_id_and_tick(id: Int, tick: Int): Pair<Int, Int> {

    }

    private fun validate_sowing_plan_plant_types(plant: String): PlantType {

    }

    private fun validate_sowing_plan_fields_by_radius(tile_id: Int, radius: Int): MutableList<Int> {

    }

    private fun validate_machines(farm_id: Int, machines: JSONArray, board: BoardData): List<Machine> {

    }

    private fun validate_machine_id_and_name(id: Int, name: String): Pair<Int, String> {

    }

    private fun validate_machine_actions(actions: Array<String>): Array<Action> {

    }

    private fun validate_machine_plants(plants: Array<String>): Array<PlantType> {

    }

    private fun validate_machine_duration(duration: Int): Int {

    }

    private fun validate_machine_tile(tile_id: Int): Int {

    }

    private fun add_machine_to_lists(current_farm_machines: MutableList<Machine>) {

    }

    private fun add_to_list(current_farm: Farm, farms: MutableList<Farm>) {

    }
}