CREATE TABLE tb_truck
(
    id             BIGINT      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    chassis_number VARCHAR(32) NOT NULL UNIQUE,
    license_plate  VARCHAR(16) NOT NULL UNIQUE,
    container_volume DOUBLE NOT NULL
);