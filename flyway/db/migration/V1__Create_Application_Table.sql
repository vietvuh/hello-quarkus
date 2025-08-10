-- Flyway migration script for Application table
-- Version: V1__Create_Application_Table.sql
-- Description: Create the application table based on OpenAPI Application schema

CREATE TABLE application (
    application_key VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    owner_id UUID NOT NULL,
    management_group_id UUID,
    created_at TIMESTAMP NOT NULL,
    created_by UUID,
    updated_at TIMESTAMP,
    updated_by UUID,
    
    -- Primary key
    CONSTRAINT pk_application PRIMARY KEY (application_key),
    
    -- Check constraints for required fields
    CONSTRAINT chk_application_key_not_empty CHECK (application_key <> ''),
    CONSTRAINT chk_name_not_empty CHECK (name <> '')
);

-- Add comments to document the table structure
COMMENT ON TABLE application IS 'Application registry table storing application metadata';
COMMENT ON COLUMN application.application_key IS 'Unique key for the application, used as primary key';
COMMENT ON COLUMN application.name IS 'Human-readable name of the application';
COMMENT ON COLUMN application.description IS 'Optional description of the application';
COMMENT ON COLUMN application.owner_id IS 'UUID of the application owner';
COMMENT ON COLUMN application.management_group_id IS 'UUID of the group with management permissions';
COMMENT ON COLUMN application.created_at IS 'Creation timestamp';
COMMENT ON COLUMN application.created_by IS 'UUID of the user who created the application';
COMMENT ON COLUMN application.updated_at IS 'Last update timestamp';
COMMENT ON COLUMN application.updated_by IS 'UUID of the user who last updated the application';

-- Create indexes for better query performance
CREATE INDEX idx_application_owner_id ON application(owner_id);
CREATE INDEX idx_application_management_group_id ON application(management_group_id);