package com.project.warehousemanagement.persistence.dto;

import com.project.warehousemanagement.constants.OrderStatus;
import com.project.warehousemanagement.persistence.entity.Order;
import com.project.warehousemanagement.persistence.entity.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderDto {
    private Long id;
    private String orderNumber;
    private LocalDateTime submittedDate;
    private OrderStatus status;

    @NotEmpty(message = "At least one item required")
    private List<@Valid OrderItemDto> items;

    @FutureOrPresent(message = "Deadline cannot be in the past")
    private LocalDate deadlineDate;


    public static OrderDto fromEntity(Order order, boolean withItems) {
        OrderDto orderDto = new OrderDto();
        orderDto.id = order.getId();
        orderDto.orderNumber = order.getOrderNumber();
        orderDto.submittedDate = order.getSubmittedDate();
        orderDto.status = order.getStatus();
        if (withItems) {
            orderDto.items = order.getItems().stream().map(OrderItemDto::fromEntity).toList();
            orderDto.deadlineDate = order.getDeadlineDate();
        }
        return orderDto;
    }


    public Order toEntity(User client) {
        Order order = new Order();
        order.setClient(client);
        order.setItems(items.stream().map(OrderItemDto::toEntity).toList());
        order.setDeadlineDate(deadlineDate);
        order.setSubmittedDate(LocalDateTime.now());
        order.setStatus(OrderStatus.CREATED);
        order.setOrderNumber(UUID.randomUUID().toString());
        return order;
    }

    public void copyTo(Order order) {
        if (items != null)
            order.setItems(items.stream().map(OrderItemDto::toEntity).toList());
        if (deadlineDate != null)
            order.setDeadlineDate(deadlineDate);
    }
}
