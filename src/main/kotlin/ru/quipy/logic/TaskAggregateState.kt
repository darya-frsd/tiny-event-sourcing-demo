package ru.quipy.logic

import ru.quipy.api.*
import java.util.*

class TaskAggregateState {
    lateinit var taskId: UUID
    lateinit var projectId: UUID
    lateinit var title: String
    lateinit var description: String
    lateinit var statusId: UUID
    var assignedUserId: UUID? = null
    val comments: MutableList<String> = mutableListOf()

    fun createTask(projectId: UUID, title: String, description: String, statusId: UUID): TaskCreatedEvent {
        if (::taskId.isInitialized) throw IllegalStateException("Task already exists.")
        return TaskCreatedEvent(
                taskId = UUID.randomUUID(),
                projectId = projectId,
                title = title,
                description = description,
                statusId = statusId
        )
    }

    fun assignUserToTask(userId: UUID): UserAssignedToTaskEvent {
        return UserAssignedToTaskEvent(
                taskId = this.taskId,
                userId = userId
        )
    }

    fun updateTaskStatus(newStatusId: UUID): TaskStatusUpdatedEvent {
        return TaskStatusUpdatedEvent(
                taskId = this.taskId,
                statusId = newStatusId
        )
    }

    fun addComment(comment: String): CommentAddedToTaskEvent {
        return CommentAddedToTaskEvent(
                taskId = this.taskId,
                comment = comment
        )
    }
}
