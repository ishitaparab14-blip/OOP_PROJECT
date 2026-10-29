package com.campusfind.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    private final LocalDateTime startedAt = LocalDateTime.now();

    @GetMapping
    public ResponseEntity<Map<String, Object>> checkHealth() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "CampusFind Digital Lost and Found",
                "version", "1.0.0",
                "startedAt", startedAt,
                "currentTime", LocalDateTime.now()
        ));
    }
}
