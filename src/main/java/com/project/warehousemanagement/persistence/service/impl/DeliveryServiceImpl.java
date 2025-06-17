package com.project.warehousemanagement.persistence.service.impl;

import com.project.warehousemanagement.constants.OrderStatus;
import com.project.warehousemanagement.exception.BadRequestException;
import com.project.warehousemanagement.exception.NotFoundException;
import com.project.warehousemanagement.persistence.dto.DeliveryDto;
import com.project.warehousemanagement.persistence.entity.Delivery;
import com.project.warehousemanagement.persistence.entity.Order;
import com.project.warehousemanagement.persistence.entity.Truck;
import com.project.warehousemanagement.persistence.repository.DeliveryRepository;
import com.project.warehousemanagement.persistence.repository.OrderRepository;
import com.project.warehousemanagement.persistence.repository.TruckRepository;
import com.project.warehousemanagement.persistence.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OrderRepository ordersRepository;
    private final TruckRepository truckRepository;


    @Override
    public DeliveryDto scheduleDelivery(Long orderId,
                                        LocalDate date,
                                        List<Long> truckIds) {

        log.info("Schedule delivery method called");
        if (date.getDayOfWeek() == DayOfWeek.SATURDAY ||
                date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new BadRequestException("Weekend scheduling is not allowed");
        }

        Order order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order", orderId));


        if (order.getStatus() != OrderStatus.APPROVED) {
            throw new BadRequestException("Order must be approved before scheduling");
        }

        if (truckIds == null || truckIds.isEmpty()) {
            throw new BadRequestException("At least one truck must be supplied");
        }

        List<Truck> trucks = truckRepository.findAllById(truckIds);
        if (trucks.size() != truckIds.size()) {
            List<Long> foundIds = trucks.stream()
                    .map(Truck::getId)
                    .toList();
            truckIds.removeAll(foundIds);
            throw new BadRequestException("Invalid truck IDs: " + truckIds);
        }


        double needed = order.getItems().stream()
                .mapToDouble(it -> it.getRequestedQty() * it.getItem().getPackageVolume())
                .sum();
        log.info("Needed: {}" , needed);

        double capacity = trucks.stream()
                .mapToDouble(Truck::getContainerVolume)
                .sum();
        log.info("Capacity: {}" , capacity);

        if (capacity < needed) {
            throw new BadRequestException(
                    String.format("Total truck capacity %.2f m³ is less than required %.2f m³", capacity, needed));
        }

        Delivery d = Delivery.builder()
                .order(order)
                .scheduledDate(date)
                .truck(trucks.get(0))
                .build();
        deliveryRepository.save(d);

        order.setStatus(OrderStatus.UNDER_DELIVERY);

        log.info("Delivery has been scheduled");
        return DeliveryDto.fromEntity(d);
    }

    @Override
    public List<LocalDate> computeAvailableDates(Long orderId, int daysAhead) {
        log.info("Compute available dates method called");
        if (daysAhead < 1 || daysAhead > 30) daysAhead = 30;

        LocalDate today = LocalDate.now();

        Set<LocalDate> booked = deliveryRepository.findAll().stream()
                .map(Delivery::getScheduledDate)
                .collect(Collectors.toSet());

        List<LocalDate> available = new ArrayList<>();
        for (int i = 1; i <= daysAhead; i++) {
            LocalDate d = today.plusDays(i);
            if (d.getDayOfWeek() == DayOfWeek.SATURDAY || d.getDayOfWeek() == DayOfWeek.SUNDAY) continue;
            if (!booked.contains(d)) available.add(d);
        }
        log.info("Available dates: {} ", available);
        return available;
    }

    @Override
    public List<DeliveryDto> listDeliveriesByDate(LocalDate date) {
        log.info("List deliveries by date method called");
        return deliveryRepository.findByScheduledDate(date).stream()
                .map(DeliveryDto::fromEntity)
                .toList();
    }
}