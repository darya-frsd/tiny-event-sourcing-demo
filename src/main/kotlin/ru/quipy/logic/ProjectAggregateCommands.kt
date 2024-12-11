package ru.quipy.logic

import ru.quipy.api.events.ProjectCreatedEvent
import ru.quipy.api.events.TaskCreatedEvent
import ru.quipy.api.events.UserAddedToProjectEvent
import java.util.*
fun ProjectAggregateState.create(id: UUID, title: String, creatorId: UUID): ProjectCreatedEvent {
    if (isProjectIdInitialized()) {
        throw IllegalStateException("Project already exists.")
    }
    return ProjectCreatedEvent(
            projectId = id,
            title = title,
            creatorId = creatorId
    )
}

fun ProjectAggregateState.addUser(userId: UUID): UserAddedToProjectEvent {
    if (userId in projectUsers) {
        throw IllegalArgumentException("User $userId is already in the project.")
    }
    return UserAddedToProjectEvent(
            projectId = this.projectId,
            userId = userId
    )
}
fun ProjectAggregateState.createTask(
        projectId: UUID,
        title: String,
        description: String,
        statusId: UUID
): TaskCreatedEvent {
    if (!projectStatuses.containsKey(statusId)) {
        throw IllegalArgumentException("Invalid statusId: $statusId. Status does not exist in the project.")
    }
    return TaskCreatedEvent(
            taskId = UUID.randomUUID(),
            projectId = projectId,
            title = title,
            description = description,
            statusId = statusId
    )
}