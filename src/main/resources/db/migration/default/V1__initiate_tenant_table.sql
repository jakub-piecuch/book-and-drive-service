CREATE SCHEMA admin

CREATE TABLE admin.tenant (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) UNIQUE NOT NULL
);

INSERT into admin.tenant (id, name)
VALUES (gen_random_uuid(), 'admin');
