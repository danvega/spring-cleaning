CREATE TABLE talk (
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    title     VARCHAR(255) NOT NULL,
    speaker   VARCHAR(255) NOT NULL,
    starts_at TIMESTAMP    NOT NULL,
    room      VARCHAR(255),
    seats     INT          NOT NULL
);
