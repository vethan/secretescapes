package com.secretescapes.codingChallenge.modules;

import com.google.inject.AbstractModule;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;

import javax.mail.Session;
import java.util.Properties;

public class GreenmailModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(String.class).annotatedWith(Names.named("email_host")).toInstance("localhost");
        bind(String.class).annotatedWith(Names.named("email_user_address")).toInstance("localhost");
        bind(String.class).annotatedWith(Names.named("email_user_password")).toInstance("localhost");

        GreenMail greenMail = new GreenMail(ServerSetupTest.SMTP);
        greenMail.start();

        Properties props = System.getProperties();
        props.put("mail.smtp.host", "localhost");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", ServerSetupTest.SMTP.getPort());
        Session session = Session.getInstance(props, null);

        bind(Session.class).toInstance(session);
        bind(GreenMail.class).toInstance(greenMail);
    }
}
