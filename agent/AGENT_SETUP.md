# AI Agent Setup Guide

## Overview

This directory contains configuration and setup files for AI agents working on the file-generator-platform project.

---

## Project Context

**Project Name:** file-generator-platform
**Primary Goal:** Enterprise File Generation Platform with CI/CD learning
**Current Branch:** `feature/tests`
**Tech Stack:**
- Java 17+
- Spring Boot 3.2.4
- Gradle 8.6
- JUnit 5
- Mockito 5.3.1

---

## Project Structure

```
file-generator-platform/
├── service/                          # Main application module
│   ├── src/main/java/                # Source code
│   │   └── com/example/filegenerator/
│   │       ├── controller/           # REST controllers
│   │       ├── service/              # Business logic
│   │       ├── formatter/            # File formatting logic
│   │       ├── model/                # DTOs & models
│   │       ├── exception/            # Exception handling
│   │       └── util/                 # Utilities
│   ├── src/test/java/                # Unit tests (85 tests)
│   │   └── com/example/filegenerator/
│   │       ├── controller/           # Controller tests (36 tests)
│   │       ├── service/              # Service tests (12 tests)
│   │       └── formatter/            # Formatter tests (37 tests)
│   └── build.gradle                  # Gradle configuration
│
├── docs/                             # Documentation
│   ├── README.md                     # Doc index & overview
│   ├── TEST_IMPLEMENTATION_SUMMARY.md # Detailed test summary
│   ├── TESTS_QUICK_START.md          # Quick reference
│   └── TEST_CODE_EXAMPLES.md         # Code patterns
│
├── agent/                            # AI Agent resources
│   └── AGENT_SETUP.md               # This file
│
├── build.gradle                      # Root Gradle config
├── README.md                         # Project README
└── settings.gradle                   # Gradle settings
```

---

## For AI Agents: Key Information

### Build & Test Commands

```bash
# Full path context
cd "C:\Users\sekha\spring projects\file-generator-platform"

# Run all tests
./gradlew test --no-daemon

# Run specific test class
./gradlew test --tests FixedWidthFormatterTest --no-daemon

# Clean rebuild
./gradlew clean test --no-daemon

# View test results
# File: service/build/reports/tests/test/index.html
```

### Core Components (Main Source)

1. **FieldSpec** - `service/src/main/java/com/example/filegenerator/formatter/FieldSpec.java`
   - Builder pattern
   - Field specification for fixed-width formatting
   
2. **FixedWidthFormatter** - `service/src/main/java/com/example/filegenerator/formatter/FixedWidthFormatter.java`
   - Spring component
   - Formats records into fixed-width lines
   
3. **ReportService** - `service/src/main/java/com/example/filegenerator/service/ReportService.java`
   - Spring service
   - Uses FixedWidthFormatter
   
4. **ReportController** - `service/src/main/java/com/example/filegenerator/controller/ReportController.java`
   - REST controller
   - POST endpoint: `/reports/fixed-width`
   
5. **ReportRequest** - `service/src/main/java/com/example/filegenerator/model/ReportRequest.java`
   - DTO with validation
   - Fields: name, age, city
   
6. **GlobalExceptionHandler** - `service/src/main/java/com/example/filegenerator/exception/GlobalExceptionHandler.java`
   - Spring error handler
   
7. **FileWriterUtil** - `service/src/main/java/com/example/filegenerator/util/FileWriterUtil.java`
   - Utility for file operations

### Test Classes (Test Source)

1. **FixedWidthFormatterTest** (37 tests)
   - Location: `service/src/test/java/com/example/filegenerator/formatter/FixedWidthFormatterTest.java`
   - Tests: Success cases, edge cases, special characters, builder config
   
2. **ReportServiceTest** (12 tests)
   - Location: `service/src/test/java/com/example/filegenerator/service/ReportServiceTest.java`
   - Tests: Core functionality, edge cases, mocking with Mockito
   
3. **ReportControllerTest** (36 tests)
   - Location: `service/src/test/java/com/example/filegenerator/controller/ReportControllerTest.java`
   - Tests: Request handling, validation, MockMvc integration

---

## Dependencies

```groovy
// Main Dependencies
- org.springframework.boot:spring-boot-starter-web:3.2.4
- org.springframework.boot:spring-boot-starter-validation:3.2.4
- org.slf4j:slf4j-api:2.0.7

// Test Dependencies
- org.springframework.boot:spring-boot-starter-test:3.2.4
- org.mockito:mockito-core:5.3.1
- org.mockito:mockito-junit-jupiter:5.3.1
```

---

## Common Tasks for AI Agents

### Task 1: Run All Tests
```bash
cd "C:\Users\sekha\spring projects\file-generator-platform"
./gradlew test --no-daemon
# Expected: BUILD SUCCESSFUL
```

### Task 2: Add New Test Method
Location: `service/src/test/java/com/example/filegenerator/formatter/FixedWidthFormatterTest.java`

Template:
```java
@Test
@DisplayName("shouldDoSomethingWhenCondition")
void shouldDoSomethingWhenCondition() {
    // Arrange
    
    // Act
    
    // Assert
}
```

### Task 3: Run Specific Test
```bash
./gradlew test --tests FixedWidthFormatterTest --no-daemon
./gradlew test --tests ReportServiceTest --no-daemon
./gradlew test --tests ReportControllerTest --no-daemon
```

### Task 4: Add New Feature Test Coverage
1. Create test method following naming convention: `shouldDoXWhenY()`
2. Use Arrange-Act-Assert structure
3. Place in appropriate nested test class
4. Run: `./gradlew test --no-daemon`

### Task 5: View Test Report
```
service/build/reports/tests/test/index.html
```

---

## Testing Patterns

### Pattern 1: Unit Test (Formatter)
```java
@Test
@DisplayName("shouldFormatRecordSuccessfully")
void shouldFormatRecordSuccessfully() {
    // Arrange
    FieldSpec spec = FieldSpec.builder("name", 10).build();
    Map<String, ?> record = Map.of("name", "John");
    
    // Act
    String result = formatter.format(record, List.of(spec));
    
    // Assert
    assertEquals("John      ", result);
}
```

### Pattern 2: Mocked Service Test
```java
@Test
@DisplayName("shouldCallFormatterWithCorrectData")
void shouldCallFormatterWithCorrectData() {
    // Arrange
    when(mockFormatter.format(any(), anyList())).thenReturn("output");
    
    // Act
    reportService.generateFixedWidthReport(request);
    
    // Assert
    verify(mockFormatter).format(any(), anyList());
}
```

### Pattern 3: Controller Test
```java
@Test
@DisplayName("shouldReturnOkForValidRequest")
void shouldReturnOkForValidRequest() throws Exception {
    // Arrange
    when(reportService.generateFixedWidthReport(any())).thenReturn("output");
    
    // Act & Assert
    mockMvc.perform(post("/reports/fixed-width")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());
}
```

---

## Key Notes for AI Agents

✅ **Do:**
- Follow Arrange-Act-Assert structure
- Use clean test names (shouldDoXWhenY)
- Nest related test classes with @Nested
- Keep tests focused and independent
- Use meaningful assertions

❌ **Don't:**
- Mix multiple assertions per test method
- Use generic test names like test1(), testFormat()
- Create dependencies between tests
- Test implementation details instead of behavior
- Over-complicate test setup

---

## Git Workflow

```bash
# Current branch
git branch
# Output: feature/tests

# Commit changes
git add service/src/test/java/...
git commit -m "feat: add tests for [component]"

# Push to feature branch
git push origin feature/tests
```

---

## Build Status

- **Last Build:** SUCCESS ✅
- **Test Count:** 85
- **Test Status:** ALL PASSING ✅
- **Build Time:** ~6-10 seconds

---

## Documentation References

- 📄 [Test Implementation Summary](../docs/TEST_IMPLEMENTATION_SUMMARY.md)
- 🚀 [Quick Start Guide](../docs/TESTS_QUICK_START.md)
- 💡 [Code Examples](../docs/TEST_CODE_EXAMPLES.md)
- 📖 [Project README](../README.md)

---

## Support & Troubleshooting

### Tests won't compile?
```bash
./gradlew clean build
```

### Tests won't run?
```bash
./gradlew test -i --no-daemon
```

### Check Java version
```bash
java -version
# Needs: Java 17+
```

### View detailed test output
```bash
./gradlew test -i --no-daemon
```

---

## Useful File Paths (Absolute)

```
C:\Users\sekha\spring projects\file-generator-platform\
├── service\build.gradle                                  (Gradle config)
├── service\src\main\java\com\example\filegenerator\     (Source code)
├── service\src\test\java\com\example\filegenerator\     (Test code)
├── service\build\reports\tests\test\index.html          (Test report)
├── docs\                                                 (Documentation)
└── agent\                                                (Agent resources)
```

---

## Next Steps

1. ✅ Review test structure in `service/src/test/java/`
2. ✅ Run tests: `./gradlew test --no-daemon`
3. ✅ View documentation in `docs/`
4. ✅ Make enhancements as needed
5. ✅ Ensure all tests pass before committing


