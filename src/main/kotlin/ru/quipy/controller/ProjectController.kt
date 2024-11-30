package ru.quipy.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.quipy.api.ProjectAggregate
import ru.quipy.api.ProjectCreatedEvent
import ru.quipy.api.TaskCreatedEvent
import ru.quipy.api.UserAddedToProjectEvent
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.ProjectAggregateState
import ru.quipy.logic.addUser
import ru.quipy.logic.create
import ru.quipy.logic.createTask
import java.util.*

@RestController
@RequestMapping("/projects")
class ProjectController(
    val projectEsService: EventSourcingService<UUID, ProjectAggregate, ProjectAggregateState>
) {

    @PostMapping("/{projectTitle}")
    fun createProject(@PathVariable projectTitle: String, @RequestParam creatorId: String) : ProjectCreatedEvent {
        return projectEsService.create { it.create(UUID.randomUUID(), projectTitle, UUID.fromString(creatorId)) }
    }

    @GetMapping("/{projectId}")
    fun getAccount(@PathVariable projectId: UUID) : ProjectAggregateState? {
        return projectEsService.getState(projectId)
    }

    @PostMapping("/{projectId}/users/{userId}")
    fun addUser(@PathVariable projectId: UUID, @PathVariable userId: UUID) : UserAddedToProjectEvent {
        return projectEsService.update(projectId) {
            it.addUser(userId, projectId)
        }
    }

    @PostMapping("/{projectId}/statuses/{statusId}/tasks/{title}")
    fun createTask(@PathVariable projectId: UUID, @PathVariable statusId: UUID, @PathVariable title: String, @RequestParam description: String) : TaskCreatedEvent {
        return projectEsService.update(projectId) {
            it.createTask(projectId, title, description, statusId)
        }
    }
}