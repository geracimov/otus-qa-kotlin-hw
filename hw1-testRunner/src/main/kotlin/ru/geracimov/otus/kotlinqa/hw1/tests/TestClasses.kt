package ru.geracimov.otus.kotlinqa.hw1.tests


class BeforeTwiceAndAfterTwiceTestClass {
    fun beforeFirst() {
        "before first".log()
    }

    fun beforeSecond() {

        "before second".log()
    }


    fun afterFirst() {
        "after first".log()
    }

    fun afterSecond() {
        "after second".log()
    }
}

class BeforeAndAfterTestClass {
    fun beforeAlone() {
        "before".log()
    }

    fun afterAlone() {
        "after".log()
    }
}

class AfterOnlyTestClass {
    fun afterOnly() {
        "after".log()
    }
}

class BeforeOnlyTestClass {
    fun beforeOnly() {
        "before".log()
    }
}

class EmptyTestClass


fun String.log() {
    println("- $this running...")
}