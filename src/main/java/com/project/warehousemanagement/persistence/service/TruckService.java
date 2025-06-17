package com.project.warehousemanagement.persistence.service;

import com.project.warehousemanagement.dto.CreateTruckRequest;
import com.project.warehousemanagement.dto.PatchTruckRequest;
import com.project.warehousemanagement.dto.UpdateTruckRequest;
import com.project.warehousemanagement.persistence.dto.TruckDto;

import java.time.LocalDate;
import java.util.List;

public interface TruckService {

    TruckDto createTruck(CreateTruckRequest createTruckRequest);

    TruckDto updateTruck(String chassisNumber, UpdateTruckRequest updateTruckRequest);

    TruckDto patchTruck(String chassisNumber, PatchTruckRequest patchTruckRequest);

    void deleteTruck(String chassisNumber);

    List<TruckDto> getTrucks();

    TruckDto detailTruck(String chassisNumber);

    List<TruckDto> getAvailableTrucks(LocalDate deliveryDate);
}
