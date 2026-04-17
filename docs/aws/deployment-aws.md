# Despliegue en AWS (ECS Fargate + ALB)

Esta guía resume el flujo para publicar la API en AWS usando ECR + ECS.

## Requisitos

- AWS CLI configurado (`aws configure`)
- Docker
- Permisos para ECR, ECS, IAM, ELBv2 y CloudWatch

## 1) Construir y publicar imagen en ECR

```bash
export AWS_REGION=us-east-1
export AWS_ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)
export ECR_REPO=franquicias-api

aws ecr create-repository --repository-name $ECR_REPO --region $AWS_REGION || true
aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com

docker build -t $ECR_REPO:latest .
docker tag $ECR_REPO:latest $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$ECR_REPO:latest
docker push $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$ECR_REPO:latest
```

## 2) Actualizar ECS con la nueva imagen

Flujo recomendado:

1. Obtener el `digest` de `latest` en ECR.
2. Registrar una nueva task definition usando la imagen por digest.
3. Forzar despliegue del servicio.
4. Esperar estabilidad y validar endpoints.

## 3) Verificar API en ALB

```bash
curl http://<alb-dns>/api
curl http://<alb-dns>/api/franquicias
```

## 4) Limpieza de imágenes en ECR

El repositorio tiene policy de lifecycle para evitar acumulación excesiva:

- Mantener 5 imágenes `latest`.
- Mantener 3 imágenes `untagged`.

Puedes ajustar estos valores según tu estrategia de releases.
