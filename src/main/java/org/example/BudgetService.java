package org.example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Stream;

public class BudgetService implements Runnable
{
    private final CountDownLatch latch;
    private boolean running = false;
    private LinkedBlockingQueue<BudgetOperation> queue;
    private LocalDate TODAY = LocalDate.now();
    private int TOTAL_BALANCE = 0;
    private final HashMap<LocalDate, List<Integer>> WITHDRAWS = new HashMap<>(Map.of(TODAY, new ArrayList<>()));
    private final HashMap<LocalDate, List<Integer>> DEPOSITS = new HashMap<>(Map.of(TODAY, new ArrayList<>()));
    private int MONTHLY_SPENDING_LIMIT = 500;

    public BudgetService(LinkedBlockingQueue<BudgetOperation> queue, CountDownLatch latch)
    {
        this.latch = latch;
        this.queue = queue;
    }

    public void depositeCash(int amount, String description)
    {
        TOTAL_BALANCE += amount;
        List<Integer> d = DEPOSITS.get(TODAY);

        if(d == null)
        {
            DEPOSITS.put(TODAY, new ArrayList<>(List.of(amount)));
        }
        else
        {
            d.add(amount);
        }

        printLineBreak();
        System.out.println("You deposited: $" + amount + " for " + (description != null ? description : "no reason"));
        System.out.printf("Your new balance is: $%d\n", TOTAL_BALANCE);
        printLineBreak();
    }
    public void withdrawCash(int amount, String description)
    {
        TOTAL_BALANCE -= amount;
        List<Integer> w = WITHDRAWS.get(TODAY);

        if(w == null)
        {
            WITHDRAWS.put(TODAY, new ArrayList<>(List.of(amount)));
        }
        else
        {
            w.add(amount);
        }
        printLineBreak();
        System.out.println("You withdrew: $" + amount + " for " + (description != null ? description : "no reason"));
        System.out.printf("Your new balance is: $%d\n", TOTAL_BALANCE);
        printLineBreak();
        checkSpendingLimit();
    }

    public void info()
    {
        printLineBreak();
        System.out.println("Today is " + TODAY);
        System.out.println("Days left in month: " + getDaysLeftInMonth());
        System.out.println("Total spent this month: $" + getTotalSpentThisMonth());
        System.out.println("Total balance: $" + TOTAL_BALANCE + "\n");
        printLineBreak();
    }

    private int getTotalSpentThisMonth()
    {
        int totalSpent = 0;
        for(Map.Entry<LocalDate, List<Integer>> entry : WITHDRAWS.entrySet())
        {
            if(entry.getKey().getMonth() == TODAY.getMonth())
            {
                totalSpent += entry.getValue().stream().mapToInt(Integer::intValue).sum();
            }
        }
        return totalSpent;
    }

    public int getDaysLeftInMonth()
    {
        return TODAY.lengthOfMonth() - TODAY.getDayOfMonth();
    }

    public void checkSpendingLimit()
    {
        int totalSpent = getTotalSpentThisMonth();
        if(totalSpent > MONTHLY_SPENDING_LIMIT)
        {
            System.out.println("You exceeded your spending limit... stop buying dumb shit damn!!");
        }
        else
        {
            System.out.println("You have $" + (MONTHLY_SPENDING_LIMIT - totalSpent) + " left to spend this month");
        }
    }

    /**
     * Test method
     * Advances the current day by one
     */
    private void nextDay()
    {
        TODAY = TODAY.plusDays(1);
    }

    @Override
    public void run()
    {
        System.out.println("Starting BudgetService...");
        running = true;
        while(running)
        {
            try
            {
                Thread.sleep(1000);
            } catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
            if(queue.isEmpty())
            {
                System.out.println("Queue is empty");
                continue;
            }
            System.out.println("Processing next job...");
            BudgetOperation operation= null;
            try
            {
                operation = queue.take();
                BudgetOperation.OperationType operationType = operation.getType();
                switch (operationType)
                {
                    case DEPOSIT:
                        depositeCash(operation.getAmount(), operation.getDescription());
                        break;
                    case WITHDRAW:
                        withdrawCash(operation.getAmount(), operation.getDescription());
                        break;
                    case SHUTDOWN:
                        System.out.println("Shutdown received!");
                        running = false;
                        break;
                    case NEXT_DAY:
                        nextDay();
                        break;
                    case INFO:
                        info();
                        break;
                }

            } catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
        }
        System.out.println("BudgetService is shutting down...");
        latch.countDown();
    }

    private static void printLineBreak()
    {
        System.out.println("----------------------------------------\n");
    }
}
