package absaliks.inventory

import absaliks.inventory.Phone
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface InventoryRepository : ReactiveCrudRepository<Phone, Long>