package absaliks.inventory.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("inventory")
data class Inventory(
    @Id val id: Long?,
    val brand: String,
    val model: String
)

@Table("journal")
data class JournalEntry(
    @Id val id: Long?,
    val itemId: Long,
    val user: String,
    val lendDate: Instant,
    val returnDate: Instant?
)



data class InventoryState(
    val id: Long?,
    val brand: String,
    val model: String,
    val user: String?,
    val lendDate: Instant?,
    val returnDate: Instant?
) {
    fun isAvailable() = user == null || returnDate != null
}

data class CreateJournalEntryRequest(
    val itemId: Long,
    val user: String,
)