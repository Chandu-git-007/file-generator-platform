# Quick Start Guide - Running Tests

## TL;DR - Run Tests Now

```bash
cd file-generator-platform
./gradlew test
```

✅ **Expected Result:** BUILD SUCCESSFUL

---

## Available Test Commands

### Run All Tests
```bash
./gradlew test --no-daemon
```

### Run Specific Test Class
```bash
# Test the formatter
./gradlew test --tests FixedWidthFormatterTest --no-daemon

# Test the service
./gradlew test --tests ReportServiceTest --no-daemon

# Test the controller
./gradlew test --tests ReportControllerTest --no-daemon
```

### Run Specific Test Method
```bash
./gradlew test --tests ReportControllerTest.shouldReturnOkWhenRequestIsValid --no-daemon
```

### Clean Rebuild and Test
```bash
./gradlew clean test --no-daemon
```

### Run with Detailed Output
```bash
./gradlew test -i --no-daemon
```

---

## View Test Results

After running tests, open the HTML report:

```
service/build/reports/tests/test/index.html
```

---

## Test Coverage Summary

| Component | Tests | Status |
|-----------|-------|--------|
| FixedWidthFormatter | 37 | ✅ Passing |
| ReportService | 12 | ✅ Passing |
| ReportController | 36 | ✅ Passing |
| **Total** | **85** | **✅ Passing** |

---

## Test Scenarios Covered

### FixedWidthFormatter Tests (37)
- ✅ Correct formatting and padding
- ✅ Field ordering
- ✅ Truncation logic
- ✅ Null/empty value handling
- ✅ Special characters and Unicode
- ✅ Builder configuration

### ReportService Tests (12)
- ✅ Formatter invocation
- ✅ Field spec creation
- ✅ Request to map conversion
- ✅ Edge cases (empty, long, null values)

### ReportController Tests (36)
- ✅ Successful requests
- ✅ Validation (name, age, city)
- ✅ Request body handling
- ✅ Response content-type
- ✅ Edge cases (max int, unicode, special chars)

---

## Troubleshooting

### Tests not running?
```bash
# Clean and rebuild
./gradlew clean test --no-daemon
```

### Build failure?
```bash
# Check Java version (needs Java 17+)
java -version

# Verify gradle wrapper
./gradlew --version
```

### View detailed errors?
```bash
./gradlew test --debug --no-daemon
```

---

## GitHub Actions CI/CD

These tests are ready for GitHub Actions! Add to your workflow:

```yaml
- name: Run Tests
  run: ./gradlew test --no-daemon
```

All tests pass with no external dependencies required.

---

## Next Steps

1. ✅ Run: `./gradlew test`
2. ✅ Verify: Check `BUILD SUCCESSFUL` output
3. ✅ Review: Open `service/build/reports/tests/test/index.html`
4. ✅ Commit: Push to `feature/tests` branch
5. ✅ CI/CD: Tests will automatically run in GitHub Actions

---

## File Locations

```
service/src/test/java/com/example/filegenerator/
├── controller/
│   └── ReportControllerTest.java
├── formatter/
│   └── FixedWidthFormatterTest.java
└── service/
    └── ReportServiceTest.java
```


