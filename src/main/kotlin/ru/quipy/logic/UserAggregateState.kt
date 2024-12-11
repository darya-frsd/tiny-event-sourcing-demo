package ru.quipy.logic

import ru.quipy.api.aggregates.UserAggregate
import ru.quipy.api.events.UserCreatedEvent
import ru.quipy.core.annotations.StateTransitionFunc
import ru.quipy.domain.AggregateState
import java.util.*

class UserAggregateState : AggregateState<UUID, UserAggregate> {
    private lateinit var userId: UUID
    lateinit var nickname: String
    lateinit var password: String
    lateinit var fullName: String
    var createdAt: Long = System.currentTimeMillis()

    override fun getId(): UUID = userId

    @StateTransitionFunc
    fun userCreatedApply(event: UserCreatedEvent) {
        userId = event.userId
        nickname = event.nickname
        password = event.password
        fullName = event.fullName
        createdAt = event.createdAt
    }

    // Метод для проверки, инициализирован ли userId
    fun isUserIdInitialized(): Boolean = this::userId.isInitialized
}