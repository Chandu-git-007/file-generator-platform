package com.example.filegenerator.formatter;

import java.util.Objects;

/**
 * Specification for a single fixed-width field.
 */
public final class FieldSpec {
    private final String name;
    private final int width;
    private final char padChar;
    private final boolean padRight; // true -> right-pad (i.e., left-aligned)
    private final boolean truncate; // true -> truncate if value exceeds width

    private FieldSpec(String name, int width, char padChar, boolean padRight, boolean truncate) {
        this.name = Objects.requireNonNull(name, "name");
        if (width < 0) throw new IllegalArgumentException("width must be >= 0");
        this.width = width;
        this.padChar = padChar;
        this.padRight = padRight;
        this.truncate = truncate;
    }

    public String getName() { return name; }
    public int getWidth() { return width; }
    public char getPadChar() { return padChar; }
    public boolean isPadRight() { return padRight; }
    public boolean isTruncate() { return truncate; }

    public static Builder builder(String name, int width) {
        return new Builder(name, width);
    }

    public static final class Builder {
        private final String name;
        private final int width;
        private char padChar = ' ';
        private boolean padRight = true;
        private boolean truncate = true;

        public Builder(String name, int width) {
            this.name = name;
            this.width = width;
        }

        public Builder padChar(char padChar) { this.padChar = padChar; return this; }
        public Builder padRight(boolean padRight) { this.padRight = padRight; return this; }
        public Builder truncate(boolean truncate) { this.truncate = truncate; return this; }

        public FieldSpec build() { return new FieldSpec(name, width, padChar, padRight, truncate); }
    }
}
