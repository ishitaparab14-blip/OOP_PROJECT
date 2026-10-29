package com.campusfind.controller;

import com.campusfind.dto.DashboardStatsDto;
import com.campusfind.service.DashboardStatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final DashboardStatsService statsService;

    public StatsController(DashboardStatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping
    public ResponseEntity<DashboardStatsDto> getStats() {
        return ResponseEntity.ok(statsService.getLiveStatistics());
    }
}
