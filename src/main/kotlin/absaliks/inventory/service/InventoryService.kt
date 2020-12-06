package absaliks.inventory.service

import absaliks.inventory.InventoryRepository
import org.springframework.stereotype.Service

@Service
class InventoryService(private val repository: InventoryRepository) {

    fun getInventory() = repository.findAll()

    fun getInventoryState() = repository.getInventoryState()

    fun itemExists(id: Long) = repository.existsById(id)

}