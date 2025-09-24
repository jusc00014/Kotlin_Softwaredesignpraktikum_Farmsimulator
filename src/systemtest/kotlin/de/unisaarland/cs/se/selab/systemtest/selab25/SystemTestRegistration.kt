package de.unisaarland.cs.se.selab.systemtest.selab25

import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.ExampleSystemTest
import de.unisaarland.cs.se.selab.systemtest.selab25.cloudtests.BigCloudTest
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.CloudCreationTest
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.CloudCreationTestOverlapping
import de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests.CloudCreationTestOverlappingDifferentTicks
import de.unisaarland.cs.se.selab.systemtest.selab25.onefieldtest.Onefieldtest

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
        testSuite.registerTest(CloudCreationTest())
        testSuite.registerTest(CloudCreationTestOverlapping())
        testSuite.registerTest(CloudCreationTestOverlappingDifferentTicks())
        testSuite.registerTest(Onefieldtest())
        testSuite.registerTest(BigCloudTest())
    }

    /**
     * Register the tests you want to run against the validation mutants here!
     * The test only check validation, so they log messages will only possibly
     * be incorrect during the parsing/validation.
     * Everything after 'Simulation start' works correctly
     */
    fun registerSystemTestsMutantValidation(testSuite: SELab25TestSuite) {
        testSuite.registerTest(ExampleSystemTest())
    }

    /**
     * The same as above, but the log message only (possibly) become incorrect
     * from the 'Simulation start' log onwards
     */
    fun registerSystemTestsMutantSimulation(testSuite: SELab25TestSuite) {
        testSuite.registerTest(ExampleSystemTest())
    }
}
