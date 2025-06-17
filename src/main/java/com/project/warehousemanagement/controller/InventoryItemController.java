package com.project.warehousemanagement.controller;

import com.project.warehousemanagement.dto.CreateInventoryItemRequest;
import com.project.warehousemanagement.dto.UpdateInventoryItemRequest;
import com.project.warehousemanagement.persistence.dto.InventoryItemDto;
import com.project.warehousemanagement.persistence.service.InventoryItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class InventoryItemController {

    private final InventoryItemService itemsService;


    @PostMapping("/manager/create")
    @PreAuthorize("hasAuthority('WAREHOUSE_MANAGER')")
    public ResponseEntity<InventoryItemDto> create(
            @Valid @RequestBody CreateInventoryItemRequest req) {

        InventoryItemDto body = itemsService.createItem(req);
        URI location = URI.create("/api/manager/items" + body.getId());
        return ResponseEntity.created(location).body(body);
    }

    @PutMapping("/manager/update/{id}")
    @PreAuthorize("hasAuthority('WAREHOUSE_MANAGER')")
    public ResponseEntity<InventoryItemDto> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateInventoryItemRequest req) {

        return ResponseEntity.ok(itemsService.updateItem(id, req));
    }


    @DeleteMapping("/manager/delete/{id}")
    @PreAuthorize("hasAuthority('WAREHOUSE_MANAGER')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        itemsService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('CLIENT','WAREHOUSE_MANAGER')")
    public ResponseEntity<List<InventoryItemDto>> list() {
        return ResponseEntity.ok(itemsService.listItems());
    }

    @GetMapping("/detail/{id}")
    @PreAuthorize("hasAnyAuthority('CLIENT','WAREHOUSE_MANAGER')")
    public ResponseEntity<InventoryItemDto> detail(@PathVariable Long id) {
        return ResponseEntity.ok(itemsService.detailItem(id));
    }
}