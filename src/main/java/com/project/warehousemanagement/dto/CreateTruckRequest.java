package com.project.warehousemanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTruckRequest {

    @NotBlank
    @Size(max = 32)
    private String chassisNumber;

    @NotBlank
    @Size(max = 16)
    private String licensePlate;

    @NotNull
    @Positive
    private Double containerVolume;
}

