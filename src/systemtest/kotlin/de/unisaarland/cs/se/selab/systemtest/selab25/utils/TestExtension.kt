package de.unisaarland.cs.se.selab.systemtest.selab25.utils

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.SystemTestSELab25

/**
 * Test Extension offering useFull LogMessage Validation functions
 */
abstract class TestExtension : SystemTestSELab25() {

    /**
     * Skips until the given [startString] is found
     */
    private suspend fun skipUntilString(startString: String): String {
        val line: String = getNextLine()
            ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        return if (line.startsWith(startString)) {
            line
        } else {
            skipUntilString(startString)
        }
    }

    /**
     * Skips until the given [logLevel] and [logType] is found
     */
    suspend fun skipUntilLogType(logLevel: LogLevel, logType: String): String {
        return skipUntilString("[$logLevel] $logType")
    }
}
