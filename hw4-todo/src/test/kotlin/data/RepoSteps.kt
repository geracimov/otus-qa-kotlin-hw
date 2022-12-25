package data

import io.cucumber.java8.En
import kotlin.test.assertEquals


class repoSteps : En {
    init {
        lateinit var tasksRepository: TasksRepository
        Given("Task repository") {
            tasksRepository = TasksRepositoryMemory()
        }
        And("Task repository has {int} active tasks") { int: Int ->
            for (i in 1..int)
                tasksRepository.addTask(Task(i, "TaskNo$i", Priority.MEDIUM))
        }
        And("Task repository has {int} completed tasks") { int: Int ->
            for (i in 1..int)
                tasksRepository.addTask(Task(i, "TaskNo$i", Priority.MEDIUM, true))
        }
        When("Task is created") {
            tasksRepository.addTask(Task(name = "dd", priority = Priority.MEDIUM))
        }
        Then("Task count in task repository are equals {int}") { int: Int ->
            assertEquals(int, tasksRepository.getTasks().size)
        }
        Then("Uncompleted task count in task repository are equals {int}") { int: Int ->
            assertEquals(int, tasksRepository.getTasks(false).size)
        }
        Then("Completed task count in task repository are equals {int}") { int: Int ->
            assertEquals(int, tasksRepository.getTasks(true).size)
        }
        When("Repository getTasks called") {
            tasksRepository.getTasks()
        }
        When("Task would delete with id {int}") { int: Int ->
            tasksRepository.deleteTask(int)
        }
    }
}