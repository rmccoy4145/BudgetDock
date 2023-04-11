package org.example;

import java.util.Objects;

public class BudgetOperation
{
    private final OperationType type;
    private String description;
    private int amount;

    public BudgetOperation(OperationType type)
    {
        Objects.requireNonNull(type, "type cannot be null");
        this.type = type;
    }

    BudgetOperation(OperationType type, int amount, String description)
    {
        this(type);
        this.amount = amount;
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }

    public int getAmount()
    {
        return amount;
    }

    public OperationType getType()
    {
        return type;
    }

    public static enum OperationType
    {
        DEPOSIT,
        WITHDRAW,
        SHUTDOWN,
        NEXT_DAY,
        INFO
    }
}
