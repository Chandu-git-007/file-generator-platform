package com.example.filegenerator.service;

import com.example.filegenerator.formatter.FieldSpec;
import com.example.filegenerator.formatter.FixedWidthFormatter;
import com.example.filegenerator.model.ReportRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {
    private static final Logger log = LoggerFactory.getLogger(ReportService.class);

    private final FixedWidthFormatter formatter;

    public ReportService(FixedWidthFormatter formatter) {
        this.formatter = formatter;
    }

    public String generateFixedWidthReport(ReportRequest request) {
        log.debug("Preparing field specs for request: name='{}' age='{}' city='{}'",
                request.getName(), request.getAge(), request.getCity());

        List<FieldSpec> specs = List.of(
                FieldSpec.builder("name", 10).padRight(true).truncate(true).build(),
                FieldSpec.builder("age", 2).padRight(true).truncate(true).build(),
                FieldSpec.builder("city", 2).padRight(true).truncate(true).build()
        );

        String line = formatter.format(request.toMap(), specs);
        log.debug("Formatted line='{}'", line);
        return line;
    }
}
