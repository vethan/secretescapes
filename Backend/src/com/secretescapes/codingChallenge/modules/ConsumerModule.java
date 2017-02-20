package com.secretescapes.codingChallenge.modules;

import com.google.inject.AbstractModule;
import com.secretescapes.codingChallenge.consumers.PaymentConsumer;

public class ConsumerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(PaymentConsumer.class);
    }
}
