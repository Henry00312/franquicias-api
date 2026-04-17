@echo off
REM Script para compilar, probar y empaquetar la aplicación en Windows

setlocal enabledelayedexpansion

echo.
echo ==========================================
echo API Gestion Franquicias - Build Script
echo ==========================================
echo.

REM Verificar requisitos
echo [INFO] Verificando requisitos...

where mvn >nul 2>nul
if errorlevel 1 (
    echo [ERROR] Maven no esta instalado
    exit /b 1
)

where java >nul 2>nul
if errorlevel 1 (
    echo [ERROR] Java no esta instalado
    exit /b 1
)

for /f "tokens=*" %%i in ('java -version 2^>^&1 ^| findstr /R "version"') do set JAVA_VERSION=%%i
echo [INFO] %JAVA_VERSION%

for /f "tokens=*" %%i in ('mvn -v ^| findstr /R "Apache"') do set MAVEN_VERSION=%%i
echo [INFO] %MAVEN_VERSION%
echo.

REM Limpiar proyecto
echo [INFO] Limpiando proyecto anterior...
call mvn clean
if errorlevel 1 goto error

REM Compilar
echo [INFO] Compilando proyecto...
call mvn compile
if errorlevel 1 goto error

REM Ejecutar tests
echo [INFO] Ejecutando tests unitarios...
call mvn test
if errorlevel 1 goto error

REM Empaquetar
echo [INFO] Empaquetando aplicacion...
call mvn package
if errorlevel 1 goto error

echo.
echo ==========================================
echo [INFO] Build completado exitosamente!
echo ==========================================
echo.
echo [INFO] JAR generado: target\franquicias-api-1.0.0.jar
echo.
echo Pasos siguientes:
echo 1. Docker Compose: docker-compose up --build
echo 2. Terraform: cd terraform ^& terraform apply
echo 3. Local: mvn spring-boot:run
echo.

goto end

:error
echo.
echo [ERROR] Hubo un error durante el build
echo.
exit /b 1

:end
endlocal
