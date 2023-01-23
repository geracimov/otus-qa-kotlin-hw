//import org.springframework.web.context.WebApplicationContext.*
import io.mockk.mockk
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import ru.otus.data.TasksRepository
import ru.otus.service.TaskService

@TestConfiguration
open class TestConfig {
    @Bean
    open fun tasksRepository(): TasksRepository {
        return mockk()
    }

    @Bean
    open fun taskService(tasksRepository: TasksRepository): TaskService {
        return TaskService(tasksRepository)
    }

}