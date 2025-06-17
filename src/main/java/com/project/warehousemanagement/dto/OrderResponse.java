package com.project.warehousemanagement.dto;


import com.project.warehousemanagement.constants.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderResponse {

    private Long id;
    private String orderNumber;
    private OrderStatus status;
    private LocalDateTime submittedDate;
    private LocalDate deadlineDate;
    private List<Line> lines;

    @Data
    @AllArgsConstructor
    public static class Line {
        private Long itemId;
        private String itemName;
        private Integer quantity;
    }
}