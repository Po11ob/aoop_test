package com.example.asyncdemo.runner;

import com.example.asyncdemo.service.TaskService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Task Runner - Alternative Implementation
 *
 * This class provides an alternative CommandLineRunner implementation.
 * It demonstrates how to structure a dedicated runner bean separately
 * from the main application class.
 *
 * Note: In AsyncDemoApplication.java, we already define a CommandLineRunner
 * as a @Bean. This class is provided as an alternative pattern.
 * Uncomment the @Component annotation if you want to use this instead.
 */
// @Component
public class TaskRunner implements CommandLineRunner {

    private final TaskService taskService;

    /**
     * Constructor injection of TaskService.
     * Spring automatically provides the bean instance.
     *
     * @param taskService Autowired TaskService instance
     */
    public TaskRunner(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Runs when Spring Boot application starts.
     *
     * This method demonstrates task submission and execution flow.
     * Note: If this class is enabled (@Component), the bean definition
     * in AsyncDemoApplication will be overridden.
     *
     * @param args Command line arguments
     * @throws Exception Any exception during execution
     */
    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("ASYNC TASK EXECUTION STARTED");
        System.out.println("=".repeat(70));

        System.out.println("\nSubmitting 3 tasks to async executor...");
        System.out.println("Each task will sleep for 1 second.\n");

        // Submit three async tasks
        // These calls return immediately
        taskService.executeTask(1);
        taskService.executeTask(2);
        taskService.executeTask(3);

        System.out.println("\n[Main Thread] All tasks submitted!");
        System.out.println("[Main Thread] Waiting for tasks to complete...");

        // Wait for async tasks to complete
        // In production, use CountDownLatch or other synchronization mechanisms
        try {
            Thread.sleep(3500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n" + "=".repeat(70));
        System.out.println("ASYNC TASK EXECUTION COMPLETED");
        System.out.println("=".repeat(70) + "\n");
    }
}
