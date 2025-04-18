-- Limpiar datos existentes
DELETE FROM asignacion_docente;
DELETE FROM asignacion_tutor;
DELETE FROM oferta;
DELETE FROM periodo_lectivo;
DELETE FROM grado;
DELETE FROM asignatura;
DELETE FROM area;
DELETE FROM proceso_educativo;
DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM roles;
DELETE FROM docente;

-- Reiniciar secuencias
ALTER SEQUENCE IF EXISTS roles_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS users_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS proceso_educativo_pred_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS area_ar_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS asignatura_asi_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS grado_gra_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS periodo_lectivo_pele_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS oferta_ofe_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS docente_doc_id_seq RESTART WITH 1;

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

-- Insertar Docentes de ejemplo
INSERT INTO docente (doc_id, doc_identificacion, doc_nombre, ins_id, doc_estado, doc_correo, doc_telefono, ar_id)
VALUES 
(1, '1234567890', 'Juan Pérez', 1, true, 'juan.perez@example.com', '0991234567', 1),
(2, '0987654321', 'María López', 1, true, 'maria.lopez@example.com', '0997654321', 2),
(3, '1122334455', 'Carlos Ruiz', 1, true, 'carlos.ruiz@example.com', '0993334444', 3),
(4, '5566778899', 'Ana Gómez', 1, false, 'ana.gomez@example.com', '0995556666', 1);  -- Docente inactivo
SELECT setval('docente_doc_id_seq', 4, true);

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
(4, 'Estudios Sociales', true),
(5, 'Educación Cultural y Artística', true),
(6, 'Educación Física', true),
(7, 'Lengua Extranjera', true);
SELECT setval('area_ar_id_seq', 7, true);

-- Insertar Asignaturas
INSERT INTO asignatura (asi_id, asi_nombre, asi_nemonico, asi_estado, ar_id)
VALUES 
-- Asignaturas básicas para todos los niveles
(1, 'Unidades Integradas', 'UNIDINT', 'ACTIVO', 1),
(2, 'Proyectos Escolares', 'PROESC', 'ACTIVO', 1),
(3, 'Inglés', 'ING', 'ACTIVO', 7),
-- Asignaturas para PAI (8°-10°)
(4, 'Lengua y Literatura Nacionalidad', 'LLN', 'ACTIVO', 1),
(5, 'Lengua y Literatura Castellana', 'LLC', 'ACTIVO', 1),
(6, 'Matemática y Etnomatemática', 'MATEM', 'ACTIVO', 2),
(7, 'Ciencias Naturales y Etnociencia', 'CNAT', 'ACTIVO', 3),
(8, 'Estudios Sociales y Etnohistoria', 'ESOC', 'ACTIVO', 4),
(9, 'Educación Cultural y Artística', 'ECA', 'ACTIVO', 5),
(10, 'Educación Física Intercultural', 'EFI', 'ACTIVO', 6);
SELECT setval('asignatura_asi_id_seq', 10, true);

-- Insertar Grados completos para todos los niveles
INSERT INTO grado (gra_id, gra_descripcion, gra_nemonico, gra_estado, pred_id)
VALUES 
-- Proceso IPS (1° grado)
(1, 'Primero de Básica', '1EB', 'ACTIVO', 1),
-- Proceso FCAP (2°-4° grado)
(2, 'Segundo de Básica', '2EB', 'ACTIVO', 2),
(3, 'Tercero de Básica', '3EB', 'ACTIVO', 2),
(4, 'Cuarto de Básica', '4EB', 'ACTIVO', 2),
-- Proceso DDTE (5°-7° grado)
(5, 'Quinto de Básica', '5EB', 'ACTIVO', 3),
(6, 'Sexto de Básica', '6EB', 'ACTIVO', 3),
(7, 'Séptimo de Básica', '7EB', 'ACTIVO', 3),
-- Proceso PAI (8°-10° grado)
(8, 'Octavo de Básica', '8EB', 'ACTIVO', 4),
(9, 'Noveno de Básica', '9EB', 'ACTIVO', 4),
(10, 'Décimo de Básica', '10EB', 'ACTIVO', 4);
SELECT setval('grado_gra_id_seq', 10, true);

-- Insertar Periodo Lectivo
INSERT INTO periodo_lectivo (pele_id, pele_nombre, pele_fecha_inicio, pele_fecha_fin, pele_activo, pele_estado)
VALUES 
(1, '2023-2024', '2023-09-01', '2024-06-30', true, true);
SELECT setval('periodo_lectivo_pele_id_seq', 1, true);

-- Insertar Ofertas para cada grado
INSERT INTO oferta (ofe_id, ofe_curso, ofe_paralelo, ofe_aforo, pele_id, gra_id, ofe_estado)
VALUES 
-- Ofertas para IPS (1° grado)
(1, 'Aula 1A', 'A', 30, 1, 1, true),
-- Ofertas para FCAP (2°-4° grado)
(2, 'Aula 2A', 'A', 30, 1, 2, true),
(3, 'Aula 3A', 'A', 30, 1, 3, true),
(4, 'Aula 4A', 'A', 25, 1, 4, true),
-- Ofertas para DDTE (5°-7° grado)
(5, 'Aula 5A', 'A', 30, 1, 5, true),
(6, 'Aula 6A', 'A', 30, 1, 6, true),
(7, 'Aula 7A', 'A', 30, 1, 7, true),
-- Ofertas para PAI (8°-10° grado)
(8, 'Aula 8A', 'A', 35, 1, 8, true),
(9, 'Aula 9A', 'A', 35, 1, 9, true),
(10, 'Aula 10A', 'A', 35, 1, 10, true);
SELECT setval('oferta_ofe_id_seq', 10, true);

-- Insertar algunas asignaciones docentes para probar el campo 'asignado'
INSERT INTO asignacion_docente (asdo_id, doc_id, ofe_id, asi_id, pele_id, asdo_estado)
VALUES 
(1, 1, 1, 1, 1, true),  -- Docente 1 asignado a Unidades Integradas en 1° grado
(2, 2, 2, 3, 1, true),  -- Docente 2 asignado a Inglés en 2° grado
(3, 3, 8, 6, 1, true);  -- Docente 3 asignado a Matemática en 8° grado
SELECT setval('asignacion_docente_asdo_id_seq', 3, true); 