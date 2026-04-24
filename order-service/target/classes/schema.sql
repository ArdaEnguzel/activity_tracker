CREATE TABLE IF NOT EXISTS orders (
    id               BIGSERIAL PRIMARY KEY,
    user_id          VARCHAR(100)   NOT NULL,
    status           VARCHAR(50)    NOT NULL DEFAULT 'PENDING',
    total_amount     NUMERIC(12, 2) NOT NULL,
    currency         VARCHAR(10)    NOT NULL DEFAULT 'TRY',
    shipping_address TEXT,
    created_at       TIMESTAMP      NOT NULL,
    updated_at       TIMESTAMP      NOT NULL
);

CREATE TABLE IF NOT EXISTS order_items (
    id           BIGSERIAL PRIMARY KEY,
    order_id     BIGINT         NOT NULL REFERENCES orders (id) ON DELETE CASCADE,
    product_id   BIGINT         NOT NULL,
    product_name VARCHAR(255)   NOT NULL,
    quantity     INTEGER        NOT NULL,
    unit_price   NUMERIC(12, 2) NOT NULL,
    total_price  NUMERIC(12, 2) NOT NULL
);