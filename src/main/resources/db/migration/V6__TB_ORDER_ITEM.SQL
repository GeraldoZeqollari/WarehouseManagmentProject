CREATE TABLE tb_order_item
(
    id                BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    order_id          BIGINT NOT NULL,
    inventory_item_id BIGINT NOT NULL,
    requested_qty     INT    NOT NULL,
    current_price     DECIMAL(4,2),
    ordered_price     DECIMAL(4,2),

    CONSTRAINT fk_order_item_order
        FOREIGN KEY (order_id)
            REFERENCES tb_order (id)
            ON UPDATE CASCADE
            ON DELETE CASCADE,

    CONSTRAINT fk_order_item_inv
        FOREIGN KEY (inventory_item_id)
            REFERENCES tb_inventory_item (id)
            ON UPDATE CASCADE
            ON DELETE RESTRICT
);