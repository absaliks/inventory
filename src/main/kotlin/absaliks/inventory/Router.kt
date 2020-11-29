package absaliks.inventory

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.router

@Configuration
class Router(private val handler: Handler) {

    @Bean
    fun eventRoutes() = router {
        accept(MediaType.APPLICATION_JSON).nest {
            "/inventory".nest {
                GET("", handler::getInventory)
                PATCH("/{id}", handler::patchInventory)
            }
        }
    }
}