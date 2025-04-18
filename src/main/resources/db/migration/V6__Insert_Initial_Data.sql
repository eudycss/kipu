-- Insertar roles
INSERT INTO roles (id, name) VALUES (1, 'ROLE_RECTOR');
INSERT INTO roles (id, name) VALUES (2, 'ROLE_DOCENTE');
SELECT setval('roles_id_seq', 2, true);

-- Insertar usuario RECTOR (password: Admin123*)
INSERT INTO users (id, username, password, nombre_completo, estado)
VALUES (1, 'admin', '$2a$10$Uf1TH0R8RXwXnvMKkfPtZeQqCB3.GiuWb8vGaolZi9yVl5vfFcxTi', 'Administrador', 'ACTIVO');
SELECT setval('users_id_seq', 1, true);

-- Asignar rol RECTOR al usuario admin
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);

-- Insertar Proceso Educativo
INSERT INTO proceso_educativo (pred_id, pred_descripcion, pred_nemonico, pred_estado, pred_nivel)
VALUES 
(1, 'Inserción a la Vida Escolar', 'IPS', 'ACTIVO', 'BASICA'),
(2, 'Fortalecimiento Cognitivo, Afectivo y Psicomotriz', 'FCAP', 'ACTIVO', 'BASICA'),
(3, 'Desarrollo de Destrezas y Técnicas de Estudio', 'DDTE', 'ACTIVO', 'BASICA'),
(4, 'Proceso de Aprendizaje Investigativo', 'PAI', 'ACTIVO', 'BASICA');
SELECT setval('proceso_educativo_pred_id_seq', 4, true);

-- Insertar Areas
INSERT INTO area (ar_id, ar_nombre, ar_estado)
VALUES 
(1, 'Lengua y Literatura', true),
(2, 'Matemática', true),
(3, 'Ciencias Naturales', true),
(4, 'Estudios Sociales', true);
SELECT setval('area_ar_id_seq', 4, true);

-- Insertar Asignaturas
INSERT INTO asignatura (asi_id, asi_nombre, asi_nemonico, asi_estado, ar_id)
VALUES 
(1, 'Unidades Integradas', 'UNIDINT', 'ACTIVO', 1),
(2, 'Proyectos Escolares', 'PROESC', 'ACTIVO', 1),
(3, 'Inglés', 'ING', 'ACTIVO', 1);
SELECT setval('asignatura_asi_id_seq', 3, true);

-- Insertar Grados
INSERT INTO grado (gra_id, gra_descripcion, gra_nemonico, gra_estado, pred_id)
VALUES 
(1, 'Primero de Básica', '1EB', 'ACTIVO', 1),
(2, 'Segundo de Básica', '2EB', 'ACTIVO', 2),
(3, 'Tercero de Básica', '3EB', 'ACTIVO', 2);
SELECT setval('grado_gra_id_seq', 3, true);

-- Insertar Periodo Lectivo
INSERT INTO periodo_lectivo (pele_id, pele_nombre, pele_fecha_inicio, pele_fecha_fin, pele_activo, pele_estado)
VALUES 
(1, '2023-2024', '2023-09-01', '2024-06-30', true, true);
SELECT setval('periodo_lectivo_pele_id_seq', 1, true);

-- Insertar Ofertas
INSERT INTO oferta (ofe_id, ofe_curso, ofe_paralelo, ofe_aforo, pele_id, gra_id, ofe_estado)
VALUES 
(1, 'Aula 1A', 'A', 30, 1, 1, true),
(2, 'Aula 2A', 'A', 30, 1, 2, true),
(3, 'Aula 3A', 'A', 30, 1, 3, true);
SELECT setval('oferta_ofe_id_seq', 3, true);

-- Insertar Docentes de ejemplo
INSERT INTO docente (doc_id, doc_identificacion, doc_nombre, ins_id, doc_estado, doc_correo, doc_telefono, ar_id)
VALUES 
(1, '1234567890', 'Juan Pérez', 1, true, 'juan.perez@example.com', '0991234567', 1),
(2, '0987654321', 'María López', 1, true, 'maria.lopez@example.com', '0997654321', 2),
(3, '1122334455', 'Carlos Ruiz', 1, true, 'carlos.ruiz@example.com', '0993334444', 3),
(4, '5566778899', 'Ana Gómez', 1, false, 'ana.gomez@example.com', '0995556666', 1);
SELECT setval('docente_doc_id_seq', 4, true); 