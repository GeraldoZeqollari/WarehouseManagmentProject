package com.project.warehousemanagement.persistence.repository;

import com.project.warehousemanagement.persistence.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    boolean existsByItemId(Long itemId);
}
