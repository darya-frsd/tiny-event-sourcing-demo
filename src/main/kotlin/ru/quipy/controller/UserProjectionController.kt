package ru.quipy.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.quipy.projections.UserProjection
import java.util.*

@RestController
@RequestMapping("/user-projections")
class UserProjectionController(
        private val userProjection: UserProjection
) {
    @GetMapping("/{userId}")
    fun getUser(@PathVariable userId: UUID): UserProjection.UserData? {
        return userProjection.getUserById(userId)
    }
}
