package ru.quipy.logic

import ru.quipy.api.*
import java.util.*


// Commands : takes something -> returns event
// Here the commands are represented by extension functions, but also can be the class member functions

fun ProjectAggregateState.create(id: UUID, title: String, creatorId: UUID): ProjectCreatedEvent {
    return ProjectCreatedEvent(
        projectId = id,
        title = title,
        creatorId = creatorId,
    )
}

fun ProjectAggregateState.addUser(userId: UUID, projectId: UUID): UserAddedToProjectEvent {
    if (userId in projectUsers) throw IllegalArgumentException("User $userId already in project")
    return UserAddedToProjectEvent(
        projectId = projectId,
        userId = userId
    )
}

fun ProjectAggregateState.createTask(projectId: UUID, title: String, description: String, statusId: UUID): TaskCreatedEvent {
    return TaskCreatedEvent(
        UUID.randomUUID(), projectId, title, description, statusId
    )
}

fun ProjectAggregateState.renameTask(statusId: UUID, taskId: UUID, newTitle: String): TaskRenamedEvent {
    if (!projectStatuses.containsKey(statusId)) throw IllegalArgumentException("No such status: $statusId")
    if (!projectStatuses[statusId]!!.tasksAssigned.containsKey(taskId)) throw IllegalArgumentException("No task $taskId in status $statusId")
    return TaskRenamedEvent(
        statusId, taskId, newTitle
    )
}

fun ProjectAggregateState.assignTask(statusId: UUID, taskId: UUID, userId: UUID): TaskAssignedEvent {
    if (!projectStatuses.containsKey(statusId)) throw IllegalArgumentException("No such status: $statusId")
    if (!projectStatuses[statusId]!!.tasksAssigned.containsKey(taskId)) throw IllegalArgumentException("No task $taskId in status $statusId")
    return TaskAssignedEvent(
        statusId, taskId, userId
    )
}

fun ProjectAggregateState.createStatus(projectId: UUID, statusName: String, statusColor: String): StatusCreatedEvent {
    if (projectStatuses.values.any { it.name == statusName }) throw IllegalArgumentException("Status already exists: $statusName")
    return StatusCreatedEvent(
        UUID.randomUUID(), projectId, statusName, statusColor
    )
}

fun ProjectAggregateState.changeTaskStatus(projectId: UUID, taskId: UUID, oldStatusId: UUID, newStatusId: UUID): TaskStatusChangedEvent {
    if (!projectStatuses.containsKey(oldStatusId)) throw IllegalArgumentException("No such status: $oldStatusId")
    if (!projectStatuses.containsKey(newStatusId)) throw IllegalArgumentException("No such status: $newStatusId")
    if (!projectStatuses[oldStatusId]!!.tasksAssigned.containsKey(taskId)) throw IllegalArgumentException("No task $taskId in status $oldStatusId")
    return TaskStatusChangedEvent(projectId, taskId, oldStatusId, newStatusId)
}

fun ProjectAggregateState.deleteStatus(projectId: UUID, statusId: UUID): StatusDeletedEvent {
    if (!projectStatuses.containsKey(statusId)) throw IllegalArgumentException("No such status: $statusId")
    if (projectStatuses[statusId]!!.name == "CREATED") throw IllegalArgumentException("Cannot delete CREATED status")
    return StatusDeletedEvent(projectId, statusId)
}