package ru.quipy.logic

import ru.quipy.api.aggregates.TaskAggregate
import ru.quipy.api.events.TaskAssignedToUserEvent
import ru.quipy.api.events.TaskCreatedEvent
import ru.quipy.api.events.TaskRenamedEvent
import ru.quipy.api.events.TaskStatusUpdatedEvent
import ru.quipy.core.annotations.StateTransitionFunc
import ru.quipy.domain.AggregateState
import java.util.*

class TaskAggregateState : AggregateState<UUID, TaskAggregate> {
    lateinit var taskId: UUID
    lateinit var projectId: UUID
    lateinit var title: String
    lateinit var description: String
    lateinit var statusId: UUID
    var assignedUserId: UUID? = null
    val comments: MutableList<String> = mutableListOf()

    fun isTaskIdInitialized(): Boolean = this::taskId.isInitialized

    override fun getId(): UUID = this.taskId

    @StateTransitionFunc
    fun taskCreatedApply(event: TaskCreatedEvent) {
        if (::taskId.isInitialized) throw IllegalStateException("Task already exists.")
        taskId = event.taskId
        projectId = event.projectId
        title = event.title
        description = event.description
        statusId = event.statusId
    }

    @StateTransitionFunc
    fun taskRenamedApply(event: TaskRenamedEvent){
        title = event.newTitle
    }

    @StateTransitionFunc
    fun userAssignedApply(event: TaskAssignedToUserEvent) {
        assignedUserId = event.userId
    }

    @StateTransitionFunc
    fun statusUpdatedApply(event: TaskStatusUpdatedEvent) {
        statusId = event.newStatusId
    }
}
