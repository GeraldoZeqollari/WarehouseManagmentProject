package com.project.warehousemanagement.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateInventoryItemRequest {

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