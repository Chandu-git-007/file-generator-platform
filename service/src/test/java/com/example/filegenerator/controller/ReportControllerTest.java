package com.example.filegenerator.controller;

import com.example.filegenerator.model.ReportRequest;
import com.example.filegenerator.service.ReportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReportController.class)
@DisplayName("ReportController")
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    @Autowired
    private ObjectMapper objectMapper;

    private ReportRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new ReportRequest("John Doe", 30, "New York");
    }

    @Nested
    @DisplayName("POST /reports/fixed-width")
    class CreateFixedWidthReport {

        @Test
        @DisplayName("shouldReturnOkWhenRequestIsValid")
        void shouldReturnOkWhenRequestIsValid() throws Exception {
            // Arrange
            String expectedOutput = "John Doe  30NY";
            when(reportService.generateFixedWidthReport(any(ReportRequest.class)))
                    .thenReturn(expectedOutput);

            // Act & Assert
            mockMvc.perform(post("/reports/fixed-width")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isOk())
                    .andExpect(content().string(expectedOutput))
                    .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"));
        }

        @Test
        @DisplayName("shouldReturnFormattedReportInPlainText")
        void shouldReturnFormattedReportInPlainText() throws Exception {
            // Arrange
            String formattedReport = "Alice     28SF";
            when(reportService.generateFixedWidthReport(any(ReportRequest.class)))
                    .thenReturn(formattedReport);

            ReportRequest request = new ReportRequest("Alice", 28, "SF");

            // Act & Assert
            mockMvc.perform(post("/reports/fixed-width")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"))
                    .andExpect(content().string(formattedReport));
        }

        @Test
        @DisplayName("shouldCallReportServiceWithRequestData")
        void shouldCallReportServiceWithRequestData() throws Exception {
            // Arrange
            when(reportService.generateFixedWidthReport(any(ReportRequest.class)))
                    .thenReturn("output");

            // Act
            mockMvc.perform(post("/reports/fixed-width")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isOk());

            // Assert - Service was called with any ReportRequest
            org.mockito.Mockito.verify(reportService)
                    .generateFixedWidthReport(any(ReportRequest.class));
        }

        @Test
        @DisplayName("shouldAcceptValidRequestWithAllFields")
        void shouldAcceptValidRequestWithAllFields() throws Exception {
            // Arrange
            ReportRequest request = new ReportRequest("Jane Smith", 45, "Boston");
            when(reportService.generateFixedWidthReport(any(ReportRequest.class)))
                    .thenReturn("Jane Smith45Boston");

            // Act & Assert
            mockMvc.perform(post("/reports/fixed-width")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Jane Smith45Boston"));
        }
    }

    @Nested
    @DisplayName("POST /reports/fixed-width - Validation")
    class ValidationTests {

        @Test
        @DisplayName("shouldReturnBadRequestWhenNameIsBlank")
        void shouldReturnBadRequestWhenNameIsBlank() throws Exception {
            // Arrange
            ReportRequest request = new ReportRequest("", 25, "City");

            // Act & Assert
            mockMvc.perform(post("/reports/fixed-width")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("shouldReturnBadRequestWhenNameIsNull")
        void shouldReturnBadRequestWhenNameIsNull() throws Exception {
            // Arrange
            ReportRequest request = new ReportRequest(null, 25, "City");

            // Act & Assert
            mockMvc.perform(post("/reports/fixed-width")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("shouldReturnBadRequestWhenAgeIsNull")
        void shouldReturnBadRequestWhenAgeIsNull() throws Exception {
            // Arrange
            String payload = "{\"name\": \"Test\", \"age\": null, \"city\": \"City\"}";

            // Act & Assert
            mockMvc.perform(post("/reports/fixed-width")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payload))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("shouldReturnBadRequestWhenAgeIsNegative")
        void shouldReturnBadRequestWhenAgeIsNegative() throws Exception {
            // Arrange
            ReportRequest request = new ReportRequest("Test", -5, "City");

            // Act & Assert
            mockMvc.perform(post("/reports/fixed-width")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("shouldReturnBadRequestWhenCityIsBlank")
        void shouldReturnBadRequestWhenCityIsBlank() throws Exception {
            // Arrange
            ReportRequest request = new ReportRequest("Test", 25, "");

            // Act & Assert
            mockMvc.perform(post("/reports/fixed-width")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("shouldReturnBadRequestWhenCityIsNull")
        void shouldReturnBadRequestWhenCityIsNull() throws Exception {
            // Arrange
            ReportRequest request = new ReportRequest("Test", 25, null);

            // Act & Assert
            mockMvc.perform(post("/reports/fixed-width")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("shouldReturnBadRequestWhenMissingNameField")
        void shouldReturnBadRequestWhenMissingNameField() throws Exception {
            // Arrange
            String payload = "{\"age\": 30, \"city\": \"City\"}";

            // Act & Assert
            mockMvc.perform(post("/reports/fixed-width")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payload))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("shouldReturnBadRequestWhenMissingAgeField")
        void shouldReturnBadRequestWhenMissingAgeField() throws Exception {
            // Arrange
            String payload = "{\"name\": \"Test\", \"city\": \"City\"}";

            // Act & Assert
            mockMvc.perform(post("/reports/fixed-width")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payload))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("shouldReturnBadRequestWhenMissingCityField")
        void shouldReturnBadRequestWhenMissingCityField() throws Exception {
            // Arrange
            String payload = "{\"name\": \"Test\", \"age\": 30}";

            // Act & Assert
            mockMvc.perform(post("/reports/fixed-width")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payload))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("shouldReturnBadRequestWhenRequestBodyIsEmpty")
        void shouldReturnBadRequestWhenRequestBodyIsEmpty() throws Exception {
            // Act & Assert
            mockMvc.perform(post("/reports/fixed-width")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("shouldReturnBadRequestWhenJsonIsMalformed")
        void shouldReturnBadRequestWhenJsonIsMalformed() throws Exception {
            // Act & Assert
            mockMvc.perform(post("/reports/fixed-width")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{invalid json"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("POST /reports/fixed-width - Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("shouldReturnOkWithMaxAgeValue")
        void shouldReturnOkWithMaxAgeValue() throws Exception {
            // Arrange
            ReportRequest request = new ReportRequest("Test", Integer.MAX_VALUE, "City");
            when(reportService.generateFixedWidthReport(any(ReportRequest.class)))
                    .thenReturn("output");

            // Act & Assert
            mockMvc.perform(post("/reports/fixed-width")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("shouldReturnOkWithAgeZero")
        void shouldReturnOkWithAgeZero() throws Exception {
            // Arrange
            ReportRequest request = new ReportRequest("Baby", 0, "City");
            when(reportService.generateFixedWidthReport(any(ReportRequest.class)))
                    .thenReturn("output");

            // Act & Assert
            mockMvc.perform(post("/reports/fixed-width")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("shouldReturnOkWithVeryLongNameValue")
        void shouldReturnOkWithVeryLongNameValue() throws Exception {
            // Arrange
            String longName = "A".repeat(1000);
            ReportRequest request = new ReportRequest(longName, 30, "City");
            when(reportService.generateFixedWidthReport(any(ReportRequest.class)))
                    .thenReturn("output");

            // Act & Assert
            mockMvc.perform(post("/reports/fixed-width")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("shouldReturnOkWithSpecialCharactersInName")
        void shouldReturnOkWithSpecialCharactersInName() throws Exception {
            // Arrange
            ReportRequest request = new ReportRequest("John-Paul O'Brien", 35, "City");
            when(reportService.generateFixedWidthReport(any(ReportRequest.class)))
                    .thenReturn("output");

            // Act & Assert
            mockMvc.perform(post("/reports/fixed-width")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("shouldReturnOkWithUnicodeCharactersInCity")
        void shouldReturnOkWithUnicodeCharactersInCity() throws Exception {
            // Arrange
            ReportRequest request = new ReportRequest("Test", 30, "São Paulo");
            when(reportService.generateFixedWidthReport(any(ReportRequest.class)))
                    .thenReturn("output");

            // Act & Assert
            mockMvc.perform(post("/reports/fixed-width")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("shouldReturnOkWithWhitespaceOnlyInNameWhenNotAllowed")
        void shouldReturnBadRequestWithWhitespaceOnlyInName() throws Exception {
            // Arrange
            ReportRequest request = new ReportRequest("   ", 30, "City");

            // Act & Assert
            mockMvc.perform(post("/reports/fixed-width")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("POST /reports/fixed-width - Response Content-Type")
    class ResponseContentType {

        @Test
        @DisplayName("shouldReturnPlainTextContentType")
        void shouldReturnPlainTextContentType() throws Exception {
            // Arrange
            when(reportService.generateFixedWidthReport(any(ReportRequest.class)))
                    .thenReturn("plain text output");

            // Act & Assert
            mockMvc.perform(post("/reports/fixed-width")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"));
        }

        @Test
        @DisplayName("shouldNotReturnJsonContentType")
        void shouldNotReturnJsonContentType() throws Exception {
            // Arrange
            when(reportService.generateFixedWidthReport(any(ReportRequest.class)))
                    .thenReturn("text output");

            // Act & Assert
            mockMvc.perform(post("/reports/fixed-width")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isOk())
                    .andExpect(result -> {
                        String contentType = result.getResponse().getContentType();
                        assert !contentType.contains(MediaType.APPLICATION_JSON_VALUE);
                    });
        }
    }
}






