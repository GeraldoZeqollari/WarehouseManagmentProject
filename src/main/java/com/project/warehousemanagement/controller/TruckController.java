package com.project.warehousemanagement.controller;


import com.project.warehousemanagement.dto.CreateTruckRequest;
import com.project.warehousemanagement.dto.PatchTruckRequest;
import com.project.warehousemanagement.dto.UpdateTruckRequest;
import com.project.warehousemanagement.persistence.dto.TruckDto;
import com.project.warehousemanagement.persistence.service.TruckService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/manager/trucks")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('WAREHOUSE_MANAGER')")
public class TruckController {

    private final TruckService truckService;

    @PostMapping("/create")
    public ResponseEntity<TruckDto> create(@Valid @RequestBody CreateTruckRequest request) {
        TruckDto body = truckService.createTruck(request);
        URI location = URI.create("/api/manager/trucks" + body.getId());
        return ResponseEntity.created(location).body(body);
    }

    @PutMapping("/update/{chassisNumber}")
    public ResponseEntity<TruckDto> update(@PathVariable String chassisNumber,
                                           @Valid @RequestBody UpdateTruckRequest request) {
        return ResponseEntity.ok(truckService.updateTruck(chassisNumber, request));
    }

    @PatchMapping("/patchUpdate/{chassisNumber}")
    public ResponseEntity<TruckDto> patch(@PathVariable String chassisNumber,
                                           @Valid @RequestBody PatchTruckRequest request) {
        return ResponseEntity.ok(truckService.patchTruck(chassisNumber, request));
    }


    @DeleteMapping("/delete/{chassisNumber}")
    public ResponseEntity<Void> delete(@PathVariable String chassisNumber) {
        truckService.deleteTruck(chassisNumber);
        return ResponseEntity.noContent().build();
    }


    @GetMapping
    public ResponseEntity<List<TruckDto>> list() {
        return ResponseEntity.ok(truckService.getTrucks());
    }

    @GetMapping("/available")
    public ResponseEntity<List<TruckDto>> listAvailableTrucks(@RequestParam("deliveryDate") LocalDate deliveryDate) {
        return ResponseEntity.ok(truckService.getAvailableTrucks(deliveryDate));
    }


    @GetMapping("/{chassisNumber}")
    public ResponseEntity<TruckDto> detail(@PathVariable String chassisNumber) {
        return ResponseEntity.ok(truckService.detailTruck(chassisNumber));
    }
}
