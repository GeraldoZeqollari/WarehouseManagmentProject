package com.project.warehousemanagement.persistence.dto;

import com.project.warehousemanagement.persistence.entity.InventoryItem;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
public class InventoryItemDto {

    private Long id;
    private String name;
    private Integer quantity;
    private BigDecimal unitPrice;
    private Double packageVolume;

    public static InventoryItemDto fromEntity(InventoryItem inventoryItem) {
        InventoryItemDto inventoryItemDto = new InventoryItemDto();
        inventoryItemDto.id = inventoryItem.getId();
        inventoryItemDto.name = inventoryItem.getName();
        inventoryItemDto.quantity = inventoryItem.getQuantity();
        inventoryItemDto.unitPrice = inventoryItem.getUnitPrice();
        inventoryItemDto.packageVolume = inventoryItem.getPackageVolume();
        return inventoryItemDto;
    }

    public InventoryItem toEntity() {
        InventoryItem inventoryItem = new InventoryItem();
        inventoryItem.setName(name);
        inventoryItem.setQuantity(quantity);
        inventoryItem.setUnitPrice(unitPrice);
        inventoryItem.setPackageVolume(packageVolume);
        return inventoryItem;
    }
}

