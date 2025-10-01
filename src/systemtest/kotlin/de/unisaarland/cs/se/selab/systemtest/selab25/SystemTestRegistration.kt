package de.unisaarland.cs.se.selab.systemtest.selab25

import de.unisaarland.cs.se.selab.systemtest.selab25.alternativeParserTest.FieldOfOtherFarm
import de.unisaarland.cs.se.selab.systemtest.selab25.alternativeParserTest.SowingAndVillage
import de.unisaarland.cs.se.selab.systemtest.selab25.alternativeParserTest.SowingPlanDoesTheSame
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.ExampleSystemTest
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.MissingFarmsteadTest
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.MissingPlantationTest
import de.unisaarland.cs.se.selab.systemtest.selab25.cloudtests.BigCloudTest
import de.unisaarland.cs.se.selab.systemtest.selab25.cloudtests.ClimateChange
import de.unisaarland.cs.se.selab.systemtest.selab25.cloudtests.CloudTestIllRemain
import de.unisaarland.cs.se.selab.systemtest.selab25.cloudtests.CloudTestImTired
import de.unisaarland.cs.se.selab.systemtest.selab25.cloudtests.CloudTestSmall
import de.unisaarland.cs.se.selab.systemtest.selab25.cloudtests.Floodgates
import de.unisaarland.cs.se.selab.systemtest.selab25.everythingTests.BigBigHostileTakeOverFarmSteadsTest
import de.unisaarland.cs.se.selab.systemtest.selab25.everythingTests.BigBigHostiletakeOverFieldsTest
import de.unisaarland.cs.se.selab.systemtest.selab25.everythingTests.BigBigTestLowMoisture
import de.unisaarland.cs.se.selab.systemtest.selab25.everythingTests.BigBigTestOctober1
import de.unisaarland.cs.se.selab.systemtest.selab25.farmactiontests.HarvestEstimateSowingEarlyTest
import de.unisaarland.cs.se.selab.systemtest.selab25.farmactiontests.MachineOrder
import de.unisaarland.cs.se.selab.systemtest.selab25.farmactiontests.MachineTimeOut
import de.unisaarland.cs.se.selab.systemtest.selab25.farmactiontests.MachineTravelTrial
import de.unisaarland.cs.se.selab.systemtest.selab25.farmactiontests.NonPrioritizedTest17
import de.unisaarland.cs.se.selab.systemtest.selab25.farmactiontests.SowingEarlyTest
import de.unisaarland.cs.se.selab.systemtest.selab25.havestTest.CherryLateHarvestTest3T
import de.unisaarland.cs.se.selab.systemtest.selab25.havestTest.CherryLateHarvestTest4T
import de.unisaarland.cs.se.selab.systemtest.selab25.havestTest.LateHarvestTest
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.AnimalAttackTest
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.AnimalAttackTestStacking
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.BeeHappyTest
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.BeeHappyTest2
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.BeeHappyTest3Overlapping
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.BeeHappyTestInvalid
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.BrokenMachineTest
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.CloudCreationTest
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.CloudCreationTestLargeRadius
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.CloudCreationTestNoTileToAffect
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.CloudCreationTestOverlapping
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.CloudCreationTestOverlappingDifferentTicks
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.DroughtTest
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.DroughtTestDryDryDesert
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.InvalidAnimalAttackTest
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.InvalidBeeHappyTest
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.InvalidBrokenMachineTest
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.InvalidDroughtTest
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.LongBrokenMachineTest
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.VeryLongBrokenMachineTest
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.VillageCreationTest
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.VillageCreationTestChained
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.VillageCreationTestCrossCloudCreation
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.VillageCreationTestCrossCloudValid
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.VillageCreationTestNextToForest
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.WeirdBeeHappyTest
import de.unisaarland.cs.se.selab.systemtest.selab25.machineMutantsHunt.MachineTest2
import de.unisaarland.cs.se.selab.systemtest.selab25.machineMutantsHunt.MachineTest3
import de.unisaarland.cs.se.selab.systemtest.selab25.machineMutantsHunt.MachineTest3x2
import de.unisaarland.cs.se.selab.systemtest.selab25.machineMutantsHunt.UltimateMachineTest
import de.unisaarland.cs.se.selab.systemtest.selab25.machineTravelTest.MachineTravelTest
import de.unisaarland.cs.se.selab.systemtest.selab25.machineTravelTest.SisyphusMachineFarFromHomeTest
import de.unisaarland.cs.se.selab.systemtest.selab25.mutant.SwissArmyKnife
import de.unisaarland.cs.se.selab.systemtest.selab25.onefieldtest.OneFieldTestTrialIrrigatingAfterDrought
import de.unisaarland.cs.se.selab.systemtest.selab25.onefieldtest.OneFieldTestTrialMoisture
import de.unisaarland.cs.se.selab.systemtest.selab25.onefieldtest.OneFieldTestTrialSowingTime18
import de.unisaarland.cs.se.selab.systemtest.selab25.onefieldtest.OneFieldTestTrialSowingTime19
import de.unisaarland.cs.se.selab.systemtest.selab25.onefieldtest.OneFieldTestTrialSowingTime20
import de.unisaarland.cs.se.selab.systemtest.selab25.onefieldtest.OneFieldTestTrialSowingTime21
import de.unisaarland.cs.se.selab.systemtest.selab25.onefieldtest.OneFieldTestTrialSowingTime22
import de.unisaarland.cs.se.selab.systemtest.selab25.onefieldtest.Onefieldtest
import de.unisaarland.cs.se.selab.systemtest.selab25.onefieldtest.Onefieldtestshouldsow
import de.unisaarland.cs.se.selab.systemtest.selab25.plantTests.FallowSowPlanPotato
import de.unisaarland.cs.se.selab.systemtest.selab25.plantTests.InitializePlantationTick19Test
import de.unisaarland.cs.se.selab.systemtest.selab25.plantTests.InitializePlantationTick21Test
import de.unisaarland.cs.se.selab.systemtest.selab25.sowingPlanTest.SowingPlanTest

/**
 * Used for test registration
 */
object SystemTestRegistration {
    /**
     * Register your tests to run against the reference implementation!
     * This can also be used to debug our system test, or to see if we
     * understood something correctly or not (everything should work
     * the same as their reference implementation)
     */
    fun registerSystemTestsForReferenceImplementation(testSuite: SELab25TestSuite) {
        testSuite.registerTest(ExampleSystemTest())
        // region ParserTests
        testSuite.registerTest(FarmPlantationIdentifiesAsField())
        // endregion ParserTests
        testSuite.registerTest(DroughtTest())
        testSuite.registerTest(BigBigTestOctober1())
        testSuite.registerTest(LateHarvestTest())
        testSuite.registerTest(FieldOfOtherFarm())
        testSuite.registerTest(SowingAndVillage())
        testSuite.registerTest(SowingPlanDoesTheSame())
        testSuite.registerTest(SowingEarlyTest())
        testSuite.registerTest(BigBigHostiletakeOverFieldsTest())
        testSuite.registerTest(BigBigHostileTakeOverFarmSteadsTest())
        testSuite.registerTest(UltimateMachineTest())
        testSuite.registerTest(BigBigTestLowMoisture())
        testSuite.registerTest(LongBrokenMachineTest())
        testSuite.registerTest(VeryLongBrokenMachineTest())
        // region PlantationLateHarvestTests
        testSuite.registerTest(CherryLateHarvestTest3T())
        testSuite.registerTest(CherryLateHarvestTest4T())
        testSuite.registerTest(MachineTest2())
        testSuite.registerTest(MachineTest3())
        testSuite.registerTest(MachineTest3x2())
        // endregion PlantationLateHarvestTests
    }

    /**
     * Register the tests you want to run against the validation mutants here!
     * The test only check validation, so they log messages will only possibly
     * be incorrect during the parsing/validation.
     * Everything after 'Simulation start' works correctly
     */
    fun registerSystemTestsMutantValidation(testSuite: SELab25TestSuite) {
        testSuite.registerTest(CloudCreationTestOverlapping())
        testSuite.registerTest(CloudCreationTestNoTileToAffect())
        testSuite.registerTest(VillageCreationTestNextToForest())
        testSuite.registerTest(VillageCreationTestCrossCloudCreation())
        testSuite.registerTest(FullData())
        testSuite.registerTest(FarmsTimes2())
        testSuite.registerTest(ShedlessFarm())
        testSuite.registerTest(ForestVillage())
        testSuite.registerTest(ShedWrongNeighbourhood())
        testSuite.registerTest(OwnedRoad())
        testSuite.registerTest(TileSameID())
        testSuite.registerTest(MachineClone())
        testSuite.registerTest(MachineDoppelganger())
        testSuite.registerTest(MachineWrongShed())
        testSuite.registerTest(MachineHomeless())
        testSuite.registerTest(FarmImposter())
        testSuite.registerTest(FarmSplitCustody())
        testSuite.registerTest(CloudyVillage())
        testSuite.registerTest(TwinClouds())
        testSuite.registerTest(InvalidAnimalAttackTest())
        testSuite.registerTest(InvalidBeeHappyTest())
        testSuite.registerTest(InvalidBrokenMachineTest())
        testSuite.registerTest(InvalidDroughtTest())
        testSuite.registerTest(SwissArmyKnife())
        testSuite.registerTest(FarmSowingAfterTheEnd())
        testSuite.registerTest(FarmInvalidPlantationTile())
        testSuite.registerTest(FarmWOPlantationsAndFields())
        testSuite.registerTest(FarmNoFieldsWSowingPlan())
        testSuite.registerTest(VillageExpansionNearForest())
        testSuite.registerTest(VillagePlantationTakeover())
        testSuite.registerTest(MissingPlantationTest())
        testSuite.registerTest(MissingFarmsteadTest())
        testSuite.registerTest(WindyVillage())
        testSuite.registerTest(VillageWithAirflow())
        testSuite.registerTest(TileSameCoordinates())
        testSuite.registerTest(InvalidCategoryForCoord())
        testSuite.registerTest(MachineDuplicateActions())
    }

    /**
     * The same as above, but the log message only (possibly) become incorrect
     * from the 'Simulation start' log onwards
     */
    fun registerSystemTestsMutantSimulation(testSuite: SELab25TestSuite) {
        testSuite.registerTest(CloudCreationTest())
        testSuite.registerTest(CloudCreationTestOverlappingDifferentTicks())
        testSuite.registerTest(VillageCreationTest())
        testSuite.registerTest(CloudCreationTestLargeRadius())
        testSuite.registerTest(CloudTestSmall())
        testSuite.registerTest(CloudTestIllRemain())
        testSuite.registerTest(CloudTestImTired())
        testSuite.registerTest(ClimateChange())
        testSuite.registerTest(VillageCreationTestChained())
        testSuite.registerTest(Floodgates())
        testSuite.registerTest(VillageCreationTestCrossCloudValid())
        testSuite.registerTest(Onefieldtest())
        testSuite.registerTest(BigCloudTest())
        testSuite.registerTest(Onefieldtestshouldsow())
        testSuite.registerTest(MachineTravelTest())
        testSuite.registerTest(OneFieldTestTrialMoisture())
        testSuite.registerTest(OneFieldTestTrialSowingTime18())
        testSuite.registerTest(OneFieldTestTrialSowingTime19())
        testSuite.registerTest(OneFieldTestTrialSowingTime20())
        testSuite.registerTest(OneFieldTestTrialIrrigatingAfterDrought())
        testSuite.registerTest(OneFieldTestTrialSowingTime21())
        testSuite.registerTest(OneFieldTestTrialSowingTime22())
        testSuite.registerTest(DroughtTestDryDryDesert())
        testSuite.registerTest(MachineTimeOut())
        testSuite.registerTest(MachineOrder())
        testSuite.registerTest(SowingPlanTest())
        testSuite.registerTest(InitializePlantationTick19Test())
        testSuite.registerTest(InitializePlantationTick21Test())
        testSuite.registerTest(SisyphusMachineFarFromHomeTest())
        testSuite.registerTest(SowingEarlyTest())
        testSuite.registerTest(HarvestEstimateSowingEarlyTest())
        testSuite.registerTest(NonPrioritizedTest17())
        testSuite.registerTest(AnimalAttackTest())
        testSuite.registerTest(AnimalAttackTestStacking())
        testSuite.registerTest(BeeHappyTest2())
        testSuite.registerTest(BeeHappyTest())
        testSuite.registerTest(BeeHappyTest3Overlapping())
        testSuite.registerTest(BeeHappyTestInvalid())
        testSuite.registerTest(FallowSowPlanPotato())
        testSuite.registerTest(MachineTravelTrial())
        testSuite.registerTest(BrokenMachineTest())
        testSuite.registerTest(WeirdBeeHappyTest())
        testSuite.registerTest(LongBrokenMachineTest())
        testSuite.registerTest(VeryLongBrokenMachineTest())
    }
}
