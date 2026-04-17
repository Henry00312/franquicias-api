variable "docker_host" {
  description = "Host del Docker daemon"
  type        = string
  default     = "unix:///var/run/docker.sock"
}

variable "mongo_username" {
  description = "Usuario de MongoDB"
  type        = string
}

variable "mongo_password" {
  description = "Contraseña de MongoDB"
  type        = string
  sensitive   = true
}

variable "mongo_database" {
  description = "Base de datos de MongoDB"
  type        = string
  default     = "franquicias"
}

variable "api_port" {
  description = "Puerto de la API"
  type        = number
  default     = 8080
}

variable "mongo_port" {
  description = "Puerto de MongoDB"
  type        = number
  default     = 27017
}

variable "network_name" {
  description = "Nombre de la red Docker"
  type        = string
  default     = "franquicias-network"
}