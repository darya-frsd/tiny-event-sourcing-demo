package ru.quipy.logic

import ru.quipy.api.*
import ru.quipy.core.annotations.StateTransitionFunc
import ru.quipy.domain.AggregateState
import java.util.*

class UserAggregateState : AggregateState<UUID, UserAggregate>
{
    private lateinit var userId: UUID
    var createdAt: Long = System.currentTimeMillis()
    var updatedAt: Long = System.currentTimeMillis()

    lateinit var nickname: String
    lateinit var password: String
    lateinit var fullName : String

    override fun getId() = userId

    @StateTransitionFunc
    fun userCreatedApply(event: UserCreatedEvent) {
        userId = event.userId
        nickname = event.nickname
        password = event.password
        fullName = event.fullName
        updatedAt = event.createdAt
    }

//    @StateTransitionFunc
//    fun userAuthenticatedApply(event: UserAuthenticatedEvent) {
//
//        updatedAt = createdAt
//    }
}