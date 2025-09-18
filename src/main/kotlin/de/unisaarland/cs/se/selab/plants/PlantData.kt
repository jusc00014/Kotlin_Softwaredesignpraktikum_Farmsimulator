package de.unisaarland.cs.se.selab.plants

/**
 * Static data for each Plant
 * @see "Project_Specification_(Post-Change).pdf Page 19-21"
 */
data class PlantData(
    val moistureMin: Int,
    val sunlightMax: Int,
    val initialHarvestEstimate: Int,
    val bloomRange: IntRange,
    val pollinationByInsects: Boolean,
    val harvestingRange: IntRange,
    val lateHarvestTimeFrame: Int,
    val sowRange: IntRange,
    val weedingTimes: List<Int>,
    val cuttingTimes: List<Int>,
    val mowingTimes: List<Int>,
    val tileType: PlantTile,
)
