terraform {
  required_version = ">= 1.0"

  required_providers {
    docker = {
      source  = "kreuzwerker/docker"
      version = "~> 3.0"
    }
  }
}

provider "docker" {
  host = var.docker_host
}

# Red Docker
resource "docker_network" "franquicias_network" {
  name   = var.network_name
  driver = "bridge"
}

# Volumen para MongoDB
resource "docker_volume" "mongodb_data" {
  name = "franquicias_mongodb_data"
}

# Contenedor MongoDB
resource "docker_image" "mongodb" {
  name         = "mongo:7.0-alpine"
  keep_locally = false
  pull_triggers = ["mongo:7.0-alpine"]
}

resource "docker_container" "mongodb" {
  name           = "franquicias-mongodb"
  image          = docker_image.mongodb.image_id
  restart_policy = "unless-stopped"

  ports {
    internal = 27017
    external = var.mongo_port
  }

  env = [
    "MONGO_INITDB_ROOT_USERNAME=${var.mongo_username}",
    "MONGO_INITDB_ROOT_PASSWORD=${var.mongo_password}",
    "MONGO_INITDB_DATABASE=${var.mongo_database}"
  ]

  volumes {
    volume_name    = docker_volume.mongodb_data.name
    container_path = "/data/db"
  }

  volumes {
    host_path      = abspath("${path.module}/init-mongo.js")
    container_path = "/docker-entrypoint-initdb.d/init-mongo.js"
    read_only      = true
  }

  networks_advanced {
    name = docker_network.franquicias_network.name
  }

  healthcheck {
    test     = ["CMD", "mongosh", "--eval", "db.adminCommand('ping')"]
    interval = "10s"
    timeout  = "5s"
    retries  = 5
  }

  depends_on = [docker_network.franquicias_network]
}

# Imagen Docker de la API
resource "docker_image" "franquicias_api" {
  name         = "franquicias-api:latest"
  keep_locally = false

  build {
    context    = path.module
    dockerfile = "Dockerfile"
  }

  depends_on = [docker_container.mongodb]
}

# Contenedor de la API
resource "docker_container" "franquicias_api" {
  name           = "franquicias-api"
  image          = docker_image.franquicias_api.image_id
  restart_policy = "unless-stopped"

  ports {
    internal = 8080
    external = var.api_port
  }

  env = [
    "MONGO_USER=${var.mongo_username}",
    "MONGO_PASSWORD=${var.mongo_password}",
    "MONGO_HOST=mongodb",
    "MONGO_PORT=${var.mongo_port}",
    "MONGO_DATABASE=${var.mongo_database}",
    "SERVER_PORT=${var.api_port}"
  ]

  networks_advanced {
    name = docker_network.franquicias_network.name
  }

  healthcheck {
    test         = ["CMD", "curl", "-f", "http://localhost:8080/api/franquicias"]
    interval     = "30s"
    timeout      = "10s"
    retries      = 3
    start_period = "40s"
  }

  depends_on = [docker_container.mongodb]
}

# Outputs
output "api_url" {
  description = "URL de la API"
  value       = "http://localhost:${var.api_port}/api"
}

output "mongodb_connection" {
  description = "Cadena de conexión de MongoDB"
  value       = "mongodb://${var.mongo_username}:***@localhost:${var.mongo_port}/${var.mongo_database}?authSource=admin"
}

output "container_mongodb_id" {
  description = "ID del contenedor MongoDB"
  value       = docker_container.mongodb.id
}

output "container_api_id" {
  description = "ID del contenedor de la API"
  value       = docker_container.franquicias_api.id
}