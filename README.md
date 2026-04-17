# API Gestión de Franquicias

API reactiva desarrollada en Spring Boot para gestionar franquicias, sucursales y productos con inventario.

## 📋 Características Principales

- ✅ **Arquitectura limpia (Clean Architecture)**: Separación clara de responsabilidades
- ✅ **Programación Reactiva**: Utiliza Spring WebFlux y Project Reactor para operaciones no bloqueantes
- ✅ **Base de datos reactiva**: MongoDB con Spring Data Reactive
- ✅ **Tests unitarios**: Cobertura completa con JUnit 5 y Mockito
- ✅ **Containerización**: Docker y Docker Compose para ambiente aislado
- ✅ **Infrastructure as Code**: Terraform para provisionar infraestructura
- ✅ **Validaciones**: Validación de datos con Jakarta Validation
- ✅ **Logging estructurado**: Logs en tiempo real con SLF4J

## 🏗️ Estructura del Proyecto

```
franquicias-api/
├── src/
│   ├── main/
│   │   ├── java/com/franquicias/
│   │   │   ├── domain/              # Capa de dominio (entidades, repositorios)
│   │   │   ├── application/         # Capa de aplicación (casos de uso, DTOs)
│   │   │   ├── presentation/        # Capa de presentación (controladores)
│   │   │   └── FranquiciasApplication.java
│   │   └── resources/
│   │       └── application.yml      # Configuración de la aplicación
│   └── test/
│       └── java/com/franquicias/    # Tests unitarios
├── terraform/                        # Infrastructure as Code
│   ├── main.tf
│   └── variables.tf
├── pom.xml                          # Dependencias Maven
├── Dockerfile                       # Containerización de la API
├── docker-compose.yml               # Orquestación de contenedores
├── init-mongo.js                    # Script de inicialización de MongoDB
└── README.md                        # Este archivo
```

## 🚀 Requisitos Previos

- Java 17 o superior
- Maven 3.9.x o superior
- Docker y Docker Compose
- Git
- Terraform (opcional, para Infrastructure as Code)

## 💻 Instalación Local

### 1. Clonar el repositorio

```bash
git clone <repository-url>
cd franquicias-api
```

### 2. Compilar el proyecto

```bash
mvn clean install
```

### 3. Ejecutar tests

```bash
mvn test
```

### 4. Ejecutar la aplicación

#### Opción A: Con Docker Compose (Recomendado)

```bash
docker-compose up --build
```

La API estará disponible en: `http://localhost:8080/api`

MongoDB estará disponible en: `mongodb://admin:password@localhost:27017/franquicias`

#### Opción B: Con Terraform

```bash
cd terraform
terraform init
terraform plan
terraform apply
```

#### Opción C: Localmente

Asegúrate de tener MongoDB ejecutándose localmente en el puerto 27017 con usuario `admin` y contraseña `password`.

```bash
mvn spring-boot:run
```

## 📡 Endpoints de la API

### Franquicias

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/franquicias` | Crear una nueva franquicia |
| GET | `/api/franquicias` | Obtener todas las franquicias |
| GET | `/api/franquicias/{id}` | Obtener una franquicia por ID |
| PATCH | `/api/franquicias/{id}/nombre` | Actualizar nombre de franquicia |
| DELETE | `/api/franquicias/{id}` | Eliminar una franquicia |

### Sucursales

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/franquicias/{franquiciaId}/sucursales` | Crear sucursal |
| GET | `/api/franquicias/{franquiciaId}/sucursales` | Obtener sucursales de una franquicia |
| GET | `/api/franquicias/{franquiciaId}/sucursales/{sucursalId}` | Obtener una sucursal |
| PATCH | `/api/franquicias/{franquiciaId}/sucursales/{sucursalId}/nombre` | Actualizar nombre de sucursal |
| DELETE | `/api/franquicias/{franquiciaId}/sucursales/{sucursalId}` | Eliminar una sucursal |

### Productos

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos` | Crear producto |
| GET | `/api/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos` | Obtener productos de una sucursal |
| GET | `/api/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos/{productoId}` | Obtener un producto |
| PATCH | `/api/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos/{productoId}/stock` | Actualizar stock |
| PATCH | `/api/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos/{productoId}/nombre` | Actualizar nombre de producto |
| DELETE | `/api/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos/{productoId}` | Eliminar producto |

### Reportes

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/franquicias/{franquiciaId}/productos-max-stock` | Productos con máximo stock por sucursal |

## 📝 Ejemplos de Uso

### Crear una Franquicia

```bash
curl -X POST http://localhost:8080/api/franquicias \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Franquicia A"
  }'
```

### Crear una Sucursal

```bash
curl -X POST http://localhost:8080/api/franquicias/{franquiciaId}/sucursales \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Sucursal Centro"
  }'
```

### Crear un Producto

```bash
curl -X POST http://localhost:8080/api/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Producto X",
    "stock": 100
  }'
```

### Actualizar Stock de un Producto

```bash
curl -X PATCH http://localhost:8080/api/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos/{productoId}/stock \
  -H "Content-Type: application/json" \
  -d '{
    "stock": 50
  }'
```

### Obtener Productos con Máximo Stock

```bash
curl http://localhost:8080/api/franquicias/{franquiciaId}/productos-max-stock
```

## 🧪 Pruebas Unitarias

Los tests unitarios se encuentran en `src/test/java/com/franquicias/`.

Ejecutar todos los tests:

```bash
mvn test
```

Ejecutar un test específico:

```bash
mvn test -Dtest=FranquiciaServiceTest
```

Con cobertura de código:

```bash
mvn test jacoco:report
```

## 🐳 Docker

### Construir imagen

```bash
docker build -t franquicias-api:latest .
```

### Ejecutar contenedor

```bash
docker run -p 8080:8080 \
  -e MONGO_HOST=mongodb \
  -e MONGO_PORT=27017 \
  -e MONGO_USER=admin \
  -e MONGO_PASSWORD=password \
  -e MONGO_DATABASE=franquicias \
  franquicias-api:latest
```

### Usar Docker Compose

```bash
# Iniciar
docker-compose up

# Detener
docker-compose down

# Ver logs
docker-compose logs -f
```

## 🏗️ Infrastructure as Code con Terraform

### Inicializar Terraform

```bash
cd terraform
terraform init
```

### Planificar la infraestructura

```bash
terraform plan
```

### Aplicar la infraestructura

```bash
terraform apply
```

### Ver outputs

```bash
terraform output
```

### Destruir la infraestructura

```bash
terraform destroy
```

## 📊 Flujo de Git

1. **rama feature**: Nuevas características
2. **rama develop**: Integración de cambios
3. **rama main**: Producción

### Ejemplo de flujo

```bash
# Crear rama de feature
git checkout -b feature/nueva-funcionalidad

# Realizar cambios
git add .
git commit -m "Agregar nueva funcionalidad"

# Hacer push
git push origin feature/nueva-funcionalidad

# Crear Pull Request
# Revisar y mergear a develop
# Después hacer release merge a main
```

## 🔍 Variables de Entorno

| Variable | Valor por defecto | Descripción |
|----------|-------------------|-------------|
| MONGO_USER | admin | Usuario de MongoDB |
| MONGO_PASSWORD | password | Contraseña de MongoDB |
| MONGO_HOST | localhost | Host de MongoDB |
| MONGO_PORT | 27017 | Puerto de MongoDB |
| MONGO_DATABASE | franquicias | Base de datos MongoDB |
| SERVER_PORT | 8080 | Puerto del servidor |

## 🐛 Troubleshooting

### MongoDB no se conecta

1. Verificar que MongoDB esté ejecutándose: `docker ps`
2. Verificar variables de entorno
3. Revisar logs: `docker-compose logs mongodb`

### API no responde

1. Verificar puerto 8080: `netstat -tlnp | grep 8080`
2. Revisar logs de la aplicación: `docker-compose logs franquicias-api`
3. Ejecutar health check: `curl http://localhost:8080/actuator/health`

### Tests fallan

1. Limpiar caché: `mvn clean`
2. Eliminar dependencias: `rm -rf ~/.m2/repository`
3. Reinstalar: `mvn install`

## 📚 Tecnologías Utilizadas

- **Spring Boot 3.2.4**: Framework Java
- **Spring WebFlux**: Programación reactiva
- **Spring Data MongoDB Reactive**: Acceso a datos reactivo
- **Project Reactor**: Implementación reactiva
- **Lombok**: Reducción de boilerplate
- **MapStruct**: Mapeo de objetos
- **MongoDB 7.0**: Base de datos NoSQL
- **Docker**: Containerización
- **Terraform**: Infrastructure as Code
- **JUnit 5**: Framework de testing
- **Mockito**: Mocking para tests

## 👨‍💻 Contribución

Las contribuciones son bienvenidas. Por favor:

1. Fork el proyecto
2. Crear una rama para tu feature
3. Realizar commits descriptivos
4. Hacer push a la rama
5. Crear un Pull Request

## 📄 Licencia

Este proyecto está bajo licencia MIT.

## 📧 Contacto

Para preguntas o sugerencias, contactar al equipo de desarrollo.

---

**Última actualización**: 16 de abril de 2026
**Versión**: 1.0.0
