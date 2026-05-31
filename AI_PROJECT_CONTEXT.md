# Enterprise File Generation Platform — AI Context Document

This file provides architecture and project context for AI coding assistants.

## Project
- Enterprise File Generation Platform
- Java 21
- Spring Boot
- Gradle
- CI/CD learning focused

## Goals
- Build enterprise-grade report generation system
- Learn GitHub Actions, Docker, Kubernetes, Helm, Argo CD
- Use AI to reduce boilerplate coding

## Features
- Fixed-width report generation
- CSV/XML generation
- Async processing
- Monitoring support
- Kubernetes-ready architecture

## Project Structure
```text
controller/
service/
formatter/
processor/
model/
exception/
util/
config/
```

## Current Feature
Input:
```json
{
  "name":"JOHN",
  "age":25,
  "city":"NY"
}
```

Output:
```text
JOHN      25NY
```

## AI Expectations
- Follow SOLID principles
- Use constructor injection
- Generate reusable enterprise-grade code
- Support CI/CD compatibility
- Support Kubernetes deployment
