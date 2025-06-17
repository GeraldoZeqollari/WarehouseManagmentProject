package com.project.warehousemanagement.persistence.service;

import com.project.warehousemanagement.constants.OrderStatus;
import com.project.warehousemanagement.dto.CreateOrderRequest;
import com.project.warehousemanagement.dto.UpdateOrderRequest;
import com.project.warehousemanagement.persistence.dto.OrderDto;

import java.util.List;

public interface OrderService {

    OrderDto createOrder(CreateOrderRequest createOrderRequest, String clientUsername);

    OrderDto updateOrder(Long id, UpdateOrderRequest orderRequest, String clientUsername);

    OrderDto submitOrder(Long id, String clientUsername);

    OrderDto cancelOrder(Long id, String clientUsername);

    List<OrderDto> listForClient(String username, OrderStatus status, boolean withItems);

    List<OrderDto> listByStatus(OrderStatus status, boolean withItems);

    List<OrderDto> findAll();

    OrderDto approveOrder(Long id);

    OrderDto declineOrder(Long id, String reason);

    OrderDto detailOrder(Long id, String clientUsername, boolean withItems);

}
