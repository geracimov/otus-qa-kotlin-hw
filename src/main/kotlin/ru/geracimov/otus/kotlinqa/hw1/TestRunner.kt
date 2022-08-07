package ru.geracimov.otus.kotlinqa.hw1

interface TestRunner<T> {
    fun runTest(steps: T, test: () -> Unit)
}


