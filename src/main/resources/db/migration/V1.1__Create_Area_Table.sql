-- Create Area Table
CREATE TABLE area (
    ar_id SERIAL PRIMARY KEY,
    ar_nombre VARCHAR(255) NOT NULL,
    ar_estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO'
); 