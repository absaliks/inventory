package absaliks.inventory.phonespec

import org.mockserver.client.MockServerClient
import org.mockserver.integration.ClientAndServer
import org.mockserver.model.HttpRequest.request
import org.mockserver.model.HttpResponse.response
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener


@Configuration
class PhoneSpecServiceMock {

    private lateinit var mockServer: ClientAndServer

    @EventListener(ApplicationReadyEvent::class)
    fun doSomethingAfterStartup() {
        mockServer = ClientAndServer.startClientAndServer(8081)
        initServer()
    }

    fun initServer() {
        MockServerClient("localhost", 8081)
            .`when`(
                request()
                    .withMethod("GET")
                    .withPath("/phoneSpec")
            )
            .respond(
                response()
                    .withStatusCode(200)
                    .withBody("""{
                        |"technology": "Edge",
                        |"2G": "850, 900, 1800, 1900"
                        |"3G": "2100, 1900, 2100, 850"
                        |}""".trimMargin())

            )
    }
}