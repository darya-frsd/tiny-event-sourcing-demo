package ru.quipy.api.events

import ru.quipy.api.aggregates.ProjectAggregate
import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import java.util.*

const val PROJECT_CREATED_EVENT = "PROJECT_CREATED_EVENT"
const val USER_ADDED_TO_PROJECT_EVENT = "USER_ADDED_TO_PROJECT_EVENT"
const val TASK_ADDED_TO_PROJECT_EVENT = "TASK_ADDED_TO_PROJECT_EVENT"
const val TASK_REMOVED_FROM_PROJECT_EVENT = "TASK_REMOVED_FROM_PROJECT_EVENT"
const val STATUS_CREATED_EVENT = "STATUS_CREATED_EVENT"

// API
@DomainEvent(name = PROJECT_CREATED_EVENT)
class ProjectCreatedEvent(
    val projectId: UUID,
    val title: String,
    val creatorId: UUID,
    createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
    name = PROJECT_CREATED_EVENT,
    createdAt = createdAt,
)

@DomainEvent(name = USER_ADDED_TO_PROJECT_EVENT)
class UserAddedToProjectEvent(
    val projectId: UUID,
    val userId: UUID,
    createdAt: Long = System.currentTimeMillis()
) : Event<ProjectAggregate> (
    name = USER_ADDED_TO_PROJECT_EVENT,
    createdAt = createdAt
)


@DomainEvent(name = STATUS_CREATED_EVENT)
class StatusCreatedEvent(
    val statusId: UUID,
    val projectId: UUID,
    val statusName: String,
    val statusColor: String,
    createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
    name = STATUS_CREATED_EVENT,
    createdAt = createdAt,
)

@DomainEvent(name = TASK_ADDED_TO_PROJECT_EVENT)
class TaskAddedToProjectEvent(
        val projectId: UUID,
        val taskId: UUID,
        createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
        name = TASK_ADDED_TO_PROJECT_EVENT,
        createdAt = createdAt,
)

@DomainEvent(name = TASK_REMOVED_FROM_PROJECT_EVENT)
class TaskRemovedFromProjectEvent(
        val projectId: UUID,
        val taskId: UUID,
        createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
        name = TASK_REMOVED_FROM_PROJECT_EVENT,
        createdAt = createdAt,
)