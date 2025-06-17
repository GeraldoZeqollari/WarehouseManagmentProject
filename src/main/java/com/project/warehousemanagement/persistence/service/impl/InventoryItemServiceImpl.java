package com.project.warehousemanagement.persistence.service.impl;

import com.project.warehousemanagement.dto.CreateInventoryItemRequest;
import com.project.warehousemanagement.dto.UpdateInventoryItemRequest;
import com.project.warehousemanagement.persistence.dto.InventoryItemDto;
import com.project.warehousemanagement.persistence.entity.InventoryItem;
import com.project.warehousemanagement.persistence.repository.InventoryItemRepository;
import com.project.warehousemanagement.persistence.repository.OrderItemRepository;
import com.project.warehousemanagement.persistence.service.InventoryItemService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class InventoryItemServiceImpl implements InventoryItemService {

    private final InventoryItemRepository itemsRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public InventoryItemDto createItem(CreateInventoryItemRequest request) {
        log.info("Create inventory item");
        if (itemsRepository.existsByName(request.getName()))
            throw new IllegalArgumentException("Item name already exists");

        InventoryItem inventoryItem = new InventoryItem();
        inventoryItem.setName(request.getName());
        inventoryItem.setQuantity(request.getQuantity());
        inventoryItem.setUnitPrice(request.getUnitPrice());
        inventoryItem.setPackageVolume(request.getPackageVolume());

        log.info("Inventory item created");
        return InventoryItemDto.fromEntity(itemsRepository.save(inventoryItem));
    }

    @Override
    public InventoryItemDto updateItem(Long id, UpdateInventoryItemRequest request) {
        log.info("Update inventory item");
        InventoryItem inventoryItem = itemsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));

        if (itemsRepository.existsByNameAndIdNot(request.getName(), id))
            throw new IllegalArgumentException("Item name already exists");

        inventoryItem.setName(request.getName());
        inventoryItem.setQuantity(request.getQuantity());
        inventoryItem.setUnitPrice(request.getUnitPrice());
        inventoryItem.setPackageVolume(request.getPackageVolume());
        log.info("Inventory item updated");
        return InventoryItemDto.fromEntity(inventoryItem);
    }

    @Override
    public void deleteItem(Long id) {
        log.info("Delete inventory item");
        if (orderItemRepository.existsByItemId(id)) {
            throw new IllegalStateException(
                    "Item is referenced by existing orders and cannot be deleted");
        }
        log.info("Delete order item");
        itemsRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryItemDto> listItems() {
        log.info("List all items");
        return itemsRepository.findAll().stream().map(InventoryItemDto::fromEntity).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryItemDto detailItem(Long id) {
        log.info("Get inventory item");
        return InventoryItemDto.fromEntity(itemsRepository.findById(id).orElseThrow());
    }
}

