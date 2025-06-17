package com.project.warehousemanagement.persistence.dto;

import com.project.warehousemanagement.persistence.entity.InventoryItem;
import com.project.warehousemanagement.persistence.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {
    private Long itemId;
    private Integer quantity;

    public static OrderItemDto fromEntity(OrderItem orderItem) {
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.itemId = orderItem.getItem().getId();
        orderItemDto.quantity = orderItem.getRequestedQty();
        return orderItemDto;
    }

    public OrderItem toEntity() {
        OrderItem orderItem = new OrderItem();
        InventoryItem ref = new InventoryItem();
        ref.setId(itemId);
        orderItem.setItem(ref);
        orderItem.setRequestedQty(quantity);
        return orderItem;
    }
}
