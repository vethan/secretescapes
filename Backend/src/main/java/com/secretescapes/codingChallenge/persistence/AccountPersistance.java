package com.secretescapes.codingChallenge.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.secretescapes.codingChallenge.model.Account;

import java.util.ArrayList;
import java.util.List;

import com.github.fakemongo.Fongo;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;

public class AccountPersistance {
    private Gson gson;
    private DB mongoDb;

    public static final String COLLECTION_NAME = "accounts";

    @Inject
    public AccountPersistance(Gson gson, DB mongoDb) {
        this.gson = gson;
        this.mongoDb = mongoDb;
    }

    public List<Account> getAccountList() {
        List<Account> accountList = new ArrayList<>();
        BasicDBObject ob = new BasicDBObject();
        DBCursor accounts = mongoDb.getCollection(COLLECTION_NAME).find(ob);

        accounts.forEach(dbObject -> {
            accountList.add(gson.fromJson(dbObject.toString(), Account.class));
        });

        return accountList;
    }

    public Account getAccount(String accountId) {
        BasicDBObject ob = new BasicDBObject();
        ob.put("_id", accountId);
        DBCursor accounts = mongoDb.getCollection(COLLECTION_NAME).find(ob);
        if (accounts.size() == 0)
            return null;

        return gson.fromJson(accounts.one().toString(), Account.class);
    }

    public boolean updateAccount(Account updatedAccount) {
        if (updatedAccount._id == null || updatedAccount.balance < 0) {
            return false;
        }
        BasicDBObject ob = BasicDBObject.parse(gson.toJson(updatedAccount));
        ob.remove("_id");

        BasicDBObject query = new BasicDBObject();
        query.put("_id", updatedAccount._id);

        mongoDb.getCollection(COLLECTION_NAME).update(query, ob);
        return true;
    }
}
