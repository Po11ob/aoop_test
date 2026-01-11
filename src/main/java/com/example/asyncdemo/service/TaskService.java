package com.example.asyncdemo.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Task Service Class
 *
 * This service provides async task execution capability using Spring's
 * @Async annotation with our custom thread pool executor.
 *
 * KEY CONCEPTS:
 * =====================================================================
 * @Async ANNOTATION:
 *   - Marks a method to be executed asynchronously
 *   - Spring creates a proxy that intercepts the call
 *   - The method runs in a thread from the thread pool
 *   - The caller receives control immediately
 *   - Caller and executor are now on different threads
 *
 * @Async("taskExecutor"):
 *   - Specifies which executor bean to use for this method
 *   - References the bean name defined in AsyncConfig
 *   - Without specifying, Spring uses default SimpleAsyncTaskExecutor
 *   - Our custom executor provides fixed-size thread pool
 *
 * LIMITATIONS:
 *   - @Async only works on public methods
 *   - Cannot use @Async on private methods (proxy limitation)
 *   - The method call must be through Spring's proxy
 *   - Self-invocation (calling from same class) bypasses proxy
 */
@Service
public class TaskService {

    /**
     * Executes a long-running task asynchronously.
     *
     * EXECUTION FLOW:
     * =====================================================================
     * 1. Method is called from main thread via Spring proxy
     * 2. Proxy submits task to "taskExecutor" thread pool
     * 3. Control returns immediately to caller
     * 4. Task executes on a separate worker thread
     * 5. Meanwhile, main thread continues execution
     *
     * THREAD SLEEP AS SIMULATION:
     *   - Real-world async tasks involve:
     *     * Database queries
     *     * HTTP requests
     *     * File I/O operations
     *   - Thread.sleep(1000) simulates these I/O operations
     *   - Allows demonstration without actual I/O
     *   - Makes thread behavior visible and testable
     *
     * WHY THREAD.SLEEP?
     *   - Demonstrates how long-running operations can block a thread
     *   - If not async: main thread would be blocked for 1 second
     *   - With async: main thread continues immediately
     *   - Multiple async calls can execute in parallel
     *
     * @param taskId Unique identifier for this task
     */
    @Async("taskExecutor")
    public void executeTask(int taskId) {
        String threadName = Thread.currentThread().getName();
        long startTime = System.currentTimeMillis();

        // Print task start information
        System.out.println("Start Task " + taskId + " on " + threadName);
        System.out.println("  ⌞─ Started at: " + startTime);

        try {
            // Simulate a long-running operation (1 second)
            // In real applications, this would be database queries, API calls, etc.
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // Handle interruption gracefully
            // This occurs when the thread pool is shutting down
            System.out.println("Task " + taskId + " was interrupted");
            Thread.currentThread().interrupt();
            return;
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // Print task completion information
        System.out.println("End Task " + taskId + " (completed in " + duration + "ms)");
    }
}
