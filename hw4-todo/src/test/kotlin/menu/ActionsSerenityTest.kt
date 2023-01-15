package menu

import data.Priority
import data.Task
import data.TasksRepository
import io.mockk.mockk
import io.mockk.verify
import net.serenitybdd.junit5.SerenityJUnit5Extension
import net.thucydides.core.annotations.Step
import net.thucydides.core.annotations.Steps
import net.thucydides.core.steps.ScenarioSteps
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import java.io.*

@ExtendWith(SerenityJUnit5Extension::class)
class ActionsSerenityTest {

    @Steps
    lateinit var actionsSteps: ActionsSteps

    @Test
    fun addTaskFromMenuTest() {
        System.setIn("SomeName\n2".byteInputStream())
        actionsSteps.addTask()
        actionsSteps.checkTaskWasSaved(Task(name = "SomeName", priority = Priority.MEDIUM))
    }

    @Test
    fun `should execute repository get tasks without parameters`() {
        actionsSteps.listTasks()
        actionsSteps.checkTasksWereGot()
    }

    @Test
    fun `should execute repository get tasks with false parameter`() {
        actionsSteps.listUncompletedTasks()
        actionsSteps.checkUncompletedTasksWereGot()
    }

    @Test
    fun `repository should delete task id that will return from system input`() {
        System.setIn("3".byteInputStream())
        actionsSteps.deleteTask()
        actionsSteps.checkTaskWasDeleted(3)
    }

    @Test
    fun `repository should complete task id that will return from system input`() {
        System.setIn("3".byteInputStream())
        actionsSteps.completeTask()
        actionsSteps.checkTaskWasCompleted(3)
    }

    @Test
    fun `repository should uncomplete task id that will return from system input`() {
        System.setIn("2".byteInputStream())
        actionsSteps.uncompleteTask()
        actionsSteps.checkTaskWasUncompleted(2)
    }

}


open class ActionsSteps : ScenarioSteps() {
    private val repository: TasksRepository = mockk(relaxed = true)

    @Step
    open fun listTasks() {
        listTasksFromMenu(repository)
    }

    @Step
    open fun listUncompletedTasks() {
        listNonCompletedTasksFromMenu(repository)
    }

    @Step
    open fun addTask() {
        addTaskFromMenu(repository)
    }

    @Step
    open fun deleteTask() {
        deleteTasksFromMenu(repository)
    }

    @Step
    open fun completeTask() {
        complete(repository)
    }

    @Step
    open fun uncompleteTask() {
        uncomplete(repository)
    }

    @Step
    open fun checkTaskWasSaved(task: Task) {
        verify(exactly = 1) { repository.addTask(task) }
    }

    @Step
    fun checkTasksWereGot() {
        verify(exactly = 1) { repository.getTasks() }
    }

    @Step
    fun checkUncompletedTasksWereGot() {
        verify(exactly = 1) { repository.getTasks(false) }
    }

    @Step
    open fun checkTaskWasDeleted(taskId: Int) {
        verify(exactly = 1) { repository.deleteTask(taskId) }
    }

    @Step
    open fun checkTaskWasCompleted(taskId: Int) {
        verify(exactly = 1) { repository.completeTask(taskId) }
    }

    @Step
    open fun checkTaskWasUncompleted(taskId: Int) {
        verify(exactly = 1) { repository.uncompleteTask(taskId) }
    }


}