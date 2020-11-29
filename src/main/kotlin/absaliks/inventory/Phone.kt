package absaliks.inventory

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("inventory")
data class Phone(
    @Id val id: Long?,
    val phoneName: String,
    val user: String?,
    val lendDate: Instant?
)/* {
    fun isAvailable(): Boolean {
        return user == null
    }
}*/