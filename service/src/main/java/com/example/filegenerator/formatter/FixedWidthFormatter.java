package com.example.filegenerator.formatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Formats records into fixed-width lines using a list of {@link FieldSpec}.
 * Supports truncation and right-padding (left alignment) or left-padding (right alignment).
 */
@Component
public class FixedWidthFormatter {
    private static final Logger log = LoggerFactory.getLogger(FixedWidthFormatter.class);

    public FixedWidthFormatter() { }

    /**
     * Format a single record into a fixed-width line according to the provided specs.
     * @param record map of field name to value
     * @param specs ordered list of FieldSpec describing each field
     * @return formatted fixed-width line (no trailing newline)
     */
    public String format(Map<String, ?> record, List<FieldSpec> specs) {
        Objects.requireNonNull(record, "record");
        Objects.requireNonNull(specs, "specs");

        log.debug("Formatting record with {} specs", specs.size());

        StringBuilder sb = new StringBuilder();
        for (FieldSpec spec : specs) {
            Object raw = record.get(spec.getName());
            String value = raw == null ? "" : raw.toString();
            sb.append(formatValue(value, spec));
        }

        String line = sb.toString();
        log.debug("Formatted line='{}'", line);
        return line;
    }

    private String formatValue(String value, FieldSpec spec) {
        int width = spec.getWidth();
        if (width == 0) return ""; // zero-width field

        if (value.length() > width) {
            if (spec.isTruncate()) {
                log.warn("Truncating field '{}' value '{}' to width {}", spec.getName(), value, width);
                return value.substring(0, width);
            } else {
                log.error("Value for field '{}' exceeds width {} and truncation is disabled", spec.getName(), width);
                throw new IllegalArgumentException(
                    "Value for field '" + spec.getName() + "' exceeds width " + width + ": '" + value + "'");
            }
        }

        int padLen = width - value.length();
        if (padLen == 0) return value;

        char pad = spec.getPadChar();
        StringBuilder sb = new StringBuilder(width);
        if (spec.isPadRight()) {
            sb.append(value);
            for (int i = 0; i < padLen; i++) sb.append(pad);
        } else {
            for (int i = 0; i < padLen; i++) sb.append(pad);
            sb.append(value);
        }
        return sb.toString();
    }
}
