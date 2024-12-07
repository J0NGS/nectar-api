-- Tabela Job
CREATE TABLE IF NOT EXISTS jobs (
    id UUID PRIMARY KEY NOT NULL,
    origin VARCHAR(255),
    appearance VARCHAR(255),
    scent VARCHAR(255),
    color VARCHAR(50),
    pesticides BOOLEAN,
    hive_loss BOOLEAN,
    quantity_of_bales INT,
    weight INT,
    post_processing_bales INT,
    post_processing_weight INT,
    post_processing_revenue INT,
    waste_rate INT,
    observation TEXT,
    product_type VARCHAR(50),
    status VARCHAR(50),
    beekeeper_id UUID,
    user_id UUID,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    FOREIGN KEY (beekeeper_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id)
);