#!/bin/bash

# Script para compilar, probar y empaquetar la aplicación

set -e

echo "=========================================="
echo "API Gestión Franquicias - Build Script"
echo "=========================================="
echo ""

# Color codes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Funciones
log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Verificar requisitos
log_info "Verificando requisitos..."
if ! command -v mvn &> /dev/null; then
    log_error "Maven no está instalado"
    exit 1
fi

if ! command -v java &> /dev/null; then
    log_error "Java no está instalado"
    exit 1
fi

log_info "Versión de Java: $(java -version 2>&1 | head -n 1)"
log_info "Versión de Maven: $(mvn -v | head -n 1)"
echo ""

# Limpiar proyecto
log_info "Limpiando proyecto anterior..."
mvn clean

# Compilar
log_info "Compilando proyecto..."
mvn compile

# Ejecutar tests
log_info "Ejecutando tests unitarios..."
mvn test

# Empaquetar
log_info "Empaquetando aplicación..."
mvn package

echo ""
echo "=========================================="
log_info "Build completado exitosamente!"
echo "=========================================="
echo ""
log_info "JAR generado: target/franquicias-api-1.0.0.jar"
echo ""
echo "Pasos siguientes:"
echo "1. Docker Compose: docker-compose up --build"
echo "2. Terraform: cd terraform && terraform apply"
echo "3. Local: mvn spring-boot:run"
echo ""
