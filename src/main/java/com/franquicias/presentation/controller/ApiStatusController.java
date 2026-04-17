package com.franquicias.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.Map;

/**
 * Endpoint base para verificar disponibilidad de la API.
 */
@RestController
public class ApiStatusController {

    /**
     * GET /api
     */
    @GetMapping({"", "/"})
    public Mono<ResponseEntity<Map<String, Object>>> estadoApi() {
        return Mono.just(ResponseEntity.ok(Map.of(
                "mensaje", "Franquicias API funcionando",
                "status", "ok",
                "version", "1.0.0",
                "timestamp", OffsetDateTime.now().toString()
        )));
    }
}