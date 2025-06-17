package com.project.warehousemanagement.persistence.service.impl;

import com.project.warehousemanagement.constants.OrderStatus;
import com.project.warehousemanagement.dto.CreateOrderRequest;
import com.project.warehousemanagement.dto.UpdateOrderRequest;
import com.project.warehousemanagement.exception.BadRequestException;
import com.project.warehousemanagement.exception.ForbiddenException;
import com.project.warehousemanagement.exception.NotFoundException;
import com.project.warehousemanagement.exception.OrderAccessDeniedException;
import com.project.warehousemanagement.persistence.dto.OrderDto;
import com.project.warehousemanagement.persistence.entity.InventoryItem;
import com.project.warehousemanagement.persistence.entity.Order;
import com.project.warehousemanagement.persistence.entity.OrderItem;
import com.project.warehousemanagement.persistence.entity.User;
import com.project.warehousemanagement.persistence.repository.InventoryItemRepository;
import com.project.warehousemanagement.persistence.repository.OrderRepository;
import com.project.warehousemanagement.persistence.repository.UserRepository;
import com.project.warehousemanagement.persistence.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository ordersRepository;
    private final UserRepository usersRepository;
    private final InventoryItemRepository inventoryItemRepository;

    @Override
    public OrderDto createOrder(CreateOrderRequest req, String clientUsername) {
        log.info("Create order");
        User client = usersRepository.findByUsername(clientUsername)
                .orElseThrow(() -> new EntityNotFoundException("client not found"));

        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setClient(client);
        order.setSubmittedDate(LocalDateTime.now());
        order.setDeadlineDate(req.getDeadlineDate());
        order.setStatus(OrderStatus.CREATED);

        log.info("Set order items");
        List<OrderItem> orderItems = req.getItem().stream()
                .map(it -> buildItem(it, order))
                .toList();
        log.info("Order items: {}", orderItems.get(0));
        order.setItems(orderItems);

        Order saved = ordersRepository.save(order);
        log.info("Saved order");
        return OrderDto.fromEntity(saved, true);
    }

    private OrderItem buildItem(CreateOrderRequest.Item dto, Order order) {
        log.info("Build item method called");
        InventoryItem stockItem = inventoryItemRepository.findById(dto.getItemId())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Item %d not found".formatted(dto.getItemId())));

        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setItem(stockItem);
        item.setCurrentPrice(stockItem.getUnitPrice());
        item.setRequestedQty(dto.getQuantity());
        log.info("Item build completed");
        return item;
    }

    @Override
    public OrderDto updateOrder(Long orderId,
                                UpdateOrderRequest updateOrderRequest,
                                String clientUsername) {
        log.info("Update order method called");
        Order order = ordersRepository.findByIdAndClientUsername(orderId, clientUsername)
                .orElseThrow(() -> new EntityNotFoundException("order not found"));

        if (!EnumSet.of(OrderStatus.CREATED, OrderStatus.DECLINED).contains(order.getStatus()))
            throw new IllegalStateException("Cannot update order in status " + order.getStatus());


        Map<Long, UpdateOrderRequest.Item> incoming =
                updateOrderRequest.getItems().stream()
                        .collect(Collectors.toMap(
                                UpdateOrderRequest.Item::getItemId,
                                Function.identity(),
                                (a, b) -> b));


        for (OrderItem item : new ArrayList<>(order.getItems())) {
            UpdateOrderRequest.Item patch = incoming.remove(item.getItem().getId());

            if (patch == null) continue;
            if (patch.getQuantity() == 0) {
                order.getItems().remove(item);
                continue;
            }
            item.setRequestedQty(patch.getQuantity());
        }

        for (UpdateOrderRequest.Item patch : incoming.values()) {
            if (patch.getQuantity() == 0) continue;

            InventoryItem item = inventoryItemRepository.findById(patch.getItemId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "item %d not found".formatted(patch.getItemId())));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setItem(item);
            orderItem.setCurrentPrice(item.getUnitPrice());
            orderItem.setRequestedQty(item.getQuantity());

            order.getItems().add(orderItem);
        }

        log.info("Update order completed");
        Order saved = ordersRepository.save(order);
        return OrderDto.fromEntity(saved, true);
    }

    @Override
    public OrderDto submitOrder(Long id, String clientUsername) {
        log.info("Submit order");
        Order order = getOwnedOrder(id, clientUsername);
        if (EnumSet.of(OrderStatus.CREATED, OrderStatus.DECLINED).contains(order.getStatus())) {
            order.setStatus(OrderStatus.AWAITING_APPROVAL);
            order.setSubmittedDate(LocalDateTime.now());
            order.setOrderNumber(UUID.randomUUID().toString());
        }
        log.info("Submit order completed");
        return OrderDto.fromEntity(order, true);
    }

    @Override
    public OrderDto cancelOrder(Long id, String clientUsername) {
        log.info("Cancel order");
        Order order = getOwnedOrder(id, clientUsername);
        if (!EnumSet.of(OrderStatus.FULFILLED, OrderStatus.UNDER_DELIVERY,
                OrderStatus.CANCELED).contains(order.getStatus())) {
            order.setStatus(OrderStatus.CANCELED);
        }
        log.info("Cancel order completed");
        return OrderDto.fromEntity(order, true);
    }

    @Override
    public List<OrderDto> listForClient(String user, OrderStatus orderStatus, boolean withItems) {
        log.info("List for client {} ", user);
        User client = usersRepository.findByUsername(user).orElseThrow();

        List<Order> list = orderStatus == null ? ordersRepository.findByClientUsername(client.getUsername()) :
                ordersRepository.findByClientUsernameAndStatus(client.getUsername(), orderStatus);

        return list.stream()
                .map(order ->
                        OrderDto.fromEntity(order, withItems))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> listByStatus(OrderStatus orderStatus, boolean withItems) {
        log.info("Inside listByStatus method");
        return ordersRepository.findByStatus(orderStatus)
                .stream().map(o ->
                        OrderDto.fromEntity(o, withItems))
                .toList();
    }

    @Override
    public OrderDto approveOrder(Long id) {
        log.info("Approve order");
        Order order = ordersRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order", id));

        if (order.getStatus() != OrderStatus.AWAITING_APPROVAL) {
            throw new BadRequestException(
                    String.format("Order %d cannot be approved: current status is %s", id, order.getStatus()));
        }


        order.setStatus(OrderStatus.APPROVED);
        ordersRepository.save(order);
        log.info("Approve order completed");
        return OrderDto.fromEntity(order, true);
    }

    @Override
    public OrderDto declineOrder(Long id, String reason) {
        log.info("Decline order");
        Order order = ordersRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order", id));

        if (order.getStatus() != OrderStatus.AWAITING_APPROVAL) {
            throw new BadRequestException(
                    String.format(
                            "Order %d cannot be declined: current status is %s",
                            id, order.getStatus()
                    ));
        }

        order.setStatus(OrderStatus.DECLINED);
        order.setDeclineReason(reason);

        ordersRepository.save(order);
        log.info("Decline order completed");
        return OrderDto.fromEntity(order, true);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto detailOrder(Long id, String clientUsername, boolean withItems) {
        log.info("Get detail order");
        Order order = ordersRepository.findById(id).orElseThrow();
        /*if (!order.getClient().getUsername().equals(clientUsername)) {
            throw new OrderAccessDeniedException(id, clientUsername);
        }*/
        return OrderDto.fromEntity(order, withItems);
    }

    private Order getOwnedOrder(Long orderId, String username) {
        log.info("Get owned order");
        Order order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order", orderId));

        if (!order.getClient().getUsername().equals(username)) {
            throw new ForbiddenException(String.format("Order %d does not belong to user '%s'", orderId, username));
        }

        return order;
    }

    @Transactional(readOnly = true)
    public List<OrderDto> findAll() {
        log.info("Find all orders");
        List<Order> orderList = ordersRepository.findAll();
        return orderList.stream().map(order -> OrderDto.fromEntity(order, true)).toList();
    }

    private String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

}
