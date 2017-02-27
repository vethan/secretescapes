package com.secretescapes.codingChallenge;

import com.secretescapes.codingChallenge.consumers.PaymentConsumer;
import com.secretescapes.codingChallenge.email.MailSender;
import com.secretescapes.codingChallenge.model.Account;
import com.secretescapes.codingChallenge.modules.GreenmailModule;
import com.secretescapes.codingChallenge.persistence.AccountPersistance;
import com.secretescapes.codingChallenge.persistence.TransactionPersistance;
import org.jukito.JukitoRunner;
import org.jukito.UseModules;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import static org.mockito.Mockito.*;

@RunWith(JukitoRunner.class)
@UseModules(GreenmailModule.class)
public class PaymentConsumerTest {
    private AccountPersistance accountPersistance;
    private TransactionPersistance transactionPersistance;
    private MailSender mailer;
    private PaymentConsumer consumer;
    private Account fromAccount;
    private Account toAccount;

    @Before
    public void setup() {
        fromAccount = new Account();
        toAccount = new Account();

        this.mailer = mock(MailSender.class);
        this.accountPersistance = mock(AccountPersistance.class);
        this.transactionPersistance = mock(TransactionPersistance.class);


        when(accountPersistance.getAccount(eq("from"))).thenReturn(fromAccount);
        when(accountPersistance.getAccount(eq("to"))).thenReturn(toAccount);

        this.consumer = new PaymentConsumer(accountPersistance, transactionPersistance, mailer);
    }


    @Test
    public void newTransaction_validParameters_sendsMailAndUpdatesPersistence() {
        fromAccount.balance = 20;
        toAccount.balance = 0;
        this.consumer.newTransaction("from", "to", 20);

        verify(accountPersistance, times(2)).updateAccount(any());
        verify(transactionPersistance, times(1)).storeNewTransaction(any());
        verify(mailer, times(2)).sendEmail(any(), any(), any());
    }


    @Test
    public void newTransaction_tooGreatBalanceTransfer_noMailAndNoPersistenceAndThrows() {
        fromAccount.balance = 19;
        toAccount.balance = 0;
        this.consumer.newTransaction("from", "to", 20);

        verify(accountPersistance, never()).updateAccount(any());
        verify(transactionPersistance, never()).storeNewTransaction(any());
        verify(mailer, never()).sendEmail(any(), any(), any());
    }

    @Test
    public void newTransaction_transferNonPositiveAmount_noMailAndNoPersistenceAndThrows() {
        this.consumer.newTransaction("from", "to", 0);

        verify(accountPersistance, never()).updateAccount(any());
        verify(transactionPersistance, never()).storeNewTransaction(any());
        verify(mailer, never()).sendEmail(any(), any(), any());
    }

}
