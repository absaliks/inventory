package absaliks.inventory

import absaliks.inventory.phonespec.PhoneSpecService
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.time.Instant

@Service
class JournalService(
    private val repository: InventoryRepository,
    private val phoneSpecService: PhoneSpecService
) {

    fun getInventory() = repository.findAll()
        .flatMap {
            phone -> phoneSpecService.fetchPhoneSpecs(phone.phoneName)
                .map { spec -> phone.copy(phoneSpecs = spec) }
        }

    fun patch(id: Long, patch: JsonNode): Mono<Phone> {
        val userNode = patch.get("user")
            ?: throw IllegalArgumentException("'user' field is required")

        val user = userNode.textValue()
        return if (user == null) returnPhone(id) else lend(id, user)
    }

    private fun lend(id: Long, user: String): Mono<Phone> {
        return repository.findById(id)
            .switchIfEmpty { Mono.error(IllegalStateException("No such phone in DB")) }
            .flatMap {
                if (it.user != null) {
                    Mono.error(IllegalStateException("Phone is already taken"))
                } else {
                    repository.save(it.copy(user = user, lendDate = Instant.now()))
                }
            }
    }

    private fun returnPhone(id: Long): Mono<Phone> {
        return repository.findById(id)
            .switchIfEmpty {Mono.error(IllegalStateException("Phone is not taken"))}
            .flatMap { repository.save(it.copy(user = null, lendDate = null)) }
    }

}