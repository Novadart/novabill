--CREATE TYPE crud_type AS ENUM ('create', 'update', 'delete');

--CREATE TYPE auditable_entity_type as ENUM ('client');


CREATE OR REPLACE FUNCTION create_populate_audit_tbl() RETURNS VOID AS $_$
	BEGIN
		IF NOT EXISTS (SELECT * FROM information_schema.tables 
        	WHERE 
            	table_catalog = CURRENT_CATALOG AND table_schema = CURRENT_SCHEMA AND table_name = 'audit_log') THEN
            	
            	CREATE TABLE audit_log (
					id BIGSERIAL PRIMARY KEY,
					entity_id BIGINT NOT NULL,
					entity auditable_entity_type NOT NULL,
					operation crud_type NOT NULL,
					version INTEGER NOT NULL,
					business BIGINT NOT NULL
				);
				
				INSERT INTO audit_log (entity_id, entity, operation, version, business)
					SELECT id, 'client', 'create', version, business from client;
				
		END IF;
	END $_$ LANGUAGE 'plpgsql';

SELECT create_populate_audit_tbl();
DROP FUNCTION create_populate_audit_tbl();

-- Clients audit
-- Insert client trigger
CREATE OR REPLACE FUNCTION log_insert_client() RETURNS TRIGGER AS $_$
	BEGIN
		INSERT INTO audit_log (entity_id, entity, operation, version, business) VALUES (new.id, 'client', 'create', new.version, new.business);
		RETURN new;
	END $_$ LANGUAGE 'plpgsql';

DROP TRIGGER IF EXISTS client_insert_audit_trigger ON client;
CREATE TRIGGER client_insert_audit_trigger AFTER INSERT ON client FOR EACH ROW EXECUTE PROCEDURE log_insert_client();

-- Update client trigger
CREATE OR REPLACE FUNCTION log_update_client() RETURNS TRIGGER AS $_$
	BEGIN
		INSERT INTO audit_log (entity_id, entity, operation, version, business) VALUES (new.id, 'client', 'update', new.version, new.business);
		RETURN new;
	END $_$ LANGUAGE 'plpgsql';

DROP TRIGGER IF EXISTS client_update_audit_trigger ON client;
CREATE TRIGGER client_update_audit_trigger AFTER UPDATE ON client FOR EACH ROW EXECUTE PROCEDURE log_update_client();

-- Delete client trigger
CREATE OR REPLACE FUNCTION log_delete_client() RETURNS TRIGGER AS $_$
	BEGIN
		INSERT INTO audit_log (entity_id, entity, operation, version, business) VALUES (old.id, 'client', 'delete', old.version, old.business);
		RETURN old;
	END $_$ LANGUAGE 'plpgsql';

DROP TRIGGER IF EXISTS client_delete_audit_trigger ON client;
CREATE TRIGGER client_delete_audit_trigger BEFORE DELETE ON client FOR EACH ROW EXECUTE PROCEDURE log_delete_client();

