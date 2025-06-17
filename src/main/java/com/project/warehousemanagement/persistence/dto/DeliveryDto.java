package com.project.warehousemanagement.persistence.dto;

import com.project.warehousemanagement.persistence.entity.Delivery;
import com.project.warehousemanagement.persistence.entity.Order;
import com.project.warehousemanagement.persistence.entity.Truck;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DeliveryDto {

    private Long id;
    private LocalDate scheduledDate;
    private Long truckId;
    private Long orderId;


    public static DeliveryDto fromEntity(Delivery delivery) {
        if (delivery == null) return null;
        DeliveryDto deliveryDto = new DeliveryDto();
        deliveryDto.setId(delivery.getId());
        deliveryDto.setScheduledDate(delivery.getScheduledDate());
        deliveryDto.setTruckId(delivery.getTruck().getId());
        deliveryDto.setOrderId(delivery.getOrder().getId());
        return deliveryDto;

    }

    public Delivery toEntity() {
        Delivery delivery = new Delivery();
        if (this.id != null) {
            delivery.setId(this.id);
        }
        delivery.setScheduledDate(this.scheduledDate);

        if (this.truckId != null) {
            Truck ref = new Truck();
            ref.setId(this.truckId);
            delivery.setTruck(ref);
        }

        if (this.orderId != null) {
            Order ref = new Order();
            ref.setId(this.orderId);
            delivery.setOrder(ref);
        }
        return delivery;
    }
}
