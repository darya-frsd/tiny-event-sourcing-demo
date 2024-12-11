package ru.quipy.projections

import org.springframework.stereotype.Service
import ru.quipy.api.aggregates.TaskAggregate
import ru.quipy.api.events.*
import ru.quipy.streams.annotation.AggregateSubscriber
import ru.quipy.streams.annotation.SubscribeEvent
import java.util.*

data class TaskProjection(
        val taskId: UUID,
        val projectId: UUID,
        val title: String,
        val description: String,
        val statusId: UUID,
        val assignedUserId: UUID?,
        val comments: List<String>
)
@Service
@AggregateSubscriber(aggregateClass = TaskAggregate::class, subscriberName = "task-projection")
class TaskProjectionService {
    private val tasks: MutableMap<UUID, TaskProjection> = mutableMapOf()

    @SubscribeEvent
    fun apply(event: TaskCreatedEvent) {
        tasks[event.taskId] = TaskProjection(
                taskId = event.taskId,
                projectId = event.projectId,
                title = event.title,
                description = event.description,
                statusId = event.statusId,
                assignedUserId = null,
                comments = listOf()
        )
    }

    @SubscribeEvent
    fun apply(event: TaskRenamedEvent) {
        tasks[event.taskId]?.let {
            tasks[event.taskId] = it.copy(title = event.newTitle)
        }
    }

    @SubscribeEvent
    fun apply(event: TaskAssignedToUserEvent) {
        tasks[event.taskId]?.let {
            tasks[event.taskId] = it.copy(assignedUserId = event.userId)
        }
    }

    @SubscribeEvent
    fun apply(event: TaskStatusUpdatedEvent) {
        tasks[event.taskId]?.let {
            tasks[event.taskId] = it.copy(statusId = event.newStatusId)
        }
    }

    fun getTask(taskId: UUID): TaskProjection? = tasks[taskId]

    fun getTasksByProject(projectId: UUID): List<TaskProjection> =
            tasks.values.filter { it.projectId == projectId }
}