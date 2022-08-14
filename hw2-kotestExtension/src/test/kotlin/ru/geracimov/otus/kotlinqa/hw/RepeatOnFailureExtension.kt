package ru.geracimov.otus.kotlinqa.hw

import io.kotest.core.annotation.AutoScan
import io.kotest.core.extensions.TestCaseExtension
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult

@AutoScan
class RepeatOnFailureExtension : TestCaseExtension {
    private val maxExecCount = 3

    override suspend fun intercept(testCase: TestCase, execute: suspend (TestCase) -> TestResult): TestResult {
        var testResult: TestResult
        var i = 1

        beforeTest(testCase)
        do {
            testResult = execute.invoke(testCase)
            attempt(testCase, testResult, i)
        } while (testResult.isErrorOrFailure && ++i <= maxExecCount)
        afterTest(testCase, testResult)

        return testResult
    }

    private fun attempt(testCase: TestCase, testResult: TestResult, num: Int) =
        println("[ATTEMPT-$num/${maxExecCount}] test \"${testCase.name.testName}\" result is success: ${testResult.isSuccess}")

    private fun beforeTest(testCase: TestCase) =
        println("[BEFORE EXEC] test \"${testCase.name.testName}\" is running...")

    private fun afterTest(testCase: TestCase, testResult: TestResult) =
        println("[AFTER EXEC] test \"${testCase.name.testName}\" finished with result $testResult")

}