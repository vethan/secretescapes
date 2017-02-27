package com.secretescapes.codingChallenge.modules;

import com.github.fakemongo.Fongo;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.WriteResult;
import com.secretescapes.codingChallenge.model.Account;
import com.secretescapes.codingChallenge.model.Transaction;
import com.secretescapes.codingChallenge.persistence.AccountPersistance;
import com.secretescapes.codingChallenge.persistence.TransactionPersistance;
import com.vividsolutions.jts.util.Debug;
import org.fluttercode.datafactory.impl.DataFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FongoModule extends AbstractModule {
    Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new TypeAdapter<LocalDateTime>() {
        @Override
        public void write(JsonWriter jsonWriter, LocalDateTime o) throws IOException {
            jsonWriter.value(o.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }

        @Override
        public LocalDateTime read(JsonReader jsonReader) throws IOException {
            return LocalDateTime.parse(jsonReader.nextString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
    }).create();

    @Override
    protected void configure() {
        //Create fake mongo instance
        Fongo fongo = new Fongo("db");

        DB database = fongo.getDB("db");
        //Fill in data
        DBCollection collection = database.getCollection(AccountPersistance.COLLECTION_NAME);
        DBCollection transactionCollection = database.getCollection(TransactionPersistance.COLLECTION_NAME);

        addTestData(collection, transactionCollection);

        //Bind to dependency injection
        bind(DB.class).toInstance(database);
    }

    private void addTestData(DBCollection accountCollection, DBCollection transactionCollection) {
        DataFactory df = new DataFactory();
        List<String> accountIds = new ArrayList<>();


        for (int i = 0; i < 10; i++) {
            accountIds.add(AddAccount(accountCollection, df.getName(), df.getEmailAddress(), df.getNumberBetween(0, 50000)));
            System.out.println("Created Acccount: " + accountIds.get(i));
        }

        for (int i = 0; i < 30; i++) {
            int fromIndex = df.getNumberUpTo(accountIds.size());
            int toIndex = df.getNumberUpTo(accountIds.size());
            if (fromIndex == toIndex) {
                toIndex = (toIndex + 1) % accountIds.size();
            }
            System.out.println("Created Transaction from " + accountIds.get(fromIndex) + " to  " + accountIds.get(toIndex));

            AddTransaction(transactionCollection, accountIds.get(fromIndex), accountIds.get(toIndex), df.getNumberBetween(1, 10000), LocalDateTime.now().minus(df.getNumberUpTo(80320), ChronoUnit.MINUTES));
        }
    }

    private void AddTransaction(DBCollection transactionCollection, String from, String to, long amount, LocalDateTime time) {
        Transaction transaction = new Transaction();
        transaction.fromAccount = from;
        transaction.toAccount = to;
        transaction.transferAmount = amount;
        transaction.transactionTime = time;
        transaction._id = UUID.randomUUID().toString();

        BasicDBObject object = BasicDBObject.parse(gson.toJson(transaction));
        transactionCollection.insert(object);
    }

    private String AddAccount(DBCollection accountCollection, String name, String emailAddress, int balance) {
        Account acc = new Account();
        acc.name = name;
        acc.emailAddress = emailAddress;
        acc.balance = balance;
        acc._id = UUID.randomUUID().toString();


        BasicDBObject object = BasicDBObject.parse(gson.toJson(acc));
        WriteResult insert = accountCollection.insert(object);
        return object.get("_id").toString();
    }

}
