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
        enableCORS("*", "*", "*");
        get("/listAccounts", (req, res) -> {
            return gson.toJson(accountPersistance.getAccountList());
        });
        get("/getTransactions/*", (req, res) -> {
            return gson.toJson(transactionPersistance.getTransactions(req.splat()[0]));
        });
        post("/makePayment", (req, res) -> {
            JsonParser parser = new JsonParser();
            JsonObject requestInput = parser.parse(req.body()).getAsJsonObject();
            return paymentConsumer.newTransaction(requestInput.get("fromAccount").getAsString()
                    , requestInput.get("toAccount").getAsString()
                    , requestInput.get("amount").getAsInt());
        });


    }

    // Enables CORS on requests. This method is an initialization method and should be called once.
    private static void enableCORS(final String origin, final String methods, final String headers) {

        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
            // Note: this may or may not be necessary in your particular application
            response.type("application/json");
        });
    }
}
