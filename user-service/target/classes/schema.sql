CREATE TABLE IF NOT EXISTS app_users (
    id           BIGSERIAL PRIMARY KEY,
    username     VARCHAR(100)  NOT NULL UNIQUE,
    email        VARCHAR(255)  NOT NULL UNIQUE,
    first_name   VARCHAR(100),
    last_name    VARCHAR(100),
    phone_number VARCHAR(20),
    active       BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at   TIMESTAMP     NOT NULL,
    updated_at   TIMESTAMP     NOT NULL
);