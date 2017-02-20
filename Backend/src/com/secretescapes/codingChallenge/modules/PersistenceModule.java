package com.secretescapes.codingChallenge.modules;

import com.google.inject.AbstractModule;
import com.secretescapes.codingChallenge.persistence.AccountPersistance;
import com.secretescapes.codingChallenge.persistence.TransactionPersistance;

public class PersistenceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(TransactionPersistance.class);
        bind(AccountPersistance.class);

    }
}
