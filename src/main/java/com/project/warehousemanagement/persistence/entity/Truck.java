package com.project.warehousemanagement.persistence.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TB_TRUCK")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Truck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 32)
    private String chassisNumber;

    @Column(nullable = false, unique = true, length = 16)
    private String licensePlate;

    @Column(nullable = false)
    private Double containerVolume;


    @OneToMany(
            mappedBy = "truck",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Delivery> deliveries = new ArrayList<>();
}



