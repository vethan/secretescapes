package com.secretescapes.codingChallenge.email;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.sun.mail.smtp.SMTPTransport;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.logging.Logger;

public class MailSender {
    private Session mailSession;
    private String host;
    private final String fromAddress;
    private final String password;

    @Inject
    public MailSender(Session mailSession
            , @Named("email_host") String host
            , @Named("email_user_address") String fromAddress
            , @Named("email_user_password") String password) {

        this.mailSession = mailSession;
        this.host = host;
        this.fromAddress = fromAddress;
        this.password = password;
    }

    public void SendEmail(String toAddress, String subject, String body) {
        try {
            Message msg = new MimeMessage(mailSession);
            msg.setFrom(new InternetAddress(toAddress));
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(fromAddress, false));
            msg.setSubject(subject);
            msg.setText(body);
            msg.setSentDate(new Date());
            SMTPTransport t = (SMTPTransport) mailSession.getTransport("smtp");
            t.connect(host, fromAddress, password);
            t.sendMessage(msg, msg.getAllRecipients());
            t.close();
        } catch (Exception e) {
            //TODO: Alert Tech team email sending failed
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
