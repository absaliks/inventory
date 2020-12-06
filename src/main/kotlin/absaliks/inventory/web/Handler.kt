package absaliks.inventory.web

import absaliks.inventory.model.CreateJournalEntryRequest
import absaliks.inventory.service.JournalService
import absaliks.inventory.model.Inventory
import absaliks.inventory.model.InventoryState
import absaliks.inventory.model.JournalEntry
import absaliks.inventory.service.InventoryService
import absaliks.inventory.utils.typeReference
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono


@Component
class InventoryHandler(private val inventoryService: InventoryService) {
    fun getInventory(request: ServerRequest) =
        ServerResponse.ok()
            .contentType(APPLICATION_JSON)
            .body(inventoryService.getInventoryState(), InventoryState::class.java)
}


@Component
class JournalHandler(private val journalService: JournalService) {

    fun getJournal(request: ServerRequest) =
        ServerResponse.ok()
            .contentType(APPLICATION_JSON)
            .body(journalService.getJournal(), JournalEntry::class.java)

    fun createJournalEntry(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(typeReference<CreateJournalEntryRequest>())
            .map { journalService.create(it) }
            .flatMap { ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(it, JournalEntry::class.java) }
    }

    fun patchJournalEntry(request: ServerRequest): Mono<ServerResponse> {
        val id = request.pathVariable("id").toLong()
        return request.bodyToMono(typeReference<JsonNode>())
            .map { journalService.patch(id, it) }
            .flatMap { ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(it, Inventory::class.java) }
    }
}
