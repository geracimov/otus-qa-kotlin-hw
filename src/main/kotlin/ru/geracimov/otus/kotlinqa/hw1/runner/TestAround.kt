package ru.geracimov.otus.kotlinqa.hw1.runner

import ru.geracimov.otus.kotlinqa.hw1.TestRunner
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredMemberFunctions

private const val BEFORE = "before"
private const val AFTER = "after"

class TestAround<T : Any> : TestRunner<T> {
    private lateinit var stepsMemberFunctions: Collection<KFunction<*>>
    private lateinit var steps: T

    override fun runTest(steps: T, test: () -> Unit) {
        this.steps = steps
        stepsMemberFunctions = steps::class.declaredMemberFunctions

        runAllBefore()
        test.invoke()
        runAllAfter()
    }

    private fun runAllBefore() {
        stepsMemberFunctions.filter { it.name.startsWith(BEFORE) }.forEach {
            println("[$BEFORE] ${it.name}")
            it.call(steps)
        }
    }

    private fun runAllAfter() {
        stepsMemberFunctions.filter { it.name.startsWith(AFTER) }.forEach {
            println("[$AFTER] ${it.name}")
            it.call(steps)
        }
    }
}

