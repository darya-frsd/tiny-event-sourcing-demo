package ru.quipy.controller

import org.springframework.web.bind.annotation.*
import ru.quipy.api.aggregates.ProjectAggregate
import ru.quipy.api.events.ProjectCreatedEvent
import ru.quipy.api.events.TaskCreatedEvent
import ru.quipy.api.events.UserAddedToProjectEvent
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.ProjectAggregateState
import ru.quipy.logic.addUser
import ru.quipy.logic.create
import ru.quipy.logic.createTask
import java.util.*
@RestController
@RequestMapping("/projects")
class ProjectController(
        private val projectEsService: EventSourcingService<UUID, ProjectAggregate, ProjectAggregateState>
) {

    @PostMapping
    fun createProject(@RequestParam title: String, @RequestParam creatorId: UUID): ProjectCreatedEvent {
        return projectEsService.create {
            it.create(UUID.randomUUID(), title, creatorId)
        }
    }

    @GetMapping("/{projectId}")
    fun getProject(@PathVariable projectId: UUID): ProjectAggregateState? {
        return projectEsService.getState(projectId)
    }

    @PostMapping("/{projectId}/users/{userId}")
    fun addUser(@PathVariable projectId: UUID, @PathVariable userId: UUID): UserAddedToProjectEvent {
        return projectEsService.update(projectId) { it.addUser(userId) }
    }

    @PostMapping("/{projectId}/tasks")
    fun createTask(
            @PathVariable projectId: UUID,
            @RequestParam title: String,
            @RequestParam description: String,
            @RequestParam statusId: UUID
    ): TaskCreatedEvent {
        return projectEsService.update(projectId) {
            val event = it.createTask(projectId, title, description, statusId)
            event
        }
    }

}