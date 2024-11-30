package ru.quipy.logic

import ru.quipy.api.ProjectCreatedEvent
import ru.quipy.api.TaskCreatedEvent
import ru.quipy.api.UserCreatedEvent
import java.util.*

fun UserAggregateState.create(userId: UUID, nickname: String, password: String, fullName: String) : UserCreatedEvent {
    return UserCreatedEvent(
        userId = userId,
        nickname = nickname,
        password = password,
        fullName = fullName
    )
}