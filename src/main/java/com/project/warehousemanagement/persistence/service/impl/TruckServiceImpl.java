package com.project.warehousemanagement.persistence.service.impl;

import com.project.warehousemanagement.dto.CreateTruckRequest;
import com.project.warehousemanagement.dto.PatchTruckRequest;
import com.project.warehousemanagement.dto.UpdateTruckRequest;
import com.project.warehousemanagement.persistence.dto.TruckDto;
import com.project.warehousemanagement.persistence.entity.Truck;
import com.project.warehousemanagement.persistence.repository.TruckRepository;
import com.project.warehousemanagement.persistence.service.TruckService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TruckServiceImpl implements TruckService {

    private final TruckRepository trucksRepository;

    @Override
    public TruckDto createTruck(CreateTruckRequest request) {
        log.info("Create truck");
        if (trucksRepository.existsByChassisNumber(request.getChassisNumber()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Chassis number already exists");
        if (trucksRepository.existsByLicensePlate(request.getLicensePlate()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "License plate already exists");

        Truck truck = new Truck();
        truck.setChassisNumber(request.getChassisNumber());
        truck.setLicensePlate(request.getLicensePlate());
        truck.setContainerVolume(request.getContainerVolume());
        log.info("Create truck completed");
        return TruckDto.fromEntity(trucksRepository.save(truck));
    }

    @Override
    public TruckDto updateTruck(String chassisNumber, UpdateTruckRequest request) {
        log.info("Update truck");
        Truck truck = trucksRepository.findByChassisNumber(chassisNumber)
                .orElseThrow(() -> new EntityNotFoundException("Truck not found"));


        truck.setChassisNumber(request.getChassisNumber());
        truck.setLicensePlate(request.getLicensePlate());
        truck.setContainerVolume(request.getContainerVolume());
        log.info("Update truck completed");
        return TruckDto.fromEntity(truck);
    }

    @Override
    public TruckDto patchTruck(String chassisNumber, PatchTruckRequest req) {
        log.info("Patch truck");
        Truck truck = trucksRepository.findByChassisNumber(chassisNumber)
                .orElseThrow(() -> new EntityNotFoundException("Truck not found"));

        if (req.getChassisNumber() != null)
            truck.setChassisNumber(req.getChassisNumber());

        if (req.getLicensePlate() != null)
            truck.setLicensePlate(req.getLicensePlate());

        if (req.getContainerVolume() != null)
            truck.setContainerVolume(req.getContainerVolume());
        log.info("Patch truck completed");
        return TruckDto.fromEntity(truck);
    }

    @Override
    public void deleteTruck(String chassisNumber) {
        log.info("Delete truck");
        Truck truck = trucksRepository.findByChassisNumber(chassisNumber)
                .orElseThrow(() -> new EntityNotFoundException("Truck not found"));
        if (truck.getDeliveries().isEmpty()) {
            trucksRepository.deleteTruckByChassisNumber(chassisNumber);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Truck has deliveries active can not be deleted");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<TruckDto> getTrucks() {
        log.info("Get all trucks");
        return trucksRepository.findAll().stream().map(TruckDto::fromEntity).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TruckDto detailTruck(String chassisNumber) {
        log.info("Get truck detail");
        return TruckDto.fromEntity(trucksRepository.findByChassisNumber(chassisNumber).orElseThrow());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TruckDto> getAvailableTrucks(LocalDate deliveryDate) {
        log.info("Get available trucks");
        return trucksRepository.findAvailableTrucks(deliveryDate).stream().map(TruckDto::fromEntity).toList();
    }
}
