ALTER TABLE asignacion_docente ADD COLUMN pele_id BIGINT;
ALTER TABLE asignacion_docente ADD CONSTRAINT fk_asignacion_periodo FOREIGN KEY (pele_id) REFERENCES periodo_lectivo(pele_id); 