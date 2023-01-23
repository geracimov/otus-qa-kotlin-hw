package ru.otus.controller

import TestConfig
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import org.hamcrest.core.IsEqual
import kotlin.test.Test
import kotlin.test.BeforeTest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*
import ru.otus.data.Priority
import ru.otus.data.Task
import ru.otus.data.TasksRepository
import ru.otus.service.TaskService

@WebMvcTest
@Import(TestConfig::class)
class TaskControllerTest {

    @Autowired
    lateinit var repo: TasksRepository

    @Autowired
    lateinit var service: TaskService

    @Autowired
    lateinit var mvc: MockMvc

    @Autowired
    lateinit var mapper: ObjectMapper

    private val completedTask1 = Task(id = 1, name = "NewTask1", priority = Priority.LOW, completed = true)
    private val completedTask2 = Task(id = 2, name = "NewTask2", priority = Priority.MEDIUM, completed = true)
    private val completedTask3 = Task(id = 3, name = "NewTask3", priority = Priority.HIGH, completed = true)
    private val uncompletedTask1 = Task(id = 4, name = "NewTask4", priority = Priority.LOW)
    private val uncompletedTask2 = Task(id = 5, name = "NewTask5", priority = Priority.MEDIUM)
    private val uncompletedTask3 = Task(id = 6, name = "NewTask6", priority = Priority.HIGH)
    private val uncompletedTask4 = Task(id = 7, name = "NewTask7", priority = Priority.HIGH)

    @BeforeTest
    fun setUp() {
        every { repo.getTasks(true) } returns listOf(completedTask1, completedTask2, completedTask3)
        every { repo.getTasks(false) } returns listOf(uncompletedTask1, uncompletedTask2, uncompletedTask3, uncompletedTask4)
    }


    @Test
    fun addCorrectNewTaskTest() {
        every { repo.addTask(any()) } returns 4

        val newTask = Task(name = "NewTask", priority = Priority.HIGH)
        mvc.post("/api/v1/tasks") {
            accept(MediaType.APPLICATION_JSON)
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(newTask)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { json("""{"id":4, "name":"NewTask", "priority": "HIGH", "completed": false}""") }
        }
    }

    @Test
    fun addAlreadyCompletedNewTaskTest() {
        val newTask = Task(name = "NewTask", priority = Priority.HIGH, completed = true)

        mvc.post("/api/v1/tasks") {
            accept(MediaType.APPLICATION_JSON)
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(newTask)
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun deleteIncorrectTaskTest() {
        mvc.delete("/api/v1/tasks/-41") {
            accept(MediaType.APPLICATION_JSON)
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun deleteCompletedTaskTest() {
        mvc.delete("/api/v1/tasks/2") {
            accept(MediaType.APPLICATION_JSON)
        }.andExpect {
            status { isInternalServerError() }
        }
    }

    @Test
    fun deleteTaskTest() {
        val uncompletedTask = Task(id = 1, name = "NewTask", priority = Priority.MEDIUM)
        every { repo.getTasks(true) } returns listOf()
        every { repo.getTasks(false) } returns listOf(uncompletedTask)
        every { repo.deleteTask(1) } returns Unit

        mvc.delete("/api/v1/tasks/1") {
            accept(MediaType.APPLICATION_JSON)
        }.andExpect {
            status { isOk() }
        }
    }

    @Test
    fun completeTaskTest() {
        every { repo.completeTask(5) } returns Unit

        mvc.put("/api/v1/tasks/status/5") {
            param("done", "true")
            accept(MediaType.APPLICATION_JSON)
        }.andExpect {
            status { isOk() }
        }
    }

    @Test
    fun completeCompletedTaskTest() {
        mvc.put("/api/v1/tasks/status/2") {
            param("done", "true")
            accept(MediaType.APPLICATION_JSON)
        }.andExpect {
            status { isAlreadyReported() }
        }
    }

    @Test
    fun completeInvalidTaskTest() {
        mvc.put("/api/v1/tasks/status/-200") {
            param("done", "true")
            accept(MediaType.APPLICATION_JSON)
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun uncompleteTaskTest() {
        every { repo.uncompleteTask(3) } returns Unit

        mvc.put("/api/v1/tasks/status/3") {
            param("done", "false")
            accept(MediaType.APPLICATION_JSON)
        }.andExpect {
            status { isOk() }
        }
    }

    @Test
    fun uncompleteCompletedTaskTest() {
        mvc.put("/api/v1/tasks/status/5") {
            param("done", "false")
            accept(MediaType.APPLICATION_JSON)
        }.andExpect {
            status { isAlreadyReported() }
        }
    }

    @Test
    fun uncompleteInvalidTaskTest() {
        mvc.put("/api/v1/tasks/status/-100") {
            param("done", "false")
            accept(MediaType.APPLICATION_JSON)
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun getUncompletedTasksTest() {
        every { repo.completeTask(3) } returns Unit

        mvc.get("/api/v1/tasks") {
            param("done", "false")
            accept(MediaType.APPLICATION_JSON)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonPath("$.length()", IsEqual(4)) }
        }
    }

    @Test
    fun getCompletedTasksTest() {
        mvc.get("/api/v1/tasks") {
            param("done", "true")
            accept(MediaType.APPLICATION_JSON)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonPath("$.length()", IsEqual(3)) }
        }
    }
}