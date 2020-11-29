package absaliks.inventory.config

import absaliks.inventory.Phone
import absaliks.inventory.InventoryRepository
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.r2dbc.core.DatabaseClient
import reactor.core.publisher.Flux

@Configuration
class DBInitializer(
    private val repository: InventoryRepository,
    private val db: DatabaseClient
) {

    @Bean
    fun initEvents() = ApplicationRunner {
        val initSchema = db.sql {
            """ CREATE TABLE inventory (
                    id IDENTITY NOT NULL PRIMARY KEY,
                    phone_name VARCHAR(255) NOT NULL,
                    user VARCHAR(255),
                    lend_date TIMESTAMP
                );
            """
        }

        val populateInventory = repository.saveAll(
            Flux.just(
                "Samsung Galaxy S9",
                "Samsung Galaxy S8",
                "Samsung Galaxy S7",
                "Motorola Nexus 6",
                "LG Nexus 5X",
                "Huawei Honor 7X",
                "Apple iPhone X",
                "Apple iPhone 8",
                "Apple iPhone 4s",
                "Nokia 3310",
            ).map { Phone(null, it, null, null, null) }
        )

        initSchema.then()
            .thenMany(populateInventory)
            .subscribe()
    }
}