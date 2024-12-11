package ru.quipy.logic

import ru.quipy.api.events.UserCreatedEvent
import java.util.*

fun UserAggregateState.create(userId: UUID, nickname: String, password: String, fullName: String): UserCreatedEvent {
    if (isUserIdInitialized()) {
        throw IllegalStateException("User with ID $userId already exists.")
    }
    return UserCreatedEvent(
            userId = userId,
            nickname = nickname,
            password = password, // Передаем пароль напрямую
            fullName = fullName
    )
}