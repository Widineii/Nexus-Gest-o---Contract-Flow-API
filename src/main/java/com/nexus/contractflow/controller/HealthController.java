package com.nexus.contractflow.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

/**
 * Endpoint publico de health check usado por load balancers / PaaS (Render, etc.).
 * Sempre retorna 200 quando a aplicacao esta de pe.
 */
@RestController
@Hidden
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "nexus-contract-flow",
                "timestamp", Instant.now().toString()
        ));
    }
}
