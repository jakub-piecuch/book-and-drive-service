-- Migrate to row-level multi-tenancy: add tenant_id discriminator column to all tenant-scoped tables

ALTER TABLE car ADD COLUMN IF NOT EXISTS tenant_id VARCHAR NOT NULL DEFAULT 'public';
ALTER TABLE instructor ADD COLUMN IF NOT EXISTS tenant_id VARCHAR NOT NULL DEFAULT 'public';
ALTER TABLE trainee ADD COLUMN IF NOT EXISTS tenant_id VARCHAR NOT NULL DEFAULT 'public';
ALTER TABLE lesson ADD COLUMN IF NOT EXISTS tenant_id VARCHAR NOT NULL DEFAULT 'public';

-- Replace single-column unique constraints with tenant-scoped composite ones
ALTER TABLE car DROP CONSTRAINT IF EXISTS car_registration_number_key;
ALTER TABLE car ADD CONSTRAINT car_tenant_registration_number_key UNIQUE (tenant_id, registration_number);

ALTER TABLE instructor DROP CONSTRAINT IF EXISTS instructor_email_key;
ALTER TABLE instructor ADD CONSTRAINT instructor_tenant_email_key UNIQUE (tenant_id, email);

ALTER TABLE trainee DROP CONSTRAINT IF EXISTS trainee_email_key;
ALTER TABLE trainee ADD CONSTRAINT trainee_tenant_email_key UNIQUE (tenant_id, email);
