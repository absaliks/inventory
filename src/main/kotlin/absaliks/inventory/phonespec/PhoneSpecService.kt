package absaliks.inventory.phonespec

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono


@Service
class PhoneSpecService(webClientBuilder: WebClient.Builder) {

    private val webClient: WebClient = webClientBuilder.baseUrl("http://localhost").build()

    fun fetchPhoneSpecs(phoneName: String): Mono<PhoneSpecs> {
        return webClient.get()
            .uri { it.queryParam("phone", phoneName).build() }
            .retrieve()
            .bodyToMono(PhoneSpecs::class.java)
    }
}