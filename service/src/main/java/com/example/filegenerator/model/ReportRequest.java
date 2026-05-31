package com.example.filegenerator.model;

import java.util.Map;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Simple DTO for report requests.
 */
public class ReportRequest {
    @NotBlank(message = "name is required")
    private String name;

    @NotNull(message = "age is required")
    @Min(value = 0, message = "age must be >= 0")
    private Integer age;

    @NotBlank(message = "city is required")
    private String city;

    public ReportRequest() {
    }

    public ReportRequest(String name, Integer age, String city) {
        this.name = name;
        this.age = age;
        this.city = city;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public Map<String, Object> toMap() {
        return Map.of(
            "name", name == null ? "" : name,
            "age", age == null ? "" : age,
            "city", city == null ? "" : city
        );
    }
}
