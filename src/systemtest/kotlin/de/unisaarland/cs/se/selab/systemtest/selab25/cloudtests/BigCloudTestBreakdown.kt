package de.unisaarland.cs.se.selab.systemtest.selab25.cloudtests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * tests cloud behavior outside of incidents */
class BigCloudTestFirstTick : TestExtension() {
    override val name = "BigCloudTest1"
    override val description = "Tests 2 ticks of everything clouds do (except incidents)"

    override val map = "bigCloudTest/bigCloudTestMap.json"
    override val farms = "bigCloudTest/bigCloudTestFarms.json"
    override val scenario = "bigCloudTest/bigCloudTestScenario.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 1

    val expectedLineCloud1FirstMove =
        CLOUD1_MOVES + "7 to tile 8."
    val expectedLineCloud1SecondMove =
        CLOUD1_MOVES + "8 to tile 11."
    val expectedLineCloud1ThirdMove =
        CLOUD1_MOVES + "11 to tile 10."
    val expectedLineCloud1FirstSunglightReduction =
        "[DEBUG] Cloud Movement: On tile 11, the amount of sunlight is 95."
    val expectedLineCloud1FourthMove =
        CLOUD1_MOVES + "10 to tile 7."
    val expectedLineCloud1FirstSunlightReduction =
        "[DEBUG] Cloud Movement: On tile 11, the amount of sunlight is 92."

    // 7 = 3
    // 8 = 4
    // 9 = 5 = 1
    // 10 = 6 = 2
    val expectedLineCloud2FirstMove =
        "[INFO] Cloud Movement: Cloud 2 with 3000 L water moved from tile 4 to tile 7."
    val expectedLineCLoud2SunlightReduction = "[DEBUG] Cloud Movement: On tile 4, the amount of sunlight is 95."
    val expectedLineCloud2SecondMove =
        "[INFO] Cloud Movement: Cloud 2 with 3000 L water moved from tile 7 to tile 8."
    val expectedLineCloud2ThirdMove =
        "[INFO] Cloud Movement: Cloud 2 with 3000 L water moved from tile 8 to tile 11."
    val expectedLineCloudMerge1 =
        "[IMPORTANT] Cloud Union: Clouds 1 and 2 united to cloud 7 with 5050 L water and duration 2 on tile 11."
    val expectedLineCloud3FirstMove =
        "[INFO] Cloud Movement: Cloud 3 with 4000 L water moved from tile 5 to tile 2."
    val expectedLineCloud3SunlightReduction =
        "[DEBUG] Cloud Movement: On tile 5, the amount of sunlight is 95."
    val expectedLineCloud4Move = "[INFO] Cloud Movement: Cloud 4 with 4999 L water moved from tile 3 to tile 1."
    val expectedLineCloud4Dissipation = "[INFO] Cloud Dissipation: Cloud 4 got stuck on tile 1."
    val expectedLineCloud5FirstMove =
        "[INFO] Cloud Movement: Cloud 5 with 1000 L water moved from tile 12 to tile 13."
    val expectedLineSecondMerge =
        "[IMPORTANT] Cloud Union: Clouds 6 and 5 united to cloud 8 with 7000 L water and duration 2 on tile 13."
    val expectedLineCloud7Rain = "[IMPORTANT] Cloud Rain: Cloud 7 on tile 11 rained down 100 L water."
    val expectedLineCloud7FirstMove =
        "[INFO] Cloud Movement: Cloud 7 with 4950 L water moved from tile 11 to tile 10."
    val expectedLineCloud7FirstSunlightReduction =
        "[DEBUG] Cloud Movement: On tile 11, the amount of sunlight is 89."
    val expectedLineCloud7SecondMove =
        "[INFO] Cloud Movement: Cloud 7 with 4950 L water moved from tile 10 to tile 7."
    val expectedLineCloud7ThirdMove =
        "[INFO] Cloud Movement: Cloud 7 with 4950 L water moved from tile 7 to tile 8."
    val expectedLineCloud7FourthMove =
        "[INFO] Cloud Movement: Cloud 7 with 4950 L water moved from tile 8 to tile 11."
    val expectedLineCloud7SecondSunlightReduction =
        "[DEBUG] Cloud Movement: On tile 11, the amount of sunlight is 86."
    val expectedLineCloud8FirstRain =
        "[IMPORTANT] Cloud Rain: Cloud 8 on tile 13 rained down 70 L water."
    val expectedLineCloud8FirstMove =
        "[INFO] Cloud Movement: Cloud 8 with 6930 L water moved from tile 13 to tile 5."
    val expectedLineCloud8FirstSunlightReduction =
        "[DEBUG] Cloud Movement: On tile 13, the amount of sunlight is 95."
    val expectedLineCloud8SecondRain =
        "[IMPORTANT] Cloud Rain: Cloud 8 on tile 5 rained down 100 L water."
    val expectedLineCloud8SecondMove =
        "[INFO] Cloud Movement: Cloud 8 with 6830 L water moved from tile 5 to tile 2."
    val expectedLineCloud8SecondSunlightReduction =
        "[DEBUG] Cloud Movement: On tile 5, the amount of sunlight is 92."
    val expectedLineLastMerge =
        "[IMPORTANT] Cloud Union: Clouds 3 and 8 united to cloud 9 with 10830 L water and duration 2 on tile 2."
    val expectedLineCloud9RainAll =
        "[IMPORTANT] Cloud Rain: Cloud 9 on tile 2 rained down 10830 L water."
    val expectedLineCloud9Dissipates =
        "[INFO] Cloud Dissipation: Cloud 9 dissipates on tile 2."
    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, CLOUD_MOVEMENT)
        assertCurrentLine(expectedLineCloud1FirstMove)
        assertNextLine(expectedLineCloud1SecondMove)
        assertNextLine(expectedLineCloud1ThirdMove)
        assertNextLine(expectedLineCloud1FirstSunglightReduction)
        assertNextLine(expectedLineCloud1FourthMove)
        assertNextLine(expectedLineCloud1FirstMove)
        assertNextLine(expectedLineCloud1SecondMove)
        assertNextLine(expectedLineCloud1ThirdMove)
        assertNextLine(expectedLineCloud1FirstSunlightReduction)
        assertNextLine(expectedLineCloud1FourthMove)
        assertNextLine(expectedLineCloud1FirstMove)
        assertNextLine(expectedLineCloud1SecondMove)

        assertNextLine(expectedLineCloud2FirstMove)
        assertNextLine(expectedLineCLoud2SunlightReduction)
        assertNextLine(expectedLineCloud2SecondMove)
        assertNextLine(expectedLineCloud2ThirdMove)
        assertNextLine(expectedLineCloudMerge1)

        assertNextLine(expectedLineCloud3FirstMove)
        assertNextLine(expectedLineCloud3SunlightReduction)

        assertNextLine(expectedLineCloud4Move)
        assertNextLine(expectedLineCloud4Dissipation)

        assertNextLine(expectedLineCloud5FirstMove)
        assertNextLine(expectedLineSecondMerge)

        assertNextLine(expectedLineCloud7Rain)
        assertNextLine(expectedLineCloud7FirstMove)
        assertNextLine(expectedLineCloud7FirstSunlightReduction)
        assertNextLine(expectedLineCloud7SecondMove)
        assertNextLine(expectedLineCloud7ThirdMove)
        assertNextLine(expectedLineCloud7FourthMove)
        assertNextLine(expectedLineCloud7FirstMove)
        assertNextLine(expectedLineCloud7SecondSunlightReduction)
        assertNextLine(expectedLineCloud7SecondMove)
        assertNextLine(expectedLineCloud7ThirdMove)

        assertNextLine(expectedLineCloud8FirstRain)
        assertNextLine(expectedLineCloud8FirstMove)
        assertNextLine(expectedLineCloud8FirstSunlightReduction)
        assertNextLine(expectedLineCloud8SecondRain)
        assertNextLine(expectedLineCloud8SecondMove)
        assertNextLine(expectedLineCloud8SecondSunlightReduction)
        assertNextLine(expectedLineLastMerge)

        assertNextLine(expectedLineCloud9RainAll)
        assertNextLine(expectedLineCloud9Dissipates)
    }
}

/**
 * tests cloud behavior outside of incidents */
class BigCloudTestSecondTick : TestExtension() {
    override val name = "BigCloudTest2"
    override val description = "Tests 2 ticks of everything clouds do (except incidents)"

    override val map = "bigCloudTest/bigCloudTestMap.json"
    override val farms = "bigCloudTest/bigCloudTestFarms.json"
    override val scenario = "bigCloudTest/bigCloudTestScenario.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 1

    val expectedLineSecondTick1stMove =
        "[INFO] Cloud Movement: Cloud 7 with 4950 L water moved from tile 11 to tile 10."
    val expectedLineSecondTick2ndMove =
        "[INFO] Cloud Movement: Cloud 7 with 4950 L water moved from tile 10 to tile 7."
    val expectedLineSecondTickSunlightReduction =
        "[DEBUG] Cloud Movement: On tile 11, the amount of sunlight is 95."
    val expectedLineSecondTick3rdMove =
        "[INFO] Cloud Movement: Cloud 7 with 4950 L water moved from tile 7 to tile 8."
    val expectedLineSecondTick4thMove =
        "[INFO] Cloud Movement: Cloud 7 with 4950 L water moved from tile 8 to tile 11."
    val expectedLineSecondTick2ndSunlightReduction =
        "[DEBUG] Cloud Movement: On tile 11, the amount of sunlight is 92."
    val expectedLineLastSunlightReduction =
        "[DEBUG] Cloud Movement: On tile 11, the amount of sunlight is 89."
    val expectedLineDissipation =
        "[INFO] Cloud Dissipation: Cloud 7 dissipates on tile 10."

    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, "Simulation Info: Tick 1")
        skipUntilLogType(LogLevel.INFO, CLOUD_MOVEMENT)
        assertNextLine(expectedLineSecondTick1stMove)
        assertNextLine(expectedLineSecondTickSunlightReduction)
        assertNextLine(expectedLineSecondTick2ndMove)
        assertNextLine(expectedLineSecondTick3rdMove)
        assertNextLine(expectedLineSecondTick4thMove)
        assertNextLine(expectedLineSecondTick1stMove)
        assertNextLine(expectedLineSecondTick2ndSunlightReduction)
        assertNextLine(expectedLineSecondTick2ndMove)
        assertNextLine(expectedLineSecondTick3rdMove)
        assertNextLine(expectedLineSecondTick4thMove)
        assertNextLine(expectedLineSecondTick1stMove)
        assertNextLine(expectedLineLastSunlightReduction)
        assertNextLine(expectedLineDissipation)
    }
}
