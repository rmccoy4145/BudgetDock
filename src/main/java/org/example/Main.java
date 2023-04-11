package org.example;

import java.util.concurrent.*;

public class Main
{
    public static void main(String[] args)
    {
        System.out.println("Welcome to BudgetDock\\n\\n");
        System.out.println("Im here to help you stop wasting money on dumb shit...\\n");

        LinkedBlockingQueue<BudgetOperation> queue = new LinkedBlockingQueue<>();
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        CountDownLatch latch = new CountDownLatch(1);

        // Submit the Runnable task to the ExecutorService
        executorService.submit(new BudgetService(queue, latch));

        // TODO: Add a vertx web server to pass requests to the queue
        queue.add(new BudgetOperation(BudgetOperation.OperationType.INFO));
        queue.add(new BudgetOperation(BudgetOperation.OperationType.DEPOSIT, 1000, "Got Paid!"));
        queue.add(new BudgetOperation(BudgetOperation.OperationType.WITHDRAW, 5, "Coffee"));
        queue.add(new BudgetOperation(BudgetOperation.OperationType.WITHDRAW, 10, "Lunch"));
        queue.add(new BudgetOperation(BudgetOperation.OperationType.WITHDRAW, 55, "Groceries"));
        queue.add(new BudgetOperation(BudgetOperation.OperationType.NEXT_DAY));
        queue.add(new BudgetOperation(BudgetOperation.OperationType.INFO));
        queue.add(new BudgetOperation(BudgetOperation.OperationType.WITHDRAW, 5, "Coffee"));
        queue.add(new BudgetOperation(BudgetOperation.OperationType.WITHDRAW, 10, "Lunch"));
        queue.add(new BudgetOperation(BudgetOperation.OperationType.WITHDRAW, 55, "Groceries"));
        queue.add(new BudgetOperation(BudgetOperation.OperationType.NEXT_DAY));
        queue.add(new BudgetOperation(BudgetOperation.OperationType.INFO));
        queue.add(new BudgetOperation(BudgetOperation.OperationType.SHUTDOWN));

        executorService.shutdown();
        try {
            // Wait for the latch to reach zero
            latch.await();
        } catch (InterruptedException e) {
            System.out.println("Interrupted while waiting for tasks to complete.");
        }

    }

}