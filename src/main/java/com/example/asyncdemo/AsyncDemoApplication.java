package com.example.asyncdemo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import com.example.asyncdemo.service.TaskService;

/**
 * Main Spring Boot Application Class
 *
 * ASYNC EXECUTION EXPLAINED:
 * ============================================================
 * When @EnableAsync is applied, Spring Boot enables proxy-based
 * async method execution. Any method annotated with @Async will be
 * executed in a separate thread managed by a thread pool executor.
 *
 * WHY @EnableAsync IS REQUIRED:
 * - It enables processing of @Async annotations
 * - It creates proxy objects around beans with @Async methods
 * - Without it, @Async methods execute on the calling thread (synchronously)
 *
 * THREAD POOL STRATEGY:
 * - The custom ThreadPoolTaskExecutor defined in AsyncConfig is registered
 * - All @Async methods use this executor unless specified otherwise
 * - This provides fine-grained control over concurrency and resource management
 */
@SpringBootApplication
@EnableAsync
public class AsyncDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsyncDemoApplication.class, args);
    }

    /**
     * CommandLineRunner Bean
     *
     * This runner executes immediately after Spring Boot starts.
     * It demonstrates the async task execution by submitting three tasks
     * that will run concurrently using the custom thread pool.
     */
    @Bean
    public CommandLineRunner taskRunner(TaskService taskService) {
        return args -> {
            System.out.println("\n" + "=".repeat(70));
            System.out.println("ASYNC THREAD POOL DEMONSTRATION");
            System.out.println("=".repeat(70));
            System.out.println("\nSubmitting 3 async tasks to execute on custom thread pool...");
            System.out.println("Thread pool configured with:");
            System.out.println("  - Core Pool Size: 2 (minimum threads always active)");
            System.out.println("  - Max Pool Size: 2 (maximum threads allowed)");
            System.out.println("  - Thread Prefix: LabWorker-");
            System.out.println("\nExpected Behavior:");
            System.out.println("  - Task 1 and Task 2 will start immediately on LabWorker-1 and LabWorker-2");
            System.out.println("  - Task 3 will wait ~1 second, then start on a freed thread");
            System.out.println("\n" + "-".repeat(70) + "\n");

            // Submit three tasks to run asynchronously
            // These calls return immediately without waiting for completion
            taskService.executeTask(1);
            taskService.executeTask(2);
            taskService.executeTask(3);

            System.out.println("\n[Main Thread] All tasks submitted! Main thread continues...");

            // Wait for async tasks to complete before application exits
            // This allows time for the thread pool to process the tasks
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            System.out.println("\n" + "=".repeat(70));
            System.out.println("DEMONSTRATION COMPLETE");
            System.out.println("=".repeat(70) + "\n");
        };
    }
}
