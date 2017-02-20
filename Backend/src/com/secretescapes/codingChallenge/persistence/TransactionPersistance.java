package com.secretescapes.codingChallenge.persistence;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.mongodb.*;
import com.secretescapes.codingChallenge.model.Account;
import com.secretescapes.codingChallenge.model.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransactionPersistance {
    private Gson gson;
    private DB mongoDb;

    public static final String COLLECTION_NAME = "transactions";

    @Inject
    public TransactionPersistance(Gson gson, DB mongoDb) {
        this.gson = gson;
        this.mongoDb = mongoDb;
    }

    public List<Transaction> getTransactions(String accountId) {
        List<Transaction> transactionList = new ArrayList<>();
        BasicDBObject obFrom = new BasicDBObject();

        obFrom.put("fromAccount", accountId);

        BasicDBObject obTo = new BasicDBObject();
        obTo.put("toAccount", accountId);
        List<BasicDBObject> ob = new ArrayList<>();
        ob.add(obFrom);
        ob.add(obTo);
        BasicDBObject query = new BasicDBObject();
        query.put("$or", ob);

        DBCursor transactions = mongoDb.getCollection(COLLECTION_NAME).find(query);
        transactions.forEach(dbObject -> {
            DBObject dbObject1 = dbObject;
            String val = dbObject.toString();
            transactionList.add(gson.fromJson(dbObject.toString(), Transaction.class));
        });

        return transactionList;
    }

    public void storeNewTransaction(Transaction transaction) {
        transaction._id = UUID.randomUUID().toString();
        mongoDb.getCollection(COLLECTION_NAME).insert(BasicDBObject.parse(gson.toJson(transaction)));
    }
}
