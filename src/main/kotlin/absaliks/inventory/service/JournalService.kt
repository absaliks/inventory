package absaliks.inventory.service

import absaliks.inventory.JournalRepository
import absaliks.inventory.model.CreateJournalEntryRequest
import absaliks.inventory.model.JournalEntry
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.time.Instant

@Service
class JournalService(
    private val inventoryService: InventoryService,
    private val journalRepository: JournalRepository,
) {

    fun getJournal(): Flux<JournalEntry> = journalRepository.findAll()

    fun create(request: CreateJournalEntryRequest): Mono<JournalEntry> {
        return validateCreateEntryRequest(request)
            .map { JournalEntry(null, request.itemId, request.user, Instant.now(), null) }
            .flatMap {journalRepository.save(it)}
    }

    private fun validateCreateEntryRequest(entry: CreateJournalEntryRequest): Mono<Void> {
        val itemId = entry.itemId
        if (itemId < 1 || entry.user.isBlank()) {
            return Mono.error(IllegalArgumentException("Missing itemId or user"))
        }
        return inventoryService.itemExists(itemId)
            .flatMap { exists ->
                if (exists) journalRepository.findLastByItemId(itemId) else
                    Mono.error(IllegalArgumentException("Item #$itemId not found"))
            }
            .flatMap {
                if (it.returnDate == null) Mono.error(IllegalStateException("Item is already taken"))
                else Mono.empty()
            }

    }


    fun patch(id: Long, patch: JsonNode): Mono<JournalEntry> {
        return journalRepository.findById(id)
            .switchIfEmpty { Mono.error(IllegalArgumentException("Journal entry #${id} not found")) }
            .map {
                if (it.returnDate != null) {
                    throw IllegalStateException("Phone is not taken")
                }
                val returnDateNode = patch.get("returnDate")
                if (returnDateNode != null) {
                    val returnDate = Instant.parse(returnDateNode.textValue())
                    it.copy(returnDate = returnDate)
                } else {
                    it
                }
            }
            .flatMap { journalRepository.save(it) }
    }

}