CREATE TABLE IF NOT EXISTS products (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(255)   NOT NULL,
    description     TEXT,
    category        VARCHAR(100),
    price           NUMERIC(12, 2) NOT NULL,
    currency        VARCHAR(10)    NOT NULL DEFAULT 'TRY',
    stock_quantity  INTEGER        NOT NULL DEFAULT 0,
    image_url       VARCHAR(512),
    active          BOOLEAN        NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP      NOT NULL,
    updated_at      TIMESTAMP      NOT NULL
);