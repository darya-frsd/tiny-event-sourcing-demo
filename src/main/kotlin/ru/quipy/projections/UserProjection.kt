package ru.quipy.projections

import org.springframework.stereotype.Service
import ru.quipy.api.aggregates.UserAggregate
import ru.quipy.api.events.UserCreatedEvent
import ru.quipy.streams.annotation.AggregateSubscriber
import ru.quipy.streams.annotation.SubscribeEvent
import java.util.*

@Service
@AggregateSubscriber(aggregateClass = UserAggregate::class, subscriberName = "user-projection")
class UserProjection {

    private val userStore = mutableMapOf<UUID, UserData>()

    @SubscribeEvent
    fun onUserCreated(event: UserCreatedEvent) {
        if (userStore.containsKey(event.userId)) {
            throw IllegalStateException("User with ID ${event.userId} already exists.")
        }
        userStore[event.userId] = UserData(
                id = event.userId,
                fullName = event.fullName,
                nickname = event.nickname,
                password = event.password, // Сохраняем пароль напрямую
                status = "active"
        )
    }

    fun getUserById(userId: UUID): UserData? = userStore[userId]

    data class UserData(
            val id: UUID,
            val fullName: String,
            val nickname: String,
            val password: String, // Пароль в открытом виде
            var status: String
    )
}