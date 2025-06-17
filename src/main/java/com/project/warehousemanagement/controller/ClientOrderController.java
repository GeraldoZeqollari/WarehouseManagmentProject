package com.project.warehousemanagement.controller;


import com.project.warehousemanagement.constants.OrderStatus;
import com.project.warehousemanagement.dto.CreateOrderRequest;
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

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/client/orders")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('CLIENT')")
@Slf4j
public class ClientOrderController {

    private final OrderService ordersService;

    @PostMapping("/create")
    public ResponseEntity<OrderDto> create(@Valid @RequestBody CreateOrderRequest createOrderRequest,
                                           @AuthenticationPrincipal String username) {

        OrderDto body = ordersService.createOrder(createOrderRequest, username);

        URI location = URI.create("/api/orders/client/create" + body.getId());

        return ResponseEntity.created(location).body(body);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<OrderDto> update(@PathVariable Long id,
                                           @Valid @RequestBody UpdateOrderRequest orderRequest,
                                           @AuthenticationPrincipal String username) {
        return ResponseEntity.ok(ordersService.updateOrder(id, orderRequest, username));
    }


    @PostMapping("/submit/{id}")
    public ResponseEntity<OrderDto> submit(@PathVariable Long id,
                                           @AuthenticationPrincipal String username) {
        return ResponseEntity.ok(ordersService.submitOrder(id, username));
    }


    @PostMapping("/cancel/{id}")
    public ResponseEntity<OrderDto> cancel(@PathVariable Long id,
                                           @AuthenticationPrincipal String username) {
        return ResponseEntity.ok(ordersService.cancelOrder(id, username));
    }


    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderDto>> listForClient(@RequestParam(required = false) OrderStatus status,
                                                        @AuthenticationPrincipal String username) {
        List<OrderDto> list = ordersService.listForClient(username, status, true);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/orderStatus")
    public ResponseEntity<List<OrderDto>> listByStatus(@RequestParam OrderStatus orderStatus) {
        List<OrderDto> list = ordersService.listByStatus(orderStatus, true);
        return ResponseEntity.ok(list);
    }


    @GetMapping("get/{id}")
    public ResponseEntity<OrderDto> detail(@PathVariable Long id,
                                           @AuthenticationPrincipal String username) {
        return ResponseEntity.ok(
                ordersService.detailOrder(id, username, true));
    }
}
