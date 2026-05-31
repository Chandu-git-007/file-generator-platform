package com.example.filegenerator.service;

import com.example.filegenerator.formatter.FieldSpec;
import com.example.filegenerator.formatter.FixedWidthFormatter;
import com.example.filegenerator.model.ReportRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReportService")
class ReportServiceTest {

    @Mock
    private FixedWidthFormatter mockFormatter;

    private ReportService reportService;

    @BeforeEach
    void setUp() {
        reportService = new ReportService(mockFormatter);
    }

    @Nested
    @DisplayName("generateFixedWidthReport()")
    class GenerateFixedWidthReport {

        @Test
        @DisplayName("shouldGenerateFixedWidthReportSuccessfully")
        void shouldGenerateFixedWidthReportSuccessfully() {
            // Arrange
            ReportRequest request = new ReportRequest("John Doe", 30, "New York");
            String expectedOutput = "John Doe  30NY";

            when(mockFormatter.format(any(), anyList()))
                    .thenReturn(expectedOutput);

            // Act
            String result = reportService.generateFixedWidthReport(request);

            // Assert
            assertNotNull(result);
            assertEquals(expectedOutput, result);
        }

        @Test
        @DisplayName("shouldInvokeFormatterWithCorrectRecord")
        void shouldInvokeFormatterWithCorrectRecord() {
            // Arrange
            ReportRequest request = new ReportRequest("Jane Smith", 25, "Boston");
            when(mockFormatter.format(any(), anyList()))
                    .thenReturn("formatted");

            // Act
            reportService.generateFixedWidthReport(request);

            // Assert
            ArgumentCaptor<Map<String, Object>> recordCaptor = ArgumentCaptor.forClass(Map.class);
            verify(mockFormatter).format(recordCaptor.capture(), anyList());

            Map<String, Object> capturedRecord = recordCaptor.getValue();
            assertEquals("Jane Smith", capturedRecord.get("name"));
            assertEquals(25, capturedRecord.get("age"));
            assertEquals("Boston", capturedRecord.get("city"));
        }

        @Test
        @DisplayName("shouldInvokeFormatterWithCorrectFieldSpecs")
        void shouldInvokeFormatterWithCorrectFieldSpecs() {
            // Arrange
            ReportRequest request = new ReportRequest("Test", 20, "City");
            when(mockFormatter.format(any(), anyList()))
                    .thenReturn("output");

            // Act
            reportService.generateFixedWidthReport(request);

            // Assert
            ArgumentCaptor<List> specsCaptor = ArgumentCaptor.forClass(List.class);
            verify(mockFormatter).format(any(), specsCaptor.capture());

            List<FieldSpec> specs = specsCaptor.getValue();
            assertEquals(3, specs.size());

            // Verify field spec for "name"
            FieldSpec nameSpec = specs.get(0);
            assertEquals("name", nameSpec.getName());
            assertEquals(10, nameSpec.getWidth());
            assertEquals(true, nameSpec.isPadRight());
            assertEquals(true, nameSpec.isTruncate());

            // Verify field spec for "age"
            FieldSpec ageSpec = specs.get(1);
            assertEquals("age", ageSpec.getName());
            assertEquals(2, ageSpec.getWidth());

            // Verify field spec for "city"
            FieldSpec citySpec = specs.get(2);
            assertEquals("city", citySpec.getName());
            assertEquals(2, citySpec.getWidth());
        }

        @Test
        @DisplayName("shouldReturnFormatterOutput")
        void shouldReturnFormatterOutput() {
            // Arrange
            ReportRequest request = new ReportRequest("Alice", 28, "SF");
            String formattedReport = "Alice     28SF";

            when(mockFormatter.format(any(), anyList()))
                    .thenReturn(formattedReport);

            // Act
            String result = reportService.generateFixedWidthReport(request);

            // Assert
            assertEquals(formattedReport, result);
        }
    }

    @Nested
    @DisplayName("generateFixedWidthReport() - Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("shouldHandleRequestWithEmptyValues")
        void shouldHandleRequestWithEmptyValues() {
            // Arrange
            ReportRequest request = new ReportRequest("", 0, "");
            when(mockFormatter.format(any(), anyList()))
                    .thenReturn("          0 ");

            // Act
            String result = reportService.generateFixedWidthReport(request);

            // Assert
            assertNotNull(result);
            verify(mockFormatter).format(any(), anyList());
        }

        @Test
        @DisplayName("shouldHandleRequestWithVeryLongValues")
        void shouldHandleRequestWithVeryLongValues() {
            // Arrange
            String longName = "VeryVeryVeryLongNameThatExceedsWidth";
            ReportRequest request = new ReportRequest(longName, 99, "VeryLongCityName");
            when(mockFormatter.format(any(), anyList()))
                    .thenReturn("truncated");

            // Act
            String result = reportService.generateFixedWidthReport(request);

            // Assert
            assertNotNull(result);
            verify(mockFormatter).format(any(), anyList());
        }

        @Test
        @DisplayName("shouldHandleRequestWithNullValues")
        void shouldHandleRequestWithNullValues() {
            // Arrange
            ReportRequest request = new ReportRequest(null, null, null);
            when(mockFormatter.format(any(), anyList()))
                    .thenReturn("   0 ");

            // Act
            String result = reportService.generateFixedWidthReport(request);

            // Assert
            assertNotNull(result);
        }

        @Test
        @DisplayName("shouldCreateConsistentFieldSpecsOnEachCall")
        void shouldCreateConsistentFieldSpecsOnEachCall() {
            // Arrange
            ReportRequest request1 = new ReportRequest("Person1", 25, "City1");
            ReportRequest request2 = new ReportRequest("Person2", 35, "City2");
            when(mockFormatter.format(any(), anyList()))
                    .thenReturn("output");

            // Act
            reportService.generateFixedWidthReport(request1);
            reportService.generateFixedWidthReport(request2);

            // Assert
            ArgumentCaptor<List> specsCaptor = ArgumentCaptor.forClass(List.class);
            verify(mockFormatter).format(any(), specsCaptor.capture());

            // Both calls should have the same field specs
            List<FieldSpec> specs1 = (List<FieldSpec>) specsCaptor.getAllValues().get(0);
            List<FieldSpec> specs2 = (List<FieldSpec>) specsCaptor.getAllValues().get(1);

            assertEquals(specs1.size(), specs2.size());
            for (int i = 0; i < specs1.size(); i++) {
                FieldSpec spec1 = specs1.get(i);
                FieldSpec spec2 = specs2.get(i);
                assertEquals(spec1.getName(), spec2.getName());
                assertEquals(spec1.getWidth(), spec2.getWidth());
            }
        }
    }

    @Nested
    @DisplayName("generateFixedWidthReport() - Integration Behavior")
    class IntegrationBehavior {

        @Test
        @DisplayName("shouldConvertRequestToMapCorrectly")
        void shouldConvertRequestToMapCorrectly() {
            // Arrange
            ReportRequest request = new ReportRequest("Bob", 45, "Miami");
            when(mockFormatter.format(any(), anyList()))
                    .thenReturn("result");

            // Act
            reportService.generateFixedWidthReport(request);

            // Assert
            ArgumentCaptor<Map> mapCaptor = ArgumentCaptor.forClass(Map.class);
            verify(mockFormatter).format(mapCaptor.capture(), anyList());

            Map<String, Object> map = mapCaptor.getValue();
            assertEquals("Bob", map.get("name"));
            assertEquals(45, map.get("age"));
            assertEquals("Miami", map.get("city"));
        }

        @Test
        @DisplayName("shouldMaintainFieldOrderAsNameAgeCityInSpecs")
        void shouldMaintainFieldOrderAsNameAgeCityInSpecs() {
            // Arrange
            ReportRequest request = new ReportRequest("Test", 30, "Loc");
            when(mockFormatter.format(any(), anyList()))
                    .thenReturn("output");

            // Act
            reportService.generateFixedWidthReport(request);

            // Assert
            ArgumentCaptor<List> specsCaptor = ArgumentCaptor.forClass(List.class);
            verify(mockFormatter).format(any(), specsCaptor.capture());

            List<FieldSpec> specs = specsCaptor.getValue();
            assertEquals("name", specs.get(0).getName());
            assertEquals("age", specs.get(1).getName());
            assertEquals("city", specs.get(2).getName());
        }
    }
}

