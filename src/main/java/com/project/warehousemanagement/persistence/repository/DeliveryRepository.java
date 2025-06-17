package com.project.warehousemanagement.persistence.repository;

import com.project.warehousemanagement.constants.OrderStatus;
import com.project.warehousemanagement.persistence.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    List<Delivery> findByScheduledDate(LocalDate date);

    Optional<Delivery> findByOrderId(Long orderId);

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE orders o
               SET status = :fulfilled
              FROM deliveries d
             WHERE d.order_id = o.id
               AND o.status   = :underDelivery
               AND d.delivery_date <= :today
            """, nativeQuery = true)
    int markDeliveredOrdersFulfilled(LocalDate today,
                                     OrderStatus underDelivery,
                                     OrderStatus fulfilled);

}
