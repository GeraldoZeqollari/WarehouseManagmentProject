package com.project.warehousemanagement.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateInventoryItemRequest {

    @NotBlank
    @Size(max = 64)
    private String name;

    @NotNull
    @PositiveOrZero
    private Integer quantity;

    @NotNull
    @Positive
    private BigDecimal unitPrice;

    @NotNull
    @Positive
    private Double packageVolume;
}