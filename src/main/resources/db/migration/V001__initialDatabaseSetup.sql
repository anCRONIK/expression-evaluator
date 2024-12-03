CREATE TABLE expressions
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    identifier        UUID          NOT NULL,
    name              VARCHAR(150)  NOT NULL,
    expression        VARCHAR(2000) NOT NULL,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT unique_name UNIQUE (name)
);

CREATE INDEX idx_identifier ON expressions (identifier);
