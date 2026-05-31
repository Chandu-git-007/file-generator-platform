package com.example.filegenerator.formatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FixedWidthFormatter")
class FixedWidthFormatterTest {

    private FixedWidthFormatter formatter;

    @BeforeEach
    void setUp() {
        formatter = new FixedWidthFormatter();
    }

    @Nested
    @DisplayName("format() - Success Cases")
    class SuccessCases {

        @Test
        @DisplayName("shouldFormatFixedWidthRecordSuccessfully")
        void shouldFormatFixedWidthRecordSuccessfully() {
            // Arrange
            FieldSpec nameSpec = FieldSpec.builder("name", 10)
                    .padRight(true)
                    .padChar(' ')
                    .truncate(true)
                    .build();
            FieldSpec ageSpec = FieldSpec.builder("age", 3)
                    .padRight(true)
                    .padChar(' ')
                    .truncate(true)
                    .build();

            List<FieldSpec> specs = List.of(nameSpec, ageSpec);
            Map<String, ?> record = Map.of("name", "John", "age", 30);

            // Act
            String result = formatter.format(record, specs);

            // Assert
            assertEquals("John      30 ", result);
            assertEquals(13, result.length());
        }

        @Test
        @DisplayName("shouldFormatMultipleFieldsInCorrectOrder")
        void shouldFormatMultipleFieldsInCorrectOrder() {
            // Arrange
            List<FieldSpec> specs = List.of(
                    FieldSpec.builder("first", 5).padRight(true).build(),
                    FieldSpec.builder("second", 5).padRight(true).build(),
                    FieldSpec.builder("third", 5).padRight(true).build()
            );
            Map<String, ?> record = Map.of(
                    "first", "A",
                    "second", "B",
                    "third", "C"
            );

            // Act
            String result = formatter.format(record, specs);

            // Assert
            assertEquals("A    B    C    ", result);
            assertEquals(15, result.length());
        }

        @Test
        @DisplayName("shouldApplyCorrectPadding")
        void shouldApplyCorrectPadding() {
            // Arrange
            FieldSpec spec = FieldSpec.builder("value", 8)
                    .padRight(true)
                    .padChar('*')
                    .build();
            Map<String, ?> record = Map.of("value", "test");

            // Act
            String result = formatter.format(record, List.of(spec));

            // Assert
            assertEquals("test****", result);
        }

        @Test
        @DisplayName("shouldLeftPadWhenPadRightIsFalse")
        void shouldLeftPadWhenPadRightIsFalse() {
            // Arrange
            FieldSpec spec = FieldSpec.builder("value", 8)
                    .padRight(false)
                    .padChar('0')
                    .build();
            Map<String, ?> record = Map.of("value", "123");

            // Act
            String result = formatter.format(record, List.of(spec));

            // Assert
            assertEquals("00000123", result);
        }

        @Test
        @DisplayName("shouldReturnExactValueWhenLengthEqualsWidth")
        void shouldReturnExactValueWhenLengthEqualsWidth() {
            // Arrange
            FieldSpec spec = FieldSpec.builder("value", 5).build();
            Map<String, ?> record = Map.of("value", "exact");

            // Act
            String result = formatter.format(record, List.of(spec));

            // Assert
            assertEquals("exact", result);
        }

        @Test
        @DisplayName("shouldFormatZeroWidthField")
        void shouldFormatZeroWidthField() {
            // Arrange
            FieldSpec spec = FieldSpec.builder("value", 0).build();
            Map<String, ?> record = Map.of("value", "test");

            // Act
            String result = formatter.format(record, List.of(spec));

            // Assert
            assertEquals("", result);
        }
    }

    @Nested
    @DisplayName("format() - Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("shouldTruncateValueWhenLengthExceedsLimitWithTruncateEnabled")
        void shouldTruncateValueWhenLengthExceedsLimitWithTruncateEnabled() {
            // Arrange
            FieldSpec spec = FieldSpec.builder("name", 5)
                    .truncate(true)
                    .build();
            Map<String, ?> record = Map.of("name", "VeryLongName");

            // Act
            String result = formatter.format(record, List.of(spec));

            // Assert
            assertEquals("VeryL", result);
        }

        @Test
        @DisplayName("shouldThrowExceptionWhenValueExceedsLimitWithTruncateDisabled")
        void shouldThrowExceptionWhenValueExceedsLimitWithTruncateDisabled() {
            // Arrange
            FieldSpec spec = FieldSpec.builder("name", 5)
                    .truncate(false)
                    .build();
            Map<String, ?> record = Map.of("name", "VeryLongName");

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () ->
                    formatter.format(record, List.of(spec))
            );
        }

        @Test
        @DisplayName("shouldHandleNullValueAsEmptyString")
        void shouldHandleNullValueAsEmptyString() {
            // Arrange
            FieldSpec spec = FieldSpec.builder("value", 5)
                    .padRight(true)
                    .padChar(' ')
                    .build();
            Map<String, ?> record = Map.of("value", (Object) null);

            // Act
            String result = formatter.format(record, List.of(spec));

            // Assert
            assertEquals("     ", result);
        }

        @Test
        @DisplayName("shouldHandleEmptyStringValue")
        void shouldHandleEmptyStringValue() {
            // Arrange
            FieldSpec spec = FieldSpec.builder("value", 5)
                    .padRight(true)
                    .padChar('-')
                    .build();
            Map<String, ?> record = Map.of("value", "");

            // Act
            String result = formatter.format(record, List.of(spec));

            // Assert
            assertEquals("-----", result);
        }

        @Test
        @DisplayName("shouldHandleMissingFieldInRecord")
        void shouldHandleMissingFieldInRecord() {
            // Arrange
            FieldSpec spec = FieldSpec.builder("missing", 5)
                    .padRight(true)
                    .build();
            Map<String, ?> record = Map.of("other", "value");

            // Act
            String result = formatter.format(record, List.of(spec));

            // Assert
            assertEquals("     ", result);
        }

        @Test
        @DisplayName("shouldHandleNumericValueConversion")
        void shouldHandleNumericValueConversion() {
            // Arrange
            FieldSpec spec = FieldSpec.builder("age", 3)
                    .padRight(true)
                    .build();
            Map<String, ?> record = Map.of("age", 25);

            // Act
            String result = formatter.format(record, List.of(spec));

            // Assert
            assertEquals("25 ", result);
        }

        @Test
        @DisplayName("shouldThrowExceptionWhenRecordIsNull")
        void shouldThrowExceptionWhenRecordIsNull() {
            // Arrange
            List<FieldSpec> specs = List.of(
                    FieldSpec.builder("value", 5).build()
            );

            // Act & Assert
            assertThrows(NullPointerException.class, () ->
                    formatter.format(null, specs)
            );
        }

        @Test
        @DisplayName("shouldThrowExceptionWhenSpecsListIsNull")
        void shouldThrowExceptionWhenSpecsListIsNull() {
            // Arrange
            Map<String, ?> record = Map.of("value", "test");

            // Act & Assert
            assertThrows(NullPointerException.class, () ->
                    formatter.format(record, null)
            );
        }
    }

    @Nested
    @DisplayName("format() - Special Characters")
    class SpecialCharacters {

        @Test
        @DisplayName("shouldPreserveSpecialCharactersInValues")
        void shouldPreserveSpecialCharactersInValues() {
            // Arrange
            FieldSpec spec = FieldSpec.builder("text", 10)
                    .padRight(true)
                    .build();
            Map<String, ?> record = Map.of("text", "@#$%");

            // Act
            String result = formatter.format(record, List.of(spec));

            // Assert
            assertEquals("@#$%      ", result);
        }

        @Test
        @DisplayName("shouldHandleUnicodeCharacters")
        void shouldHandleUnicodeCharacters() {
            // Arrange
            FieldSpec spec = FieldSpec.builder("unicode", 10)
                    .padRight(true)
                    .build();
            Map<String, ?> record = Map.of("unicode", "café");

            // Act
            String result = formatter.format(record, List.of(spec));

            // Assert
            assertEquals("café      ", result);
        }
    }

    @Nested
    @DisplayName("format() - Builder Configuration")
    class BuilderConfiguration {

        @Test
        @DisplayName("shouldUseDefaultConfigurationFromBuilder")
        void shouldUseDefaultConfigurationFromBuilder() {
            // Arrange - Using default values: padChar=' ', padRight=true, truncate=true
            FieldSpec spec = FieldSpec.builder("value", 5).build();
            Map<String, ?> record = Map.of("value", "ab");

            // Act
            String result = formatter.format(record, List.of(spec));

            // Assert
            assertEquals("ab   ", result);
        }

        @Test
        @DisplayName("shouldFormatWithVariousPadCharacters")
        void shouldFormatWithVariousPadCharacters() {
            // Arrange
            List<Character> padChars = List.of('*', '.', '0', '-', '_');
            Map<String, ?> record = Map.of("val", "X");

            for (char padChar : padChars) {
                FieldSpec spec = FieldSpec.builder("val", 4)
                        .padChar(padChar)
                        .padRight(true)
                        .build();

                // Act
                String result = formatter.format(record, List.of(spec));

                // Assert
                assertEquals("X" + padChar + padChar + padChar, result);
            }
        }
    }
}















