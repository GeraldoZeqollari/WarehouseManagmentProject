package com.project.warehousemanagement.persistence.dto;

import com.project.warehousemanagement.persistence.entity.Truck;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TruckDto {
    private Long id;
    private String chassisNumber;
    private String licensePlate;
    private Double containerVolume;

    public static TruckDto fromEntity(Truck truck) {
        TruckDto truckDto = new TruckDto();
        truckDto.id = truck.getId();
        truckDto.chassisNumber = truck.getChassisNumber();
        truckDto.licensePlate = truck.getLicensePlate();
        truckDto.containerVolume = truck.getContainerVolume();
        return truckDto;
    }

    public Truck toEntity() {
        Truck truck = new Truck();
        truck.setChassisNumber(chassisNumber);
        truck.setLicensePlate(licensePlate);
        truck.setContainerVolume(containerVolume);
        return truck;
    }
}
