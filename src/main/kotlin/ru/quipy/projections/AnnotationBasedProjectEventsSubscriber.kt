package ru.quipy.projections

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.quipy.api.aggregates.ProjectAggregate
import ru.quipy.api.events.*
import ru.quipy.streams.annotation.AggregateSubscriber
import ru.quipy.streams.annotation.SubscribeEvent

@Service
@AggregateSubscriber(
    aggregateClass = ProjectAggregate::class, subscriberName = "demo-subs-stream"
)
class AnnotationBasedProjectEventsSubscriber {

    val logger: Logger = LoggerFactory.getLogger(AnnotationBasedProjectEventsSubscriber::class.java)

    @SubscribeEvent
    fun projectCreatedSubscriber(event: ProjectCreatedEvent) {
        logger.info("Project created: {}", event.title)
    }

    @SubscribeEvent
    fun userAddedSubscriber(event: UserAddedToProjectEvent) {
        logger.info("User {} added to project {}", event.userId, event.projectId)
    }

    @SubscribeEvent
    fun statusCreatedSubscriber(event: StatusCreatedEvent) {
        logger.info("Status created: {}", event.statusName)
    }

    @SubscribeEvent
    fun taskAddedSubscriber(event: TaskAddedToProjectEvent) {
        logger.info("Task {} added to project {}", event.taskId, event.projectId)
    }

    @SubscribeEvent
    fun taskRemovedSubscriber(event: TaskRemovedFromProjectEvent) {
        logger.info("Task {} removed from project {}", event.taskId, event.projectId)
    }
}