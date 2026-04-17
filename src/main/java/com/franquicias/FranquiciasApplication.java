package com.franquicias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Aplicación principal de Spring Boot para la API de gestión de franquicias.
 * Implementa programación reactiva con Spring WebFlux y MongoDB reactivo.
 */
@SpringBootApplication
public class FranquiciasApplication {

    public static void main(String[] args) {
        SpringApplication.run(FranquiciasApplication.class, args);
    }
}
