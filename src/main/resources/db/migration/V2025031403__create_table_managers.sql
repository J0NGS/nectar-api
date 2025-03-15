-- Tabela Managers
CREATE TABLE IF NOT EXISTS managers (
    id UUID PRIMARY KEY NOT NULL,
    user_id UUID,
    org_id UUID,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (org_id) REFERENCES users(id)
);