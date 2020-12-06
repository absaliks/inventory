package absaliks.inventory.web

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.router

@Configuration
class Router(
    private val inventoryHandler: InventoryHandler,
    private val journalHandler: JournalHandler
) {

    @Bean
    fun inventoryRoutes() = router {
        accept(MediaType.APPLICATION_JSON).nest {
            "/inventory".nest {
                GET("", inventoryHandler::getInventory)
            }
        }
    }

    @Bean
    fun journalRoutes() = router {
        accept(MediaType.APPLICATION_JSON).nest {
            "/journal".nest {
                GET("", journalHandler::getJournal)
                POST("", journalHandler::createJournalEntry)
                PATCH("/{id}", journalHandler::patchJournalEntry)
            }
        }
    }
}