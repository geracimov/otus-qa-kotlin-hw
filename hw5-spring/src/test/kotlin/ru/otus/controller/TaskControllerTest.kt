package ru.otus.controller

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isZero
import assertk.assertions.size
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.web.WebProperties.Resources.Chain.Strategy.Content
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.servlet.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import ru.otus.data.Priority
import ru.otus.data.Task
import ru.otus.data.TasksRepository
import ru.otus.data.TasksRepositoryMemory
import ru.otus.service.TaskService
import java.nio.charset.Charset
import java.util.stream.Stream

@WebMvcTest
@Import(TaskService::class, TasksRepositoryMemory::class)
class TaskControllerTest {

    @Autowired
    lateinit var mvc: MockMvc

    @Autowired
    lateinit var service: TaskService

    @Autowired
    lateinit var mapper: ObjectMapper


    @BeforeEach
    fun setUp() {
        Stream.of(service.getTasks(false), service.getTasks(true)).flatMap { it.stream() }.map { it.id }.distinct().forEach { service.deleteTask(it as Int) }
        service.addTask(Task(3, "Task3", Priority.HIGH, false))
        service.addTask(Task(1, "Task1", Priority.LOW, false))
        service.addTask(Task(2, "Task2", Priority.MEDIUM, false))
    }

    @Test
    fun addTask() {
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
    fun deleteIncorrectTask() {
        mvc.delete("/api/v1/tasks/-41") {
            accept(MediaType.APPLICATION_JSON)
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun deleteCompletedTask() {
        service.completeTask(2)
        mvc.delete("/api/v1/tasks/2") {
            accept(MediaType.APPLICATION_JSON)
        }.andExpect {
            status { isInternalServerError() }
        }
    }

    @Test
    fun deleteTask() {
        mvc.delete("/api/v1/tasks/1") {
            accept(MediaType.APPLICATION_JSON)
        }.andExpect {
            status { isOk() }
        }
    }

    @Test
    fun completeTask() {
        assertThat(service.getTasks(true)).size().isZero()

        mvc.put("/api/v1/tasks/status/2") {
            param("done", "true")
            accept(MediaType.APPLICATION_JSON)
        }.andExpect {
            status { isOk() }
        }

        assertThat(service.getTasks(true)).size().isEqualTo(1)
    }

    @Test
    fun getUncompletedTasks() {
        mvc.get("/api/v1/tasks") {
            param("done", "false")
            accept(MediaType.APPLICATION_JSON)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { json("""[{},{},{}]""") }
        }
    }

    @Test
    fun getCompletedTasks() {
        mvc.get("/api/v1/tasks") {
            param("done", "true")
            accept(MediaType.APPLICATION_JSON)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { json("[]") }
        }
    }
}