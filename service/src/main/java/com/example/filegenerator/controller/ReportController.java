package com.example.filegenerator.controller;

import com.example.filegenerator.model.ReportRequest;
import com.example.filegenerator.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/reports")
@Validated
public class ReportController {
    private static final Logger log = LoggerFactory.getLogger(ReportController.class);

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping(path = "/fixed-width", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> createFixedWidthReport(@Valid @RequestBody ReportRequest request) {
        log.info("Received fixed-width report request for name='{}'", request.getName());
        String result = reportService.generateFixedWidthReport(request);
        return ResponseEntity.ok(result);
    }
}
