package com.project.warehousemanagement.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DeliveryScheduleRequest {
    @NotEmpty
    private List<@Positive Long> truckIds;
}