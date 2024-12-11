package ru.quipy.api.events

import ru.quipy.api.aggregates.ProjectAggregate
import ru.quipy.api.aggregates.TaskAggregate
import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import java.util.*

const val TASK_CREATED_EVENT = "TASK_CREATED_EVENT"
const val TASK_DELETED_EVENT = "TASK_DELETED_EVENT"
const val TASK_RENAMED_EVENT = "TASK_RENAMED_EVENT"
const val TASK_ASSIGNED_TO_USER_EVENT = "TASK_ASSIGNED_TO_USER_EVENT"
const val TASK_STATUS_UPDATED_EVENT = "TASK_STATUS_UPDATED_EVENT"

// Событие создания задачи
@DomainEvent(name = TASK_CREATED_EVENT)
class TaskCreatedEvent(
        val taskId: UUID,
        val projectId: UUID,
        val title: String,
        val description: String,
        val statusId: UUID,
        createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
        name = TASK_CREATED_EVENT,
        createdAt = createdAt,
)

// Событие удаления задачи
@DomainEvent(name = TASK_DELETED_EVENT)
class TaskDeletedEvent(
        val taskId: UUID,
        val projectId: UUID,
        createdAt: Long = System.currentTimeMillis(),
) : Event<TaskAggregate>(
        name = TASK_DELETED_EVENT,
        createdAt = createdAt,
)

// Событие переименования задачи
@DomainEvent(name = TASK_RENAMED_EVENT)
class TaskRenamedEvent(
        val taskId: UUID,
        val newTitle: String,
        createdAt: Long = System.currentTimeMillis(),
) : Event<TaskAggregate>(
        name = TASK_RENAMED_EVENT,
        createdAt = createdAt,
)

// Событие назначения пользователя на задачу
@DomainEvent(name = TASK_ASSIGNED_TO_USER_EVENT)
class TaskAssignedToUserEvent(
        val taskId: UUID,
        val userId: UUID,
        createdAt: Long = System.currentTimeMillis(),
) : Event<TaskAggregate>(
        name = TASK_ASSIGNED_TO_USER_EVENT,
        createdAt = createdAt,
)

// Событие обновления статуса задачи
@DomainEvent(name = TASK_STATUS_UPDATED_EVENT)
class TaskStatusUpdatedEvent(
        val taskId: UUID,
        val newStatusId: UUID,
        createdAt: Long = System.currentTimeMillis(),
) : Event<TaskAggregate>(
        name = TASK_STATUS_UPDATED_EVENT,
        createdAt = createdAt,
)