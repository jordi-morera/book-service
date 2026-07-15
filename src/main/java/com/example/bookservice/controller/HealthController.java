package com.example.bookservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

/**
 * Endpoint publico de ejemplo (ver SecurityConfig: /api/v1/public/** esta permitido sin autenticacion).
 */
@RestController
@Tag(name = "Public", description = "Endpoints publicos, no requieren autenticacion")
public class HealthController {

    @GetMapping("/api/v1/public/status")
    @Operation(summary = "Comprueba que el servicio esta activo (sin autenticacion)")
    public Map<String, Object> status() {
        return Map.of(
                "status", "UP",
                "timestamp", Instant.now().toString()
        );
    }
}
