package com.project.warehousemanagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class PatchTruckRequest {

    @Size(max = 32)
    private String chassisNumber;

    @Size(max = 16)
    private String licensePlate;

    @Positive
    private Double containerVolume;

}