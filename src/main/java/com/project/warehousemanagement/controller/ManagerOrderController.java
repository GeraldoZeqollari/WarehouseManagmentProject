package com.project.warehousemanagement.controller;


import com.project.warehousemanagement.dto.UpdateOrderRequest;
import com.project.warehousemanagement.persistence.dto.OrderDto;
import com.project.warehousemanagement.persistence.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager/orders")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('WAREHOUSE_MANAGER')")
@Slf4j
public class ManagerOrderController {

    private final OrderService ordersService;

/*
    @PutMapping("/update/{id}")
    public ResponseEntity<OrderDto> update(@PathVariable Long id,
                                           @Valid @RequestBody UpdateOrderRequest orderRequest,
                                           @AuthenticationPrincipal String username) {
        return ResponseEntity.ok(ordersService.updateOrder(id, orderRequest, username));
    }*/


    @PostMapping("/submit/{id}")
    public ResponseEntity<OrderDto> submit(@PathVariable Long id,
                                           @AuthenticationPrincipal String username) {
        return ResponseEntity.ok(ordersService.submitOrder(id, username));
    }

    /*
    @PostMapping("/cancel/{id}")
    public ResponseEntity<OrderDto> cancel(@PathVariable Long id,
                                           @AuthenticationPrincipal String username) {
        return ResponseEntity.ok(ordersService.cancelOrder(id, username));
    }*/

    @PostMapping("/approve/{id}")
    public ResponseEntity<OrderDto> approve(@PathVariable Long id) {
        return ResponseEntity.ok(ordersService.approveOrder(id));
    }

    @PostMapping("/decline/{id}")
    public ResponseEntity<OrderDto> decline(@PathVariable Long id, String reason) {
        return ResponseEntity.ok(ordersService.declineOrder(id, reason));
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<OrderDto> list = ordersService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<OrderDto> detail(@PathVariable Long id,
                                           @AuthenticationPrincipal String username) {
        return ResponseEntity.ok(
                ordersService.detailOrder(id, username, true));
    }
}
