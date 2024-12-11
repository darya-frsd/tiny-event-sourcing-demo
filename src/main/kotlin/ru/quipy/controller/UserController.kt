import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.quipy.api.aggregates.UserAggregate
import ru.quipy.api.events.UserCreatedEvent
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.UserAggregateState
import ru.quipy.logic.create
import java.util.*

@RestController
@RequestMapping("/users")
class UserController(
        private val userEsService: EventSourcingService<UUID, UserAggregate, UserAggregateState>
) {
    @PostMapping("/create")
    fun createUser(
            @RequestParam nickname: String,
            @RequestParam password: String,
            @RequestParam fullName: String
    ): ResponseEntity<UserCreatedEvent> {
        val event = userEsService.create {
            it.create(UUID.randomUUID(), nickname, password, fullName)
        }
        return ResponseEntity.status(201).body(event)
    }
}