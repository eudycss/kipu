# Documentación de Endpoints SEIBE API

## Introducción

Esta documentación describe los endpoints disponibles en la API de SEIBE para la gestión de docentes, ofertas académicas y asignaturas. Todos los endpoints requieren autenticación mediante JWT.

## Autenticación

### Registro de Usuario con Rol Rector

Para registrar un nuevo usuario con rol de Rector:

- **URL**: `/api/auth/signup`
- **Método**: POST
- **Cabecera**:
  - `Content-Type: application/json`
- **Cuerpo de la Petición**:
  ```json
  {
     "username": "rector3",
      "email": "nuevo_rector@example.com",
     "password": "MTIzNDU2Nzg",
     "roles": ["rector"]
  }
  ```
- **Ejemplo de respuesta exitosa** (Código 201 Created):
  ```json
  {
    "mensaje": "Usuario registrado exitosamente",
    "usuario": {
      "id": 2,
      "username": "rector_user",
      "nombre_completo": "Nombre del Rector",
      "roles": ["ROLE_RECTOR"],
      "estado": "ACTIVO"
    }
  }
  ```

### Inicio de Sesión y Obtención del Token JWT

Para iniciar sesión y obtener el token JWT necesario para las peticiones autenticadas:

- **URL**: `/api/auth/singin`
- **Método**: POST
- **Cabecera**:
  - `Content-Type: application/json`
- **Cuerpo de la Petición**:
  ```json
  {
    "username": "rector3",
    "password": "MTIzNDU2Nzg*"
  }
  ```
- **Ejemplo de respuesta exitosa** (Código 200 OK):
  ```json
  {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tipo": "Bearer",
    "username": "rector_user",
    "roles": ["ROLE_RECTOR"]
  }
  ```

### Uso del Token en Solicitudes

Para autenticarte en la API, debes incluir el token JWT obtenido en el encabezado `Authorization` de tus solicitudes:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

## Endpoints

### 1. Docentes

#### 1.1 Obtener docentes por área (ordenados)

Obtiene la lista de docentes ordenada por área, mostrando primero los docentes del área especificada y luego el resto de docentes.

- **URL**: `/api/docentes/area/{areaId}`
- **Método**: GET
- **Parámetros de ruta**:
  - `areaId`: ID del área (obligatorio)
- **Parámetros de consulta**:
  - `mostrarTodos`: Boolean (opcional, predeterminado: false)
    - Si es `true`, muestra todos los docentes ordenados por área
    - Si es `false`, solo muestra los docentes del área especificada
- **Permisos**: ROLE_ADMIN, ROLE_RECTOR
- **Ejemplo de solicitud**:
  ```
  GET http://localhost:8080/api/docentes/area/4?mostrarTodos=true
  ```
- **Ejemplo de respuesta**:
  ```json
  [
    {
      "docenteId": 2,
      "docNombre": "María López",
      "docIdentificacion": "0987654321",
      "docCorreo": "maria.lopez@example.com",
      "docTelefono": "0997654321",
      "docEstado": true,
      "institucionId": 1,
      "areaId": 4
    },
    {
      "docenteId": 1,
      "docNombre": "Juan Pérez",
      "docIdentificacion": "1234567890",
      "docCorreo": "juan.perez@example.com",
      "docTelefono": "0991234567",
      "docEstado": true,
      "institucionId": 1,
      "areaId": 1
    },
    ...
  ]
  ```

### 2. Ofertas

#### 2.1 Obtener docentes disponibles y no disponibles por oferta

Obtiene la lista de docentes disponibles y no disponibles para una oferta específica.

- **URL**: `/api/ofertas/{id}/docentes`
- **Método**: GET
- **Parámetros de ruta**:
  - `id`: ID de la oferta (obligatorio)
- **Permisos**: ROLE_ADMIN, ROLE_RECTOR
- **Ejemplo de solicitud**:
  ```
  GET http://localhost:8080/api/ofertas/1/docentes
  ```
- **Ejemplo de respuesta**:
  ```json
  [
    {
      "docenteId": 3,
      "docIdentificacion": "1122334455",
      "docNombre": "Carlos Ruiz",
      "docCorreo": "carlos.ruiz@example.com",
      "areaId": 3,
      "areaNombre": "Ciencias Naturales",
      "disponible": true
    },
    {
      "docenteId": 1,
      "docIdentificacion": "1234567890",
      "docNombre": "Juan Pérez",
      "docCorreo": "juan.perez@example.com",
      "areaId": 1,
      "areaNombre": "Lengua y Literatura",
      "disponible": false
    },
    ...
  ]
  ```

#### 2.2 Obtener ofertas por proceso educativo y periodo

Obtiene las ofertas académicas relacionadas con un proceso educativo específico para un periodo lectivo determinado.

- **URL**: `/api/ofertas/proceso/{procesoId}`
- **Método**: GET
- **Parámetros de ruta**:
  - `procesoId`: ID del proceso educativo (obligatorio)
- **Parámetros de consulta**:
  - `periodoId`: ID del periodo lectivo (obligatorio)
- **Permisos**: ROLE_ADMIN, ROLE_RECTOR
- **Ejemplo de solicitud**:
  ```
  GET http://localhost:8080/api/ofertas/proceso/2?periodoId=1
  ```
- **Ejemplo de respuesta**:
  ```json
  [
    {
      "ofertaId": 2,
      "ofeCurso": "Aula 2A",
      "ofeParalelo": "A",
      "ofeAforo": 30,
      "ofeEstado": true,
      "grado": {
        "gradoId": 2,
        "descripcion": "Segundo de Básica",
        "nemonico": "2EB"
      }
    },
    {
      "ofertaId": 3,
      "ofeCurso": "Aula 3A",
      "ofeParalelo": "A",
      "ofeAforo": 30,
      "ofeEstado": true,
      "grado": {
        "gradoId": 3,
        "descripcion": "Tercero de Básica",
        "nemonico": "3EB"
      }
    },
    {
      "ofertaId": 4,
      "ofeCurso": "Aula 4A",
      "ofeParalelo": "A",
      "ofeAforo": 25,
      "ofeEstado": true,
      "grado": {
        "gradoId": 4,
        "descripcion": "Cuarto de Básica",
        "nemonico": "4EB"
      }
    }
  ]
  ```

#### 2.3 Obtener asignaturas por oferta

Obtiene las asignaturas asociadas a una oferta específica, filtradas según el nivel educativo (grado) de la oferta.

- **URL**: `/api/ofertas/{id}/asignaturas`
- **Método**: GET
- **Parámetros de ruta**:
  - `id`: ID de la oferta (obligatorio)
- **Permisos**: ROLE_ADMIN, ROLE_RECTOR, ROLE_DOCENTE
- **Reglas de filtrado**:
  - Para 1° grado: Solo "Unidades Integradas" y "Proyectos Escolares"
  - Para 2°-4° grado: "Unidades Integradas", "Inglés" y "Proyectos Escolares"
  - Para 5°-7° grado: Igual que 2°-4° grado
  - Para 8°-10° grado: Todas las asignaturas del PAI (incluye Lengua y Literatura, Matemática, Ciencias Naturales, etc.)
- **Ejemplo de solicitud**:
  ```
  GET http://localhost:8080/api/ofertas/9/asignaturas
  ```
- **Ejemplo de respuesta**:
  ```json
  [
    {
      "asignaturaId": 4,
      "nombre": "Lengua y Literatura Nacionalidad",
      "nemonico": "LLN",
      "areaId": 1,
      "areaNombre": "Lengua y Literatura",
      "asignado": false
    },
    {
      "asignaturaId": 5,
      "nombre": "Lengua y Literatura Castellana",
      "nemonico": "LLC",
      "areaId": 1,
      "areaNombre": "Lengua y Literatura",
      "asignado": false
    },
    {
      "asignaturaId": 6,
      "nombre": "Matemática y Etnomatemática",
      "nemonico": "MATEM",
      "areaId": 2,
      "areaNombre": "Matemática",
      "asignado": false
    },
    ...
  ]
  ```

## Instalación y Uso

1. Importa la colección `seibe_endpoints_collection.json` en Postman
2. Configura la variable `base_url` con la URL base de tu API (por defecto: `http://localhost:8080`)
3. Ejecuta la solicitud "Iniciar Sesión" para obtener un token JWT válido
   - El token se guardará automáticamente en la variable de entorno `token`
   - Si esto no funciona, copia manualmente el token de la respuesta y actualiza la variable
4. Una vez que tengas el token, puedes ejecutar cualquiera de las otras solicitudes

## Notas adicionales

- Todos los endpoints devuelven el código de estado HTTP 200 (OK) para respuestas exitosas
- En caso de error, devolverán un código de estado apropiado (400, 401, 403, 404, 500, etc.) junto con un mensaje detallado
- Las respuestas siempre se devuelven en formato JSON 