#!/bin/bash

# Script para iniciar la aplicación con Docker Compose

set -e

echo "=========================================="
echo "API Gestión Franquicias - Docker Startup"
echo "=========================================="
echo ""

# Color codes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Verificar Docker
if ! command -v docker &> /dev/null; then
    log_error "Docker no está instalado"
    exit 1
fi

if ! command -v docker-compose &> /dev/null; then
    log_error "Docker Compose no está instalado"
    exit 1
fi

# Parar contenedores anteriores
log_info "Deteniendo contenedores anteriores..."
docker-compose down || true

# Construir e iniciar
log_info "Construyendo e iniciando contenedores..."
docker-compose up --build

# Al salir
log_info "Contenedores detenidos"
