CREATE TABLE tb_inventory_item
(
    id         BIGINT         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR(255)   NOT NULL UNIQUE,
    quantity   INT            NOT NULL,
    unit_price DECIMAL(12, 2) NOT NULL,
    package_volume DOUBLE NOT NULL
);