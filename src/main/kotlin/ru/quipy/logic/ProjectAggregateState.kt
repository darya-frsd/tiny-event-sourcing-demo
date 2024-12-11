package ru.quipy.logic

import ru.quipy.api.*
import ru.quipy.core.annotations.StateTransitionFunc
import ru.quipy.domain.AggregateState
import java.util.*
import kotlin.collections.ArrayList

// Service's business logic
class ProjectAggregateState : AggregateState<UUID, ProjectAggregate> {
    private lateinit var projectId: UUID
    var createdAt: Long = System.currentTimeMillis()
    var updatedAt: Long = System.currentTimeMillis()

    lateinit var projectTitle: String
    var projectStatuses = mutableMapOf<UUID, StatusEntity>()
    var projectUsers = ArrayList<UUID>() // сменили список инфо на список айдишников. мысли? иначе чето сложно

    var baseStatus: StatusEntity = StatusEntity()

    override fun getId() = projectId

    // State transition functions which is represented by the class member function
    @StateTransitionFunc
    fun projectCreatedApply(event: ProjectCreatedEvent) {
        projectId = event.projectId
        projectTitle = event.title
        projectUsers.add(event.creatorId)
        updatedAt = event.createdAt
        projectStatuses[baseStatus.id] = baseStatus
    }

    @StateTransitionFunc
    fun userAddedToProjectApply(event: UserAddedToProjectEvent) {
        projectUsers.add(event.userId)
        updatedAt = event.createdAt
    }

    @StateTransitionFunc
    fun taskCreatedApply(event: TaskCreatedEvent) {
        val newTask = TaskEntity(id = event.taskId, title = event.title, description = event.description)
        projectStatuses[event.statusId]?.tasksAssigned?.put(newTask.id, newTask)
            ?: throw IllegalArgumentException("No such status: ${event.statusId}")
        updatedAt = event.createdAt
    }

    @StateTransitionFunc
    fun taskRenamedApply(event: TaskRenamedEvent) {
        projectStatuses[event.statusId]?.tasksAssigned?.get(event.taskId)?.let {
            it.title = event.newTitle
        } ?: throw IllegalArgumentException("No such status: ${event.statusId} or no such task (${event.taskId} in that status)")
        updatedAt = event.createdAt
    }

    @StateTransitionFunc
    fun taskAssignedApply(event: TaskAssignedEvent) {
        projectStatuses[event.statusId]?.tasksAssigned?.get(event.taskId)?.let {
            if (event.userId !in it.usersAssigned) {
                it.usersAssigned.add(event.userId)
            }
        } ?: throw IllegalArgumentException("No such status: ${event.statusId} or no such task (${event.taskId} in that status)")
        updatedAt = event.createdAt
    }

    @StateTransitionFunc
    fun statusCreatedApply(event: StatusCreatedEvent) {
        val status = StatusEntity(
            id = event.statusId,
            name = event.statusName,
            color = event.statusColor
        )
        projectStatuses[status.id] = status
        updatedAt = event.createdAt
    }

    @StateTransitionFunc
    fun taskStatusChangedApply(event: TaskStatusChangedEvent) {
        var task = TaskEntity() // очень плохо.... как по-другому... помогитбе....
        projectStatuses[event.oldStatusId]?.tasksAssigned?.let {
            task = it[event.taskId]
                ?: throw IllegalArgumentException("no such task: ${event.taskId} in that status (${event.oldStatusId})")
        } ?: throw IllegalArgumentException("No such status: ${event.oldStatusId}")
        projectStatuses[event.newStatusId]?.tasksAssigned?.put(event.taskId, task)
            ?: throw IllegalArgumentException("No such status: ${event.newStatusId}")
        updatedAt = event.createdAt
    }

    @StateTransitionFunc
    fun statusDeletedApply(event: StatusDeletedEvent) {
        projectStatuses.remove(event.statusId) ?: throw IllegalArgumentException("No such status: ${event.statusId}")
        updatedAt = event.createdAt
    }

data class TaskEntity(
    val id: UUID = UUID.randomUUID(),
    var title: String = "",
    val description: String = "",
    val usersAssigned: MutableSet<UUID> = mutableSetOf()
)

data class StatusEntity(
    val id: UUID = UUID.randomUUID(),
    var tasksAssigned: MutableMap<UUID, TaskEntity> = mutableMapOf(),
    val name: String = "CREATED",
    val color: String = "blue"
)
}
