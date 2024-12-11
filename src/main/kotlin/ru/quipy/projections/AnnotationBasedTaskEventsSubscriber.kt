package ru.quipy.projections

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.quipy.api.aggregates.TaskAggregate
import ru.quipy.api.events.*
import ru.quipy.streams.annotation.AggregateSubscriber
import ru.quipy.streams.annotation.SubscribeEvent

@Service
@AggregateSubscriber(
    aggregateClass = TaskAggregate::class, subscriberName = "why am i even alive"
)
class AnnotationBasedTaskEventsSubscriber {
    val logger: Logger = LoggerFactory.getLogger(AnnotationBasedTaskEventsSubscriber::class.java)

    @SubscribeEvent
    fun taskCreatedSubscriber(event: TaskCreatedEvent) {
        logger.info("Task created: {}", event.title)
    }


    @SubscribeEvent
    fun taskRenamedSubscriber(event: TaskRenamedEvent) {
        logger.info("Task {} renamed to {}", event.taskId, event.newTitle)
    }

    @SubscribeEvent
    fun taskAssignedSubscriber(event: TaskAssignedToUserEvent) {
        logger.info("Task {} assigned to user {}", event.taskId, event.userId)
    }

    @SubscribeEvent
    fun taskStatusUpdatedSubscriber(event: TaskStatusUpdatedEvent) {
        logger.info("Task {} status is now {}", event.taskId, event.newStatusId)
    }
}