package com.project.warehousemanagement.persistence.repository;

import com.project.warehousemanagement.constants.OrderStatus;
import com.project.warehousemanagement.persistence.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderNumber(String orderNumber);

    List<Order> findByClientUsername(String username);

    List<Order> findByClientUsernameAndStatus(String username, OrderStatus status);

    List<Order> findByStatus(OrderStatus status);

    Optional<Order> findByIdAndClientUsername(long id, String username);

}
