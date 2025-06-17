CREATE TABLE tb_order
(
    id             BIGINT      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    order_number   VARCHAR(64) NOT NULL UNIQUE,
    submitted_date TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `status`       VARCHAR(32) NOT NULL,
    deadline_date  DATE NULL,
    updated_date   TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    decline_reason TEXT,
    client_id      BINARY(16)     NOT NULL,
    CONSTRAINT fk_order_client
        FOREIGN KEY (client_id)
            REFERENCES tb_user (id)
            ON UPDATE RESTRICT
            ON DELETE RESTRICT
);