-- Add foreign key constraint to docente table
ALTER TABLE docente
ADD CONSTRAINT fk_docente_area FOREIGN KEY (ar_id) REFERENCES area(ar_id);

-- Create Asignacion Docente Table
CREATE TABLE asignacion_docente (
    asig_id BIGSERIAL PRIMARY KEY,
    doc_id BIGINT NOT NULL,
    asi_id BIGINT NOT NULL,
    ofe_id BIGINT NOT NULL,
    asig_estado BOOLEAN DEFAULT true,
    CONSTRAINT fk_asignacion_docente FOREIGN KEY (doc_id) REFERENCES docente(doc_id),
    CONSTRAINT fk_asignacion_asignatura FOREIGN KEY (asi_id) REFERENCES asignatura(asi_id),
    CONSTRAINT fk_asignacion_oferta FOREIGN KEY (ofe_id) REFERENCES oferta(ofe_id)
); 