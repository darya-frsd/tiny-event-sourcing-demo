package ru.quipy.projections

import ru.quipy.api.events.*
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
class TaskProjectionService {
    private val tasks: MutableMap<UUID, TaskProjection> = mutableMapOf()

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

    fun apply(event: TaskAssignedToUserEvent) {
        tasks[event.taskId]?.let {
            tasks[event.taskId] = it.copy(assignedUserId = event.userId)
        }
    }

    fun apply(event: TaskStatusUpdatedEvent) {
        tasks[event.taskId]?.let {
            tasks[event.taskId] = it.copy(statusId = event.newStatusId)
        }
    }

    fun getTask(taskId: UUID): TaskProjection? = tasks[taskId]

    fun getTasksByProject(projectId: UUID): List<TaskProjection> =
            tasks.values.filter { it.projectId == projectId }
}