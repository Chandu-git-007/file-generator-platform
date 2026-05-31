package com.example.filegenerator.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Simple utility to write text reports to disk.
 */
public final class FileWriterUtil {
    private static final Logger log = LoggerFactory.getLogger(FileWriterUtil.class);

    private FileWriterUtil() { }

    public static Path writeToFile(String content, Path path) throws IOException {
        if (path == null) throw new IllegalArgumentException("path is required");
        Files.createDirectories(path.getParent());
        Files.writeString(path, content);
        log.info("Wrote {} bytes to {}", content == null ? 0 : content.length(), path.toString());
        return path;
    }
}
