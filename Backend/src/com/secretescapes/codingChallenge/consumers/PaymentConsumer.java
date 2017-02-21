package com.secretescapes.codingChallenge.consumers;

import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.inject.Inject;
import com.secretescapes.codingChallenge.email.MailSender;
import com.secretescapes.codingChallenge.model.Account;
import com.secretescapes.codingChallenge.model.Transaction;
import com.secretescapes.codingChallenge.persistence.AccountPersistance;
import com.secretescapes.codingChallenge.persistence.TransactionPersistance;

import java.time.LocalDateTime;

public class PaymentConsumer {
    private final AccountPersistance accountPersistance;
    private final TransactionPersistance transactionPersistance;
    private final MailSender mailer;

    @Inject
    public PaymentConsumer(AccountPersistance accountPersistance, TransactionPersistance transactionPersistance, MailSender mailer) {

        this.accountPersistance = accountPersistance;
        this.transactionPersistance = transactionPersistance;
        this.mailer = mailer;
    }

    public String newTransaction(String fromAccountId, String toAccountId, int transferAmount) {
        JsonObject result = new JsonObject();
        if (transferAmount < 1) {
            result.addProperty("success", false);
            result.addProperty("errorMessage", "Cannot transfer less that £0.01");
            return result.getAsString();
        }
        Account fromAccount = accountPersistance.getAccount(fromAccountId);
        Account toAccount = accountPersistance.getAccount(toAccountId);

        if (fromAccount.balance < transferAmount) {
            result.addProperty("success", false);
            result.addProperty("errorMessage", "Cannot transfer more money than is in the account");
            return result.getAsString();
        }

        fromAccount.balance -= transferAmount;
        toAccount.balance += transferAmount;
        Transaction transaction = new Transaction();
        transaction.fromAccount = fromAccountId;
        transaction.toAccount = toAccountId;
        transaction.transferAmount = transferAmount;
        transaction.transactionTime = LocalDateTime.now();

        transactionPersistance.storeNewTransaction(transaction);
        accountPersistance.updateAccount(fromAccount);
        accountPersistance.updateAccount(toAccount);
        String amountString = "£" + (transferAmount * 0.01f);
        mailer.SendEmail(fromAccount.emailAddress, "You've made a payment", "You've successfully paid " + toAccount.name + " " + amountString);
        mailer.SendEmail(toAccount.emailAddress, "You've made a payment", "You've recieved " + amountString + " from " + fromAccount.name);

        result.addProperty("success", true);
        return result.toString();
    }
}
