package com.secretescapes.codingChallenge.model;

import java.time.LocalDateTime;

public class Transaction {
    public String fromAccount;
    public String toAccount;
    public LocalDateTime transactionTime;
    public long transferAmount;
    public String _id = null;
}
