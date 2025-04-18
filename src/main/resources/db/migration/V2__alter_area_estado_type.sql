-- Primero eliminar el valor por defecto
ALTER TABLE area ALTER COLUMN ar_estado DROP DEFAULT;

-- Luego cambiar el tipo a boolean
ALTER TABLE area ALTER COLUMN ar_estado TYPE boolean USING CASE WHEN ar_estado = 'ACTIVO' THEN true ELSE false END;

-- Finalmente establecer el nuevo valor por defecto
ALTER TABLE area ALTER COLUMN ar_estado SET DEFAULT true; 