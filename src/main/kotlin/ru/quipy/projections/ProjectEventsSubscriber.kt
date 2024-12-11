package ru.quipy.projections
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.quipy.api.aggregates.ProjectAggregate
import ru.quipy.api.events.ProjectCreatedEvent
import ru.quipy.api.events.TaskCreatedEvent
import ru.quipy.api.events.UserAddedToProjectEvent
import ru.quipy.streams.AggregateSubscriptionsManager
import javax.annotation.PostConstruct

@Service
class ProjectEventsSubscriber {

    private val logger = LoggerFactory.getLogger(ProjectEventsSubscriber::class.java)

    @Autowired
    lateinit var subscriptionsManager: AggregateSubscriptionsManager

    @PostConstruct
    fun init() {
        subscriptionsManager.createSubscriber(ProjectAggregate::class, "project-events-subscriber") {
            `when`(ProjectCreatedEvent::class) { event ->
                logger.info("Project created: ${event.title}")
            }
            `when`(UserAddedToProjectEvent::class) { event ->
                logger.info("User ${event.userId} added to project ${event.projectId}")
            }
            `when`(TaskCreatedEvent::class) { event ->
                logger.info("Task created: ${event.title}")
            }
        }
    }
}
