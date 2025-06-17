package com.project.warehousemanagement.scheduler;

import com.project.warehousemanagement.constants.OrderStatus;
import com.project.warehousemanagement.persistence.repository.DeliveryRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@RequiredArgsConstructor
@Component
@Slf4j
public class DeliveryScheduler {


    private final DeliveryRepository deliveryRepository;


    @Scheduled(cron = "${jobs.delivery-scheduler-cron:0 5 2 * * *}")
    public void fulfilDeliveredOrders() {
        LocalDate today = LocalDate.now();
        int updated = deliveryRepository.markDeliveredOrdersFulfilled(
                today, OrderStatus.UNDER_DELIVERY, OrderStatus.FULFILLED);

        if (updated > 0) {
            log.info("DeliveryFulfilmentJob - {} order(s) marked FULFILLED (≤ {})", updated, today);
        } else {
            log.debug("DeliveryFulfilmentJob - no orders to fulfil (date: {})", today);
        }
    }


    @PostConstruct
    void init() {
        log.info("DeliveryFulfilmentJob scheduled — cron '{}'",
                System.getProperty("jobs.delivery-fulfilment-cron", "0 5 2 * * *"));
    }



}
