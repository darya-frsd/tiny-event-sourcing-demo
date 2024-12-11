package ru.quipy.projections

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.quipy.api.aggregates.UserAggregate
import ru.quipy.api.events.UserCreatedEvent
import ru.quipy.streams.AggregateSubscriptionsManager
import javax.annotation.PostConstruct

@Service
class UserEventsSubscriber {
    private val logger: Logger = LoggerFactory.getLogger(UserEventsSubscriber::class.java)

    @Autowired
    lateinit var subscriptionsManager: AggregateSubscriptionsManager

    @PostConstruct
    fun init() {
        subscriptionsManager.createSubscriber(UserAggregate::class, "user-events-subscriber") {
            `when`(UserCreatedEvent::class) { event ->
                handleUserCreated(event)
            }
            // В будущем можно добавить другие события
        }
    }

    private fun handleUserCreated(event: UserCreatedEvent) {
        logger.info("User created: {}, Full Name: {}", event.nickname, event.fullName)
        // Здесь можно добавить больше логики обработки события
    }
}