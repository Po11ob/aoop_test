# Async Thread Pool Demonstration Project

## Overview

This is a comprehensive Spring Boot application that demonstrates background task execution using the `@Async` annotation with a custom thread pool executor. The project showcases how to efficiently handle concurrent operations using a fixed-size thread pool instead of Spring's default unbounded executor.

## Project Structure

```
src/main/java/com/example/asyncdemo/
├── AsyncDemoApplication.java          # Main application class with @EnableAsync
├── config/
│   └── AsyncConfig.java              # Thread pool configuration
├── service/
│   └── TaskService.java              # Async task execution service
└── runner/
    └── TaskRunner.java               # Alternative CommandLineRunner implementation

src/main/resources/
└── application.properties             # Application configuration
```

## Key Components

### 1. AsyncDemoApplication.java
- Enables async support with `@EnableAsync` annotation
- Defines a CommandLineRunner bean to demonstrate functionality
- Provides clear console output showing task execution flow

### 2. AsyncConfig.java
Configuration class defining the custom thread pool:
- Core Pool Size: 2 (minimum threads always active)
- Max Pool Size: 2 (maximum threads allowed)
- Queue Capacity: 100
- Thread Name Prefix: "LabWorker-"

### 3. TaskService.java
Service class with async task execution:
- `@Async("taskExecutor")` method for concurrent execution
- Simulates 1-second long-running operation
- Uses specified executor bean

### 4. TaskRunner.java
Alternative CommandLineRunner implementation (currently disabled)

## Expected Output

```
======================================================================
ASYNC THREAD POOL DEMONSTRATION
======================================================================

Submitting 3 async tasks to execute on custom thread pool...
Thread pool configured with:
  - Core Pool Size: 2 (minimum threads always active)
  - Max Pool Size: 2 (maximum threads allowed)
  - Thread Prefix: LabWorker-

Expected Behavior:
  - Task 1 and Task 2 will start immediately on LabWorker-1 and LabWorker-2
  - Task 3 will wait ~1 second, then start on a freed thread

----------------------------------------------------------------------

Start Task 1 on LabWorker-1
  ⌞─ Started at: 1234567890123
Start Task 2 on LabWorker-2
  ⌞─ Started at: 1234567890124
[Main Thread] All tasks submitted! Main thread continues...

End Task 1 (completed in 1001ms)
End Task 2 (completed in 1002ms)
Start Task 3 on LabWorker-1
  ⌞─ Started at: 1234567892125
End Task 3 (completed in 1001ms)

======================================================================
DEMONSTRATION COMPLETE
======================================================================
```

## Building and Running

### Build
```bash
mvn clean package -DskipTests
```

### Run
```bash
# Option 1: Using Java directly
java -jar target/async-demo-1.0.0.jar

# Option 2: Using Maven
mvn spring-boot:run
```

## Key Concepts

### Why @EnableAsync?
- Enables Spring proxy-based async method execution
- Without it, @Async methods execute synchronously
- Allows Spring to intercept and redirect calls to thread pool

### Custom vs Default Executor
- **Default**: Creates unlimited threads (unbounded)
- **Custom**: Fixed thread count with queue system

### Thread Pool Behavior
With 2-thread pool and 3 tasks:
1. Tasks 1 & 2 start immediately (one per thread)
2. Task 3 waits in queue (~1 second)
3. Task 3 executes when thread becomes available

### Async Method Rules
✓ Must be public
✓ Spring must manage the bean
✓ Must call through Spring proxy (not directly)
✓ Return type: void or CompletableFuture<T>

## When to Use @Async

✅ Email sending
✅ Database operations
✅ File processing
✅ API calls
✅ Report generation
✅ Long-running computations
✅ Background maintenance

## Project Requirements

- Java 17+
- Spring Boot 3.2.0
- Maven 3.6+
- No Lombok or Reactive dependencies

## Summary

This project demonstrates:
✓ Spring Boot async configuration
✓ Custom thread pool setup
✓ Concurrent task execution
✓ Proper exception handling
✓ Production-ready code patterns
✓ Clear execution flow visualization

Run the application to see async execution in action!
