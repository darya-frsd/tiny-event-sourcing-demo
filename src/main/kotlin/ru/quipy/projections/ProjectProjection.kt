package ru.quipy.projections

import org.springframework.stereotype.Service
import ru.quipy.api.aggregates.ProjectAggregate
import ru.quipy.api.events.ProjectCreatedEvent
import ru.quipy.api.events.TaskCreatedEvent
import ru.quipy.api.events.UserAddedToProjectEvent
import ru.quipy.streams.annotation.AggregateSubscriber
import ru.quipy.streams.annotation.SubscribeEvent
import java.util.*
@Service
@AggregateSubscriber(aggregateClass = ProjectAggregate::class, subscriberName = "project-projection")
class ProjectProjection {

    private val projectStore = mutableMapOf<UUID, ProjectData>()

    @SubscribeEvent
    fun onProjectCreated(event: ProjectCreatedEvent) {
        projectStore[event.projectId] = ProjectData(
                id = event.projectId,
                name = event.title,
                ownerId = event.creatorId,
                participants = mutableSetOf(event.creatorId),
                tasks = mutableSetOf()
        )
    }

    @SubscribeEvent
    fun onUserAdded(event: UserAddedToProjectEvent) {
        projectStore[event.projectId]?.participants?.add(event.userId)
    }

    @SubscribeEvent
    fun onTaskCreated(event: TaskCreatedEvent) {
        projectStore[event.projectId]?.tasks?.add(event.taskId)
    }

    fun getProjectById(projectId: UUID): ProjectData? = projectStore[projectId]

    data class ProjectData(
            val id: UUID,
            val name: String,
            val ownerId: UUID,
            val participants: MutableSet<UUID>,
            val tasks: MutableSet<UUID>
    )
}