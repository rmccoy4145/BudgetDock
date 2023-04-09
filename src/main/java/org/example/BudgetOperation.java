package org.example;

import java.util.Objects;

public class AccountOperation
{
    private final OperationType type;
    private final String description;
    private final int amount;

    AccountOperation(OperationType type, int amount, String description)
    {
        Objects.requireNonNull(type, "type cannot be null");
        Objects.requireNonNull(description, "description cannot be null");
        this.type = type;
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
        WITHDRAW
    }
}
