package de.unisaarland.cs.se.selab.systemtest.selab25

import de.unisaarland.cs.se.selab.systemtest.selab25.alternativeParserTest.FieldOfOtherFarm
import de.unisaarland.cs.se.selab.systemtest.selab25.alternativeParserTest.IDsNotUnique
import de.unisaarland.cs.se.selab.systemtest.selab25.alternativeParserTest.MachineCanTReach
import de.unisaarland.cs.se.selab.systemtest.selab25.alternativeParserTest.SameMachineId
import de.unisaarland.cs.se.selab.systemtest.selab25.alternativeParserTest.SameMachineName
import de.unisaarland.cs.se.selab.systemtest.selab25.alternativeParserTest.SamePlanId
import de.unisaarland.cs.se.selab.systemtest.selab25.alternativeParserTest.SowingAndVillage
import de.unisaarland.cs.se.selab.systemtest.selab25.alternativeParserTest.SowingPlanDoesTheSame
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.ExampleSystemTest
import de.unisaarland.cs.se.selab.systemtest.selab25.cloudtests.BigCloudTest
import de.unisaarland.cs.se.selab.systemtest.selab25.cloudtests.BigCloudTestFirstTick
import de.unisaarland.cs.se.selab.systemtest.selab25.cloudtests.BigCloudTestSecondTick
import de.unisaarland.cs.se.selab.systemtest.selab25.cloudtests.ClimateChange
import de.unisaarland.cs.se.selab.systemtest.selab25.cloudtests.CloudTestIllRemain
import de.unisaarland.cs.se.selab.systemtest.selab25.cloudtests.CloudTestImTired
import de.unisaarland.cs.se.selab.systemtest.selab25.cloudtests.CloudTestSmall
import de.unisaarland.cs.se.selab.systemtest.selab25.cloudtests.Floodgates
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.CloudCreationTest
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.CloudCreationTestLargeRadius
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.CloudCreationTestNoTileToAffect
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.CloudCreationTestOverlapping
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.CloudCreationTestOverlappingDifferentTicks
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.DroughtTest
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.InvalidAnimalAttackTest
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.InvalidBeeHappyTest
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.InvalidBrokenMachineTest
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.InvalidDroughtTest
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.VillageCreationTest
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.VillageCreationTestChained
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.VillageCreationTestCrossCloudCreation
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.VillageCreationTestCrossCloudValid
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.VillageCreationTestNextToForest
import de.unisaarland.cs.se.selab.systemtest.selab25.machineTravelTest.MachineTravelTest
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
        // endregion ParserTests
        testSuite.registerTest(Onefieldtest())
        testSuite.registerTest(BigCloudTest())
        testSuite.registerTest(Onefieldtestshouldsow())
        testSuite.registerTest(MachineTravelTest())
        testSuite.registerTest(SwissArmyKnife())
        testSuite.registerTest(OneFieldTestTrialMoisture())
        testSuite.registerTest(OneFieldTestTrialSowingTime18())
        testSuite.registerTest(OneFieldTestTrialSowingTime19())
        testSuite.registerTest(OneFieldTestTrialSowingTime20())
        testSuite.registerTest(OneFieldTestTrialIrrigatingAfterDrought())
        testSuite.registerTest(BigCloudTestFirstTick())
        testSuite.registerTest(BigCloudTestSecondTick())
        testSuite.registerTest(InvalidAnimalAttackTest())
        testSuite.registerTest(InvalidBeeHappyTest())
        testSuite.registerTest(InvalidBrokenMachineTest())
        testSuite.registerTest(InvalidDroughtTest())
        testSuite.registerTest(OneFieldTestTrialSowingTime21())
        testSuite.registerTest(OneFieldTestTrialSowingTime22())
        testSuite.registerTest(DroughtTest())
        testSuite.registerTest(FarmSowingAfterTheEnd())
        testSuite.registerTest(FarmInvalidPlantationTile())
        testSuite.registerTest(FarmWOPlantationsAndFields())
        testSuite.registerTest(FarmNoFieldsWSowingPlan())
        testSuite.registerTest(MachineCanTReach())
        testSuite.registerTest(IDsNotUnique())
        testSuite.registerTest(FieldOfOtherFarm())
        testSuite.registerTest(SowingAndVillage())
        testSuite.registerTest(SameMachineId())
        testSuite.registerTest(SameMachineName())
        testSuite.registerTest(SamePlanId())
        testSuite.registerTest(SowingPlanDoesTheSame())
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
        testSuite.registerTest(WindyVillage())
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
    }
}
