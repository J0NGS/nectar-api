-- V6__add_default_roles.sql

-- Inserir roles padrão na tabela roles
INSERT INTO roles (id, name, created_at)
VALUES 
    (gen_random_uuid(), 'ROLE_MANAGER', NOW()),
    (gen_random_uuid(), 'ROLE_ORG', NOW());
