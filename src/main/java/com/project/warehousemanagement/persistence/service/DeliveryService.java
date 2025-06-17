package com.project.warehousemanagement.persistence.service;

import com.project.warehousemanagement.persistence.dto.DeliveryDto;

import java.time.LocalDate;
import java.util.List;

public interface DeliveryService {

    DeliveryDto scheduleDelivery(Long orderId, LocalDate date, List<Long> truckIds);

    List<LocalDate> computeAvailableDates(Long orderId, int daysAhead);

    List<DeliveryDto> listDeliveriesByDate(LocalDate date);
}
