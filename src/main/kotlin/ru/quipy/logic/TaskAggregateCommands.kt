package ru.quipy.logic

import ru.quipy.api.events.*
import java.util.UUID

fun TaskAggregateState.create(projectId: UUID, title: String, description: String, statusId: UUID) : TaskCreatedEvent {
    if (isTaskIdInitialized()) {
        throw IllegalStateException("Task already exists.")
    }

    return TaskCreatedEvent(
        taskId = UUID.randomUUID(),
        projectId =  projectId,
        title = title,
        description = description,
        statusId = statusId
    )
}

fun TaskAggregateState.rename(newTitle: String) : TaskRenamedEvent {
    return TaskRenamedEvent(
        taskId = this.taskId,
        newTitle = newTitle
    )
}

fun TaskAggregateState.assignToUser(userId: UUID) : TaskAssignedToUserEvent {
    return TaskAssignedToUserEvent(
        taskId = this.taskId,
        userId = userId
    )
}

fun TaskAggregateState.updateStatus(statusId: UUID) : TaskStatusUpdatedEvent {
    return TaskStatusUpdatedEvent(
        taskId = this.taskId,
        newStatusId = statusId
    )
}