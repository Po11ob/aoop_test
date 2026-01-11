package com.example.asyncdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Async Configuration Class
 *
 * This class defines the custom thread pool executor for handling async tasks.
 * Instead of using Spring's default unbounded thread pool, we create a
 * fixed-size thread pool with predictable behavior.
 *
 * CUSTOM EXECUTOR VS DEFAULT:
 * =====================================================================
 * DEFAULT EXECUTOR (without custom configuration):
 *   - Creates new threads as needed (unbounded)
 *   - Can lead to unlimited thread creation
 *   - Resource intensive and unpredictable
 *   - Risk of thread explosion under heavy load
 *
 * CUSTOM EXECUTOR (ThreadPoolTaskExecutor):
 *   - Fixed number of worker threads (core pool size)
 *   - Queue system for tasks exceeding available threads
 *   - Controlled resource usage
 *   - Predictable performance characteristics
 *   - Can be tuned for specific workload requirements
 */
@Configuration
public class AsyncConfig {

    /**
     * Configures the ThreadPoolTaskExecutor for async operations.
     *
     * THREAD POOL SIZE IMPACT ON CONCURRENCY:
     * =====================================================================
     * - Core Pool Size = 2: Always keep 2 threads active
     * - Max Pool Size = 2: Never create more than 2 threads
     *
     * When you submit 3 tasks:
     *   - Tasks 1 & 2: Execute immediately on separate threads
     *   - Task 3: Waits in queue until a thread becomes available
     *
     * This is effective for:
     *   - Database operations
     *   - Email sending
     *   - File processing
     *   - API calls
     *   - Any long-running I/O bound operations
     *
     * @return Configured Executor bean
     */
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // Core pool size: minimum number of threads to maintain
        // Even if idle, these threads stay alive
        executor.setCorePoolSize(2);

        // Maximum pool size: maximum number of threads that can be created
        // Set equal to core pool size to maintain fixed pool
        executor.setMaxPoolSize(2);

        // Queue capacity: number of tasks that can wait in queue
        // When queue is full and all threads are busy, new tasks are rejected
        executor.setQueueCapacity(100);

        // Thread name prefix: helps identify async threads in logs
        // Makes debugging and monitoring much easier
        executor.setThreadNamePrefix("LabWorker-");

        // Wait time for graceful shutdown (milliseconds)
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(10);

        // Initialize the executor: required to start the thread pool
        // Must be called after setting all configuration
        executor.initialize();

        return executor;
    }
}
