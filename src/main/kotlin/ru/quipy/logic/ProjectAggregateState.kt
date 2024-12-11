
package ru.quipy.logic

import ru.quipy.api.aggregates.ProjectAggregate
import ru.quipy.api.events.*
import ru.quipy.core.annotations.StateTransitionFunc
import ru.quipy.domain.AggregateState
import java.util.*

class ProjectAggregateState : AggregateState<UUID, ProjectAggregate> {

    lateinit var projectId: UUID
    lateinit var projectTitle: String
    val projectUsers = mutableSetOf<UUID>()
    val projectTasks = mutableMapOf<UUID, TaskEntity>()
    val projectStatuses: MutableMap<UUID, String> = mutableMapOf()

    override fun getId(): UUID = projectId
    @StateTransitionFunc
    fun projectCreatedApply(event: ProjectCreatedEvent) {
        projectId = event.projectId
        projectTitle = event.title
        projectUsers.add(event.creatorId)
    }

    @StateTransitionFunc
    fun userAddedToProjectApply(event: UserAddedToProjectEvent) {
        projectUsers.add(event.userId)
    }

    @StateTransitionFunc
    fun taskCreatedApply(event: TaskCreatedEvent) {
        projectTasks[event.taskId] = TaskEntity(event.taskId, event.title, event.description, event.statusId)
    }

    data class TaskEntity(val id: UUID, val title: String, val description: String, val statusId: UUID)

    fun isProjectIdInitialized(): Boolean = this::projectId.isInitialized

    fun addTask(taskId: UUID, title: String, description: String, statusId: UUID): TaskAddedToProjectEvent {
        if (taskId in projectTasks) throw IllegalArgumentException("Task already exists in the project.")
        return TaskAddedToProjectEvent(
                projectId = this.projectId,
                taskId = taskId
        )
    }

    fun removeTask(taskId: UUID): TaskRemovedFromProjectEvent {
        if (taskId !in projectTasks) throw IllegalArgumentException("Task does not exist in the project.")
        return TaskRemovedFromProjectEvent(
                projectId = this.projectId,
                taskId = taskId
        )
    }

    @StateTransitionFunc
    fun taskAddedToProjectApply(event: TaskAddedToProjectEvent) {
        projectTasks[event.taskId] = TaskEntity(
                id = event.taskId,
                title = "Default title", // Заполните или измените, если данные передаются в событии
                description = "Default description", // Заполните или измените, если данные передаются в событии
                statusId = UUID.randomUUID() // Или возьмите статус из события, если он передаётся
        )
    }

}
