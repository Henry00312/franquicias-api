# API Gestion de Franquicias

API reactiva para gestionar franquicias, sucursales y productos con Spring Boot WebFlux y MongoDB.

## Caracteristicas

- Arquitectura por capas: domain, application, presentation
- Programacion reactiva con Mono y Flux
- Persistencia reactiva con MongoDB
- Validaciones de negocio y manejo de errores HTTP
- Soporte de ejecucion local con Docker Compose
- Soporte de despliegue en AWS (ECR + ECS + ALB)

## Stack tecnico

- Java 25
- Spring Boot 3.5.13
- Spring WebFlux
- Spring Data MongoDB Reactive
- Maven
- Docker y Docker Compose
- Terraform (opcional para IaC)

## Estructura del proyecto

```text
src/main/java/com/franquicias
├── domain        # Entidades y contratos de repositorio
├── application   # DTOs, mappers, servicios y casos de uso
└── presentation  # Controladores REST

src/test/java/com/franquicias
└── application/service
```

## Variables de entorno

Archivo base: `.env.example`

Variables principales:

- `MONGO_DATABASE` (default: franquicias)
- `MONGO_PORT` (default: 27017)
- `API_PORT` (default: 8080)

Nota: en Docker local la API usa `MONGODB_URI` con el contenedor de Mongo.

## Ejecucion local con Docker (recomendado)

1. Copiar variables de entorno:

```bash
cp .env.example .env
```

2. Iniciar servicios:

```bash
docker compose up -d --build
```

3. Verificar estado:

```bash
curl http://localhost:8080/api
curl http://localhost:8080/api/franquicias
```

4. Detener servicios:

```bash
docker compose down
```

## Ejecucion local sin Docker

Requiere MongoDB disponible y accesible.

```bash
mvn clean install
mvn spring-boot:run
```

## Pruebas

Ejecutar pruebas unitarias:

```bash
mvn test
```

Compilar sin pruebas:

```bash
mvn -DskipTests compile
```

## Endpoints principales

- `GET /api` estado de la API
- `POST /api/franquicias` crear franquicia
- `GET /api/franquicias` listar franquicias
- `POST /api/franquicias/{franquiciaId}/sucursales` crear sucursal
- `POST /api/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos` crear producto
- `GET /api/franquicias/{franquiciaId}/productos-max-stock` reporte de maximo stock

## Manejo de errores

- Duplicados de negocio retornan `409 Conflict` con mensaje en el body
- Recursos no encontrados retornan `404 Not Found`
- Errores de validacion o negocio retornan `400 Bad Request`

## Coleccion Postman

Archivo: [Postman_Collection.json](Postman_Collection.json)

Variables utiles:

- `base_url` (apunta a local o AWS)
- `local_base_url`
- `aws_base_url`
- `franquicia_id`
- `sucursal_id`
- `producto_id`

Incluye request para validar conflicto de duplicado (409).

## Despliegue en AWS

Documentacion separada en:

- [docs/aws/deployment-aws.md](docs/aws/deployment-aws.md)
- [docs/aws/artifacts/README.md](docs/aws/artifacts/README.md)

## Notas para mantenimiento

- Usar imagen por digest al desplegar en ECS para evitar drift
- Mantener politica de lifecycle en ECR para controlar acumulacion de imagenes
- Los JSON de task definitions manuales quedaron organizados en `docs/aws/artifacts`
