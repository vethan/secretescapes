package com.secretescapes.codingChallenge.modules;

import com.google.inject.AbstractModule;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;

import javax.mail.Session;
import java.util.Properties;

public class GreenmailModule extends AbstractModule {
    private static final String USER_PASSWORD = "abcdef123";
    private static final String USER_NAME = "hascode";
    private static final String EMAIL_USER_ADDRESS = "hascode@localhost";
    private static final String LOCALHOST = "127.0.0.1";

    @Override
    protected void configure() {
        bind(String.class).annotatedWith(Names.named("email_host")).toInstance(LOCALHOST);
        bind(String.class).annotatedWith(Names.named("email_user_address")).toInstance(EMAIL_USER_ADDRESS);
        bind(String.class).annotatedWith(Names.named("email_user_password")).toInstance(USER_PASSWORD);

        GreenMail greenMail = new GreenMail(ServerSetupTest.SMTP);
        greenMail.start();
        greenMail.setUser(EMAIL_USER_ADDRESS, USER_NAME, USER_PASSWORD);

        Properties props = System.getProperties();
        props.put("mail.smtp.host", LOCALHOST);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", ServerSetupTest.SMTP.getPort());
        Session session = Session.getInstance(props, null);

        bind(Session.class).toInstance(session);
        bind(GreenMail.class).toInstance(greenMail);
    }
}
