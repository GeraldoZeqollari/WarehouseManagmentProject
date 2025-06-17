CREATE TABLE tb_delivery
(
    id             BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    scheduled_date DATE   NOT NULL,
    order_id       BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_delivery_order
        FOREIGN KEY (order_id)
            REFERENCES tb_order (id)
            ON UPDATE CASCADE
            ON DELETE CASCADE
);