CREATE TABLE tb_delivery_truck
(
    delivery_id BIGINT NOT NULL,
    truck_id    BIGINT NOT NULL,

    PRIMARY KEY (delivery_id, truck_id),

    CONSTRAINT fk_deliv_truck_delivery
        FOREIGN KEY (delivery_id)
            REFERENCES tb_delivery (id)
            ON UPDATE CASCADE
            ON DELETE CASCADE,

    CONSTRAINT fk_deliv_truck_truck
        FOREIGN KEY (truck_id)
            REFERENCES tb_truck (id)
            ON UPDATE CASCADE
            ON DELETE RESTRICT
);