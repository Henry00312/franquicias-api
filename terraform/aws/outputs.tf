output "api_public_url" {
  description = "URL publica de la API"
  value       = "http://${aws_lb.api.dns_name}/api"
}

output "alb_dns_name" {
  description = "DNS del Application Load Balancer"
  value       = aws_lb.api.dns_name
}

output "ecs_cluster_name" {
  description = "Nombre del cluster ECS"
  value       = aws_ecs_cluster.main.name
}

output "ecs_service_name" {
  description = "Nombre del servicio ECS"
  value       = aws_ecs_service.api.name
}
