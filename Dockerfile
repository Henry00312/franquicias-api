# Etapa 1: Build - Compilar la aplicación
FROM maven:3.9.14-eclipse-temurin-25 AS builder

WORKDIR /app

# Copiar archivos de configuración de Maven
COPY pom.xml .

# Descargar dependencias (aprovecha la capa de caché de Docker)
RUN mvn dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Compilar y empaquetar la aplicación
RUN mvn clean package -DskipTests

# Etapa 2: Runtime - Ejecutar la aplicación
FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

# Copiar JAR desde la etapa de build
COPY --from=builder /app/target/franquicias-api-*.jar app.jar

# Exponer puerto
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
  CMD wget -q --spider http://localhost:8080/actuator/health 2>/dev/null || curl -sf http://localhost:8080/api/franquicias > /dev/null || exit 1

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]
