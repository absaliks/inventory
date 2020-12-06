package absaliks.inventory.config

import absaliks.inventory.InventoryRepository
import absaliks.inventory.model.Inventory
import absaliks.inventory.JournalRepository
import absaliks.inventory.model.JournalEntry
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.r2dbc.core.DatabaseClient
import java.time.Instant
import java.time.temporal.ChronoUnit

@Configuration
class DBInitializer(
    private val journalRepository: JournalRepository,
    private val inventoryRepository: InventoryRepository,
    private val db: DatabaseClient
) {

    @Bean
    fun initEvents() = ApplicationRunner {
        val initSchema = db.sql {
            """ CREATE TABLE inventory (
                    id IDENTITY NOT NULL PRIMARY KEY,
                    brand VARCHAR(255) NOT NULL,
                    model VARCHAR(255) NOT NULL
                );
                
                CREATE TABLE journal (
                    id IDENTITY NOT NULL PRIMARY KEY,
                    item_id VARCHAR(255) NOT NULL,
                    user VARCHAR(255) NOT NULL,
                    lend_date TIMESTAMP NOT NULL,
                    return_date TIMESTAMP
                );
            """
        }

        val populateInventory = inventoryRepository.saveAll(
            mapOf(
                "Samsung" to "Galaxy S9",
                "Samsung" to "Galaxy S8",
                "Samsung" to "Galaxy S7",
                "Motorola" to "Nexus 6",
                "LG" to "Nexus 5X",
                "Huawei" to "Honor 7X",
                "Apple" to "iPhone X",
                "Apple" to "iPhone 8",
                "Apple" to "iPhone 4s",
                "Nokia" to "3310",
            ).map { Inventory(null, it.key, it.value) }
        )

        val today = Instant.now()
        val yesterday = today.minus(1, ChronoUnit.DAYS)
        val dayBeforeYesterday = today.minus(2, ChronoUnit.DAYS)
        val populateJournal = journalRepository.saveAll(
            listOf(
                JournalEntry(null, 1, "John Doe", dayBeforeYesterday, yesterday),
                JournalEntry(null, 1, "John Doe", yesterday, today),
                JournalEntry(null, 1, "Jane Doe", today, null),
                JournalEntry(null, 2, "Shamil", today, null),
            )
        )

        initSchema.then()
            .thenMany(populateInventory)
            .thenMany(populateJournal)
            .subscribe()
    }
}