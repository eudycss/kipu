-- Create Docente Table
CREATE TABLE docente (
    doc_id BIGSERIAL PRIMARY KEY,
    doc_identificacion VARCHAR(20) NOT NULL,
    doc_nombre VARCHAR(255) NOT NULL,
    ins_id BIGINT NOT NULL,
    doc_estado BOOLEAN DEFAULT true,
    doc_correo VARCHAR(255),
    doc_telefono VARCHAR(20),
    ar_id BIGINT
);

-- Create Asignatura Table
CREATE TABLE asignatura (
    asi_id BIGSERIAL PRIMARY KEY,
    asi_nombre VARCHAR(255) NOT NULL,
    asi_nemonico VARCHAR(20) NOT NULL,
    asi_estado VARCHAR(20) DEFAULT 'ACTIVO',
    ar_id BIGINT REFERENCES area(ar_id)
);

-- Create Periodo Lectivo Table
CREATE TABLE periodo_lectivo (
    pele_id BIGSERIAL PRIMARY KEY,
    pele_nombre VARCHAR(255) NOT NULL,
    pele_fecha_inicio DATE NOT NULL,
    pele_fecha_fin DATE NOT NULL,
    pele_activo BOOLEAN DEFAULT true,
    pele_estado BOOLEAN DEFAULT true
);

-- Create Proceso Educativo Table
CREATE TABLE proceso_educativo (
    pred_id BIGSERIAL PRIMARY KEY,
    pred_descripcion VARCHAR(255) NOT NULL,
    pred_nemonico VARCHAR(20) NOT NULL,
    pred_estado VARCHAR(20) DEFAULT 'ACTIVO',
    pred_nivel VARCHAR(20) NOT NULL
);

-- Create Grado Table
CREATE TABLE grado (
    gra_id BIGSERIAL PRIMARY KEY,
    gra_descripcion VARCHAR(255) NOT NULL,
    gra_nemonico VARCHAR(20) NOT NULL,
    gra_estado VARCHAR(20) DEFAULT 'ACTIVO',
    pred_id BIGINT REFERENCES proceso_educativo(pred_id)
);

-- Create Oferta Table
CREATE TABLE oferta (
    ofe_id BIGSERIAL PRIMARY KEY,
    ofe_curso VARCHAR(255) NOT NULL,
    ofe_paralelo VARCHAR(10) NOT NULL,
    ofe_aforo INTEGER NOT NULL,
    pele_id BIGINT REFERENCES periodo_lectivo(pele_id),
    gra_id BIGINT REFERENCES grado(gra_id),
    ofe_estado BOOLEAN DEFAULT true
); 