CREATE TABLE TB_DELIVERY
(
    id             BIGINT NOT NULL AUTO_INCREMENT,
    scheduled_date DATE   NOT NULL,
    truck_id       BIGINT NOT NULL,
    order_id       BIGINT NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_delivery_truck_date (truck_id, scheduled_date),
    UNIQUE KEY uk_delivery_order (order_id),
    CONSTRAINT fk_delivery_truck
        FOREIGN KEY (truck_id)
            REFERENCES TB_TRUCK (id)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    CONSTRAINT fk_delivery_order
        FOREIGN KEY (order_id)
            REFERENCES TB_ORDER (id)
            ON DELETE RESTRICT
            ON UPDATE CASCADE
);