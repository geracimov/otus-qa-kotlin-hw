package ru.geracimov.otus.kotlinqa.hw1

import ru.geracimov.otus.kotlinqa.hw1.dsl.testAround
import ru.geracimov.otus.kotlinqa.hw1.tests.*


fun main() {
    testAround {
        runTest(BeforeTwiceAndAfterTwiceTestClass()) { println("§ BeforeTwiceAndAfterTwiceTestClass RUNNING") }
    }
    testAround {
        runTest(BeforeAndAfterTestClass()) { println("§ BeforeAndAfterTestClass RUNNING") }
    }
    testAround {
        runTest(AfterOnlyTestClass()) { println("§ AfterOnlyTestClass RUNNING") }
    }
    testAround {
        runTest(BeforeOnlyTestClass()) { println("§ BeforeOnlyTestClass RUNNING") }
    }
    testAround {
        runTest(EmptyTestClass()) { println("§ EmptyTestClass RUNNING") }
    }
}