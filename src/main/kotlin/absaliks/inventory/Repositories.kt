package absaliks.inventory

import absaliks.inventory.model.Inventory
import absaliks.inventory.model.InventoryState
import absaliks.inventory.model.JournalEntry
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Instant

interface InventoryRepository : ReactiveCrudRepository<Inventory, Long> {

    @Query("""
        SELECT TOP 1 WITH TIES i.*, j.user, j.lend_date, j.return_date
        FROM inventory i
          LEFT JOIN journal j ON j.item_id = i.id
        ORDER BY ROW_NUMBER() OVER (PARTITION BY i.id ORDER BY j.id DESC)
    """)
    fun getInventoryState(): Flux<InventoryState>
}

interface JournalRepository : ReactiveCrudRepository<JournalEntry, Long> {

    @Query("""
       SELECT * FROM journal
       WHERE item_id = $1
       ORDER BY id desc 
       LIMIT 1; 
    """)
    fun findLastByItemId(itemId: Long): Mono<JournalEntry>
}