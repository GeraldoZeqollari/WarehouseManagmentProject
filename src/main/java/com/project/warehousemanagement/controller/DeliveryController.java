package com.project.warehousemanagement.controller;

import com.project.warehousemanagement.dto.DeliveryScheduleRequest;
import com.project.warehousemanagement.persistence.dto.DeliveryDto;
import com.project.warehousemanagement.persistence.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/manager/delivery")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('WAREHOUSE_MANAGER')")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping("/schedule/{orderId}")
    public ResponseEntity<DeliveryDto> schedule(@PathVariable Long orderId,
                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                @RequestBody DeliveryScheduleRequest request) {

        DeliveryDto body = deliveryService.scheduleDelivery(orderId, date, request.getTruckIds());

        URI location = URI.create("/api/manager/delivery/schedule/" + body.getId());

        return ResponseEntity
                .created(location)
                .body(body);
    }

    @GetMapping("/available-dates/{orderId}")
    public ResponseEntity<List<LocalDate>> available(@PathVariable Long orderId,
                                                     @RequestParam(defaultValue = "7") int daysAhead) {

        return ResponseEntity.ok(
                deliveryService.computeAvailableDates(orderId, daysAhead));
    }

    @GetMapping
    public ResponseEntity<List<DeliveryDto>> byDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        return ResponseEntity.ok(
                deliveryService.listDeliveriesByDate(date));
    }
}
