variable "aws_region" {
  description = "Region AWS donde desplegar"
  type        = string
  default     = "us-east-1"
}

variable "project_name" {
  description = "Prefijo de nombres de recursos"
  type        = string
  default     = "franquicias-api"
}

variable "container_image" {
  description = "Imagen del contenedor de la API (ECR o Docker Hub)"
  type        = string
}

variable "container_port" {
  description = "Puerto interno de la aplicacion"
  type        = number
  default     = 8080
}

variable "desired_count" {
  description = "Cantidad de tareas ECS"
  type        = number
  default     = 1
}

variable "task_cpu" {
  description = "CPU para la tarea ECS (256, 512, 1024, etc.)"
  type        = number
  default     = 256
}

variable "task_memory" {
  description = "Memoria para la tarea ECS en MB (512, 1024, 2048, etc.)"
  type        = number
  default     = 512
}

variable "mongodb_uri" {
  description = "URI de MongoDB/Atlas usada por la API"
  type        = string
  sensitive   = true
}

variable "environment" {
  description = "Ambiente de despliegue"
  type        = string
  default     = "dev"
}
