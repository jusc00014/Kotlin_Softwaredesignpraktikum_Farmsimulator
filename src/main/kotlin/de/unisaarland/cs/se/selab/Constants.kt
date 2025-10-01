package de.unisaarland.cs.se.selab

import de.unisaarland.cs.se.selab.plants.PlantData
import de.unisaarland.cs.se.selab.plants.PlantTile

/**
 * Object holding generic Constants
 */
object Constants {
    const val POTATO_MOISTURE = 500
    const val WHEAT_MOISTURE = 450
    const val OAT_MOISTURE = 300
    const val PUMPKIN_MOISTURE = 600
    const val APPLE_MOISTURE = 100
    const val ALMOND_MOISTURE = 400
    const val CHERRY_MOISTURE = 150
    const val GRAPE_MOISTURE = 250

    const val POTATO_SUNLIGHT = 130
    const val WHEAT_SUNLIGHT = 90
    const val OAT_SUNLIGHT = 90
    const val PUMPKIN_SUNLIGHT = 120
    const val APPLE_SUNLIGHT = 50
    const val ALMOND_SUNLIGHT = 130
    const val CHERRY_SUNLIGHT = 120
    const val GRAPE_SUNLIGHT = 150

    const val POTATO_HE = 1_000_000
    const val WHEAT_HE = 1_500_000
    const val OAT_HE = 1_200_000
    const val PUMPKIN_HE = 500_000
    const val APPLE_HE = 1_700_000
    const val ALMOND_HE = 800_000
    const val CHERRY_HE = 1_200_000
    const val GRAPE_HE = 1_200_000

    const val YEAR_TICK_1 = 1
    const val YEAR_TICK_2 = 2
    const val YEAR_TICK_3 = 3
    const val YEAR_TICK_9 = 9
    const val YEAR_TICK_24 = 24

    const val JAN_1 = 1
    const val JAN_2 = 2
    const val FEB_1 = 3
    const val FEB_2 = 4
    const val MAR_1 = 5
    const val MAR_2 = 6
    const val APR_1 = 7
    const val APR_2 = 8
    const val MAY_1 = 9
    const val MAY_2 = 10
    const val JUN_1 = 11
    const val JUN_2 = 12
    const val JUL_1 = 13
    const val JUL_2 = 14
    const val AUG_1 = 15
    const val AUG_2 = 16
    const val SEP_1 = 17
    const val SEP_2 = 18
    const val OCT_1 = 19
    const val OCT_2 = 20
    const val NOV_1 = 21
    const val NOV_2 = 22
    const val DEC_1 = 23
    const val DEC_2 = 24
    val potato = PlantData(
        POTATO_MOISTURE,
        POTATO_SUNLIGHT,
        POTATO_HE,
        FEB_2..FEB_2,
        true,
        SEP_1..OCT_2,
        0,
        APR_1..MAY_2,
        (YEAR_TICK_2..YEAR_TICK_24 step 2).toList(),
        emptyList(),
        emptyList(),
        PlantTile.FIELD
    )
    val wheat = PlantData(
        WHEAT_MOISTURE,
        WHEAT_SUNLIGHT,
        WHEAT_HE,
        MAY_1..MAY_1,
        false,
        JUN_1..JUL_1,
        2,
        OCT_1..OCT_2,
        listOf(YEAR_TICK_3, YEAR_TICK_9),
        emptyList(),
        emptyList(),
        PlantTile.FIELD
    )
    val oat = PlantData(
        OAT_MOISTURE,
        OAT_SUNLIGHT,
        OAT_HE,
        0..0,
        false,
        JUL_1..AUG_2,
        2,
        MAR_2..MAR_2,
        listOf(YEAR_TICK_1, YEAR_TICK_2, YEAR_TICK_3),
        emptyList(),
        emptyList(),
        PlantTile.FIELD
    )
    val pumpkin = PlantData(
        PUMPKIN_MOISTURE,
        PUMPKIN_SUNLIGHT,
        PUMPKIN_HE,
        3..FEB_2,
        true,
        SEP_1..OCT_2,
        0,
        MAY_2..JUN_2,
        (YEAR_TICK_2..YEAR_TICK_24 step 2).toList(),
        emptyList(),
        emptyList(),
        PlantTile.FIELD
    )
    val apple = PlantData(
        APPLE_MOISTURE,
        APPLE_SUNLIGHT,
        APPLE_HE,
        APR_2..MAY_1,
        true,
        SEP_1..OCT_1,
        1,
        0..0,
        emptyList(),
        listOf(NOV_1, NOV_2, FEB_1, FEB_2),
        listOf(JUN_1, SEP_1),
        PlantTile.PLANTATION
    )
    val almond = PlantData(
        ALMOND_MOISTURE,
        ALMOND_SUNLIGHT,
        ALMOND_HE,
        FEB_2..MAR_1,
        true,
        AUG_2..OCT_1,
        1,
        0..0,
        emptyList(),
        listOf(NOV_1, NOV_2, FEB_1, FEB_2),
        listOf(JUN_1, SEP_1),
        PlantTile.PLANTATION
    )
    val cherry = PlantData(
        CHERRY_MOISTURE,
        CHERRY_SUNLIGHT,
        CHERRY_HE,
        APR_2..MAY_1,
        true,
        JUL_1..JUL_2,
        1,
        0..0,
        emptyList(),
        listOf(NOV_1, NOV_2, FEB_1, FEB_2),
        listOf(JUN_1),
        PlantTile.PLANTATION
    )
    val grape = PlantData(
        GRAPE_MOISTURE,
        GRAPE_SUNLIGHT,
        GRAPE_HE,
        JUN_2..JUL_1,
        false,
        SEP_1..SEP_1,
        3,
        0..0,
        emptyList(),
        listOf(JUL_2, AUG_1, AUG_2),
        listOf(APR_1, JUL_1),
        PlantTile.PLANTATION
    )
}
