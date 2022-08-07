package ru.geracimov.otus.kotlinqa.hw1.dsl

import ru.geracimov.otus.kotlinqa.hw1.runner.TestAround

fun <T : Any> testAround(initializer: TestAround<T>.() -> Unit): TestAround<T> {
    val testAround = TestAround<T>()
    testAround.initializer()
    return testAround
}