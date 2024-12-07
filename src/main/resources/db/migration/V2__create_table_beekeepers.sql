CREATE TABLE IF NOT EXISTS beekeepers (
    id UUID PRIMARY KEY NOT NULL,
    status VARCHAR(50),
    email VARCHAR(255),
    profile_id UUID,
    owner_id UUID,
    org_id UUID,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE SET NULL,
    FOREIGN KEY (owner_id) REFERENCES users(id),
    FOREIGN KEY (org_id) REFERENCES users(id)
);