package com.secretescapes.codingChallenge;

import com.google.inject.name.Names;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.secretescapes.codingChallenge.email.MailSender;
import com.secretescapes.codingChallenge.modules.GreenmailModule;
import org.jukito.JukitoRunner;
import org.jukito.UseModules;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;

@RunWith(JukitoRunner.class)
public class MailSenderTest {

    private static final String USER_PASSWORD = "abcdef123";
    private static final String USER_NAME = "hascode";
    private static final String EMAIL_USER_ADDRESS = "hascode@localhost";
    private static final String LOCALHOST = "127.0.0.1";

    static GreenMail greenMail;
    MailSender sender;
    static Session session;

    @BeforeClass
    public static void classSetup() {

        GreenMail greenMail = new GreenMail(ServerSetupTest.SMTP);
        greenMail.start();
        greenMail.setUser(EMAIL_USER_ADDRESS, USER_NAME, USER_PASSWORD);

        Properties props = System.getProperties();
        props.put("mail.smtp.host", LOCALHOST);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", ServerSetupTest.SMTP.getPort());
        Session session = Session.getInstance(props, null);

        MailSenderTest.session = (session);
        MailSenderTest.greenMail = greenMail;
    }

    @Before
    public void setup() {
        this.sender = new MailSender(session, LOCALHOST, EMAIL_USER_ADDRESS, USER_PASSWORD);
        greenMail.reset();
    }

    @Test
    public void sendEmail_arrivesViaGreenmail() throws MessagingException, IOException {
        sender.sendEmail("someone@somewhere.com", "something", "something else");

        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        Assert.assertEquals(receivedMessages.length, 1);

        MimeMessage email = receivedMessages[0];
        Assert.assertEquals(email.getContent().toString().trim(), "something else");
        Assert.assertEquals(email.getSubject().trim(), "something");
        Address[] recipients = email.getRecipients(Message.RecipientType.TO);

        Assert.assertEquals(recipients.length, 1);
        Assert.assertEquals(recipients[0].toString(), "someone@somewhere.com");

    }

}
