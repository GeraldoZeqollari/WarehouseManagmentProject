package com.project.warehousemanagement.persistence.repository;

import com.project.warehousemanagement.persistence.entity.Truck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TruckRepository extends JpaRepository<Truck, Long> {

    Optional<Truck> findByChassisNumber(String chassisNumber);

    Optional<Truck> findByLicensePlate(String licensePlate);

    void deleteTruckByChassisNumber(String chassisNumber);

    boolean existsByChassisNumber(String chassisNumber);

    boolean existsByLicensePlate(String licensePlate);


    @Modifying
    @Query("UPDATE Truck u SET u.containerVolume = :containerVolume  WHERE u.chassisNumber = :chassisNumber")
    void updateTruckByChassis(@Param("containerVolume") String containerVolume,
                         @Param("chassisNumber") String chassisNumber);


    @Modifying
    @Query("SELECT t FROM Truck t WHERE t.id NOT IN (" +
            "SELECT dt.id FROM Delivery d JOIN d.truck dt " +
            "WHERE d.scheduledDate = :deliveryDate" +
            ")")
    List<Truck> findAvailableTrucks(@Param("deliveryDate") LocalDate deliveryDate);


}
