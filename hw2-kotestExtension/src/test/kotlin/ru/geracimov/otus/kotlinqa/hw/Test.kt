package ru.geracimov.otus.kotlinqa.hw

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Test : FunSpec({

    test("successTest") {
        withContext(Dispatchers.IO) {
            Thread.sleep(200)
        }
        1 + 2 shouldBe 3
    }
    test("failedTest") {
        withContext(Dispatchers.IO) {
            Thread.sleep(200)
        }
        1 + 2 shouldBe 4
    }

    test("errorTest") {
        withContext(Dispatchers.IO) {
            Thread.sleep(200)
        }
        throw IllegalStateException("some exception thrown")
    }

})
