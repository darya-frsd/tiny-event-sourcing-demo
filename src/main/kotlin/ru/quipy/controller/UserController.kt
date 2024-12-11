import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.quipy.api.*
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.*
import java.util.*

@RestController
@RequestMapping("/users")
class UserController(
    val userEsService: EventSourcingService<UUID, UserAggregate, UserAggregateState>
) {
    @PostMapping("/create")
    fun create(
        @RequestParam(required = true, value = "nickname") nickname: String,
        @RequestParam(required = true, value = "password") password: String,
        @RequestParam(required = true, value = "fullName") fullName: String) : UserCreatedEvent {
        return userEsService.create {
            it.create(UUID.randomUUID(), nickname, password, fullName)
        }
    }
}
