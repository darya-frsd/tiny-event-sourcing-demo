package ru.quipy.projections

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.quipy.api.*
import ru.quipy.streams.AggregateSubscriptionsManager
import javax.annotation.PostConstruct

@Service
class ProjectEventsSubscriber {

    val logger: Logger = LoggerFactory.getLogger(ProjectEventsSubscriber::class.java)

    @Autowired
    lateinit var subscriptionsManager: AggregateSubscriptionsManager

    @PostConstruct
    fun init() {
        subscriptionsManager.createSubscriber(ProjectAggregate::class, "some-meaningful-name") {

            `when`(ProjectCreatedEvent::class) { event ->
                logger.info("Project created: {}", event.title)
            }

            `when`(UserAddedToProjectEvent::class) { event ->
                logger.info("User {} added to project {}: ", event.userId, event.projectId)
            }

            `when`(TaskCreatedEvent::class) { event ->
                logger.info("Task created: {}", event.title)
            }

            `when`(StatusCreatedEvent::class) { event ->
                logger.info("Status created: {}", event.statusName)
            }

            // тут можно соответственно побольше подписаться
        }
    }
}