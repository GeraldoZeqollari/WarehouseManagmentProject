package com.project.warehousemanagement.persistence.service;

import com.project.warehousemanagement.dto.CreateInventoryItemRequest;
import com.project.warehousemanagement.dto.UpdateInventoryItemRequest;
import com.project.warehousemanagement.persistence.dto.InventoryItemDto;

import java.util.List;

public interface InventoryItemService {

    InventoryItemDto createItem(CreateInventoryItemRequest request);

    InventoryItemDto updateItem(Long id, UpdateInventoryItemRequest request);

    void deleteItem(Long id);

    List<InventoryItemDto> listItems();

    InventoryItemDto detailItem(Long id);

}
