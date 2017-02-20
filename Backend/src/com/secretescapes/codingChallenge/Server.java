package com.secretescapes.codingChallenge;

import static spark.Spark.*;

import com.google.gson.*;
import com.google.inject.Inject;
import com.secretescapes.codingChallenge.consumers.PaymentConsumer;
import com.secretescapes.codingChallenge.persistence.AccountPersistance;
import com.secretescapes.codingChallenge.persistence.TransactionPersistance;

public class Server {
    private final Gson gson;
    private final AccountPersistance accountPersistance;
    private final TransactionPersistance transactionPersistance;
    private final PaymentConsumer paymentConsumer;

    @Inject
    public Server(Gson gson, AccountPersistance accountPersistance, TransactionPersistance transactionPersistance, PaymentConsumer paymentConsumer) {
        this.gson = gson;

        this.accountPersistance = accountPersistance;
        this.transactionPersistance = transactionPersistance;
        this.paymentConsumer = paymentConsumer;
    }

    public void start() {
        get("/listAccounts", (req, res) -> {
            return gson.toJson(accountPersistance.getAccountList());
        });
        get("/getTransactions/*", (req, res) -> {
            return gson.toJson(transactionPersistance.getTransactions(req.splat()[0]));
        });
        put("/makePayment", (req, res) -> {
            JsonParser parser = new JsonParser();
            JsonObject requestInput = parser.parse(req.body()).getAsJsonObject();
            return paymentConsumer.newTransaction(requestInput.get("fromAddress").getAsString()
                    , requestInput.get("toAddress").getAsString()
                    , requestInput.get("amount").getAsInt());
        });


    }
}
