package de.unisaarland.cs.se.selab.systemtest.selab25.cloudtests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.TestExtension

/**
 * same as before with some cloud creation and village*/
class BigCloudTestWithIncidents : TestExtension() {
    override val name = "BigCloudTest"
    override val description = "Tests 4 ticks of everything clouds do"

    override val map = "bigCloudTest/bigCloudTestMap.json"
    override val farms = "bigCloudTest/bigCloudTestFarms.json"
    override val scenario = "bigCloudTest/bigCloudTestScenario.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 3
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
        "[IMPORTANT] Cloud Union: Clouds 5 and 6 united to cloud 8 with 7000 L water and duration 1 on tile 13."
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
        "[IMPORTANT] Cloud Union: Clouds 3 and 8 united to cloud 9 with 10830 L water and duration 1 on tile 2."
    val expectedLineCloud9RainAll =
        "[IMPORTANT] Cloud Rain: Cloud 9 on tile 2 rained down 10830 L water."
    val expectedLineCloud9Dissipates =
        "[INFO] Cloud Dissipation: Cloud 9 dissipates on tile 2."

    val expectedLineSecondTick1stMove =
        "[INFO] Cloud Movement: Cloud 7 with 4950 L water moved from tile 8 to tile 11."
    val expectedLineSecondTick2ndMove =
        "[INFO] Cloud Movement: Cloud 7 with 4950 L water moved from tile 11 to tile 10."
    val expectedLineSecondTickSunlightReduction =
        "[DEBUG] Cloud Movement: On tile 11, the amount of sunlight is 109."
    val expectedLineSecondTick3rdMove =
        "[INFO] Cloud Movement: Cloud 7 with 4950 L water moved from tile 10 to tile 7."
    val expectedLineSecondTick4thMove =
        "[INFO] Cloud Movement: Cloud 7 with 4950 L water moved from tile 7 to tile 8."
    val expectedLineSecondTick2ndSunlightReduction =
        "[DEBUG] Cloud Movement: On tile 11, the amount of sunlight is 106."
    val expectedLineLastSunlightReduction =
        "[DEBUG] Cloud Movement: On tile 11, the amount of sunlight is 103."
    val expectedLineSecondTickDissipate =
        "[INFO] Cloud Dissipation: Cloud 9 dissipates on tile 10."

    override suspend fun run() {
        assert(skipUntilLogType(LogLevel.INFO, CLOUD_MOVEMENT) == expectedLineCloud1FirstMove)
        assert(skipUntilLogType(LogLevel.INFO, CLOUD_MOVEMENT) == expectedLineCloud1SecondMove)
        assert(skipUntilLogType(LogLevel.INFO, CLOUD_MOVEMENT) == expectedLineCloud1ThirdMove)
        assert(skipUntilLogType(LogLevel.DEBUG, CLOUD_MOVEMENT) == expectedLineCloud1FirstSunglightReduction)
        assert(skipUntilLogType(LogLevel.INFO, CLOUD_MOVEMENT) == expectedLineCloud1FourthMove)
        assert(skipUntilLogType(LogLevel.INFO, CLOUD_MOVEMENT) == expectedLineCloud1FirstMove)
        assert(skipUntilLogType(LogLevel.INFO, CLOUD_MOVEMENT) == expectedLineCloud1SecondMove)
        assert(skipUntilLogType(LogLevel.INFO, CLOUD_MOVEMENT) == expectedLineCloud1ThirdMove)
        assert(skipUntilLogType(LogLevel.DEBUG, CLOUD_MOVEMENT) == expectedLineCloud1FirstSunlightReduction)
        assert(skipUntilLogType(LogLevel.INFO, CLOUD_MOVEMENT) == expectedLineCloud1FirstMove)
        assert(skipUntilLogType(LogLevel.INFO, CLOUD_MOVEMENT) == expectedLineCloud1SecondMove)

        assert(skipUntilLogType(LogLevel.INFO, CLOUD_MOVEMENT) == expectedLineCloud2FirstMove)
        assert(skipUntilLogType(LogLevel.DEBUG, CLOUD_MOVEMENT) == expectedLineCLoud2SunlightReduction)
        assert(skipUntilLogType(LogLevel.INFO, CLOUD_MOVEMENT) == expectedLineCloud2SecondMove)
        assert(skipUntilLogType(LogLevel.INFO, CLOUD_MOVEMENT) == expectedLineCloud2ThirdMove)
        assert(skipUntilLogType(LogLevel.IMPORTANT, CLOUD_UNION) == expectedLineCloudMerge1)

        assert(skipUntilLogType(LogLevel.INFO, CLOUD_MOVEMENT) == expectedLineCloud3FirstMove)
        assert(skipUntilLogType(LogLevel.DEBUG, CLOUD_DISSIPATION) == expectedLineCloud3SunlightReduction)

        assert(skipUntilLogType(LogLevel.INFO, CLOUD_MOVEMENT) == expectedLineCloud4Move)
        assert(skipUntilLogType(LogLevel.INFO, CLOUD_DISSIPATION) == expectedLineCloud4Dissipation)

        assert(skipUntilLogType(LogLevel.INFO, CLOUD_MOVEMENT) == expectedLineCloud5FirstMove)
        assert(skipUntilLogType(LogLevel.IMPORTANT, CLOUD_UNION) == expectedLineSecondMerge)

        assert(skipUntilLogType(LogLevel.IMPORTANT, CLOUD_RAIN) == expectedLineCloud7Rain)
        assert(skipUntilLogType(LogLevel.INFO, CLOUD_MOVEMENT) == expectedLineCloud7FirstMove)
        assert(skipUntilLogType(LogLevel.DEBUG, CLOUD_MOVEMENT) == expectedLineCloud7FirstSunlightReduction)
        assert(skipUntilLogType(LogLevel.INFO, CLOUD_MOVEMENT) == expectedLineCloud7SecondMove)
        assert(skipUntilLogType(LogLevel.INFO, CLOUD_MOVEMENT) == expectedLineCloud7ThirdMove)
        assert(skipUntilLogType(LogLevel.INFO, CLOUD_MOVEMENT) == expectedLineCloud7FourthMove)
        assert(skipUntilLogType(LogLevel.INFO, CLOUD_MOVEMENT) == expectedLineCloud7FirstMove)
        assert(skipUntilLogType(LogLevel.DEBUG, CLOUD_MOVEMENT) == expectedLineCloud7SecondSunlightReduction)
        assert(skipUntilLogType(LogLevel.INFO, CLOUD_MOVEMENT) == expectedLineCloud7SecondMove)
        assert(skipUntilLogType(LogLevel.INFO, CLOUD_MOVEMENT) == expectedLineCloud7ThirdMove)

        assert(skipUntilLogType(LogLevel.IMPORTANT, CLOUD_RAIN) == expectedLineCloud8FirstRain)
        assert(skipUntilLogType(LogLevel.INFO, CLOUD_MOVEMENT) == expectedLineCloud8FirstMove)
        assert(skipUntilLogType(LogLevel.DEBUG, CLOUD_MOVEMENT) == expectedLineCloud8FirstSunlightReduction)
        assert(skipUntilLogType(LogLevel.IMPORTANT, CLOUD_RAIN) == expectedLineCloud8SecondRain)
        assert(skipUntilLogType(LogLevel.INFO, CLOUD_MOVEMENT) == expectedLineCloud8SecondMove)
        assert(skipUntilLogType(LogLevel.INFO, CLOUD_MOVEMENT) == expectedLineCloud8SecondSunlightReduction)
        assert(skipUntilLogType(LogLevel.IMPORTANT, CLOUD_UNION) == expectedLineLastMerge)

        assert(skipUntilLogType(LogLevel.IMPORTANT, CLOUD_RAIN) == expectedLineCloud9RainAll)
        assert(skipUntilLogType(LogLevel.INFO, CLOUD_DISSIPATION) == expectedLineCloud9Dissipates)

        assert(skipUntilLogType(LogLevel.INFO, CLOUD_MOVEMENT) == expectedLineSecondTick1stMove)
        assert(skipUntilLogType(LogLevel.INFO, CLOUD_MOVEMENT) == expectedLineSecondTick2ndMove)
        assert(skipUntilLogType(LogLevel.INFO, CLOUD_MOVEMENT) == expectedLineSecondTickSunlightReduction)
        assert(skipUntilLogType(LogLevel.INFO, CLOUD_MOVEMENT) == expectedLineSecondTick3rdMove)
        assert(skipUntilLogType(LogLevel.INFO, CLOUD_MOVEMENT) == expectedLineSecondTick4thMove)
        assert(skipUntilLogType(LogLevel.INFO, CLOUD_MOVEMENT) == expectedLineSecondTick1stMove)
        assert(skipUntilLogType(LogLevel.INFO, CLOUD_MOVEMENT) == expectedLineSecondTick2ndMove)
        assert(skipUntilLogType(LogLevel.INFO, CLOUD_MOVEMENT) == expectedLineSecondTick2ndSunlightReduction)
        assert(skipUntilLogType(LogLevel.INFO, CLOUD_MOVEMENT) == expectedLineSecondTick3rdMove)
        assert(skipUntilLogType(LogLevel.INFO, CLOUD_MOVEMENT) == expectedLineSecondTick4thMove)
        assert(skipUntilLogType(LogLevel.INFO, CLOUD_MOVEMENT) == expectedLineSecondTick1stMove)
        assert(skipUntilLogType(LogLevel.INFO, CLOUD_MOVEMENT) == expectedLineSecondTick2ndMove)
        assert(skipUntilLogType(LogLevel.INFO, CLOUD_MOVEMENT) == expectedLineLastSunlightReduction)
        assert(skipUntilLogType(LogLevel.DEBUG, CLOUD_DISSIPATION) == expectedLineSecondTickDissipate)
    }
}
