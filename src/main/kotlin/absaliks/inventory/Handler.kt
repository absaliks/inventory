package absaliks.inventory

import absaliks.inventory.utils.typeReference
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class Handler(private val service: JournalService) {
    fun getInventory(request: ServerRequest) =
        ServerResponse.ok()
            .contentType(APPLICATION_JSON)
            .body(service.getInventory(), Phone::class.java)

    fun patchInventory(request: ServerRequest): Mono<ServerResponse> {
        val id = request.pathVariable("id").toLong()
        return request.bodyToMono(typeReference<JsonNode>())
            .map { service.patch(id, it) }
            .flatMap { ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(it, Phone::class.java) }
    }
}
