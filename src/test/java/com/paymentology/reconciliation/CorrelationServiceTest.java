package com.paymentology.reconciliation;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;

import com.paymentology.reconciliation.entities.Transaction;
import com.paymentology.reconciliation.services.correlations.CalculateCorrelation;
import com.paymentology.reconciliation.services.correlations.CorrelationService;
import com.paymentology.reconciliation.services.correlations.operations.CorrelationOperation;
import com.paymentology.reconciliation.services.correlations.operations.ProfileNameCorrelation;
import com.paymentology.reconciliation.services.correlations.operations.TransactionAmountCorrelation;
import com.paymentology.reconciliation.services.correlations.operations.TransactionDateCorrelation;
import com.paymentology.reconciliation.services.correlations.operations.TransactionDescriptionCorrelation;
import com.paymentology.reconciliation.services.correlations.operations.TransactionIdCorrelation;
import com.paymentology.reconciliation.services.correlations.operations.TransactionNarrativeCorrelation;
import com.paymentology.reconciliation.services.correlations.operations.TransactionTypeCorrelation;
import com.paymentology.reconciliation.services.correlations.operations.WalletReferenceCorrelation;

@SpringBootTest
public class CorrelationServiceTest {

    @Test
    @Description("Sanity test")
    public void sanity() {
        assertThat(new CorrelationService(new CalculateCorrelation(null))).isNotNull();
    }

    @Test
    @Description("CorrelationService receive a transaction and a list of transanction as input." +
            "The ouput expected is a list of transactions ordered by the correlation value." +
            "The list is order from smaller correlation value to bigger. (Smaller values means more similarity)")
    public void correlationService() throws IOException {
        // Add differnt correlation operations
        List<CorrelationOperation> operations = Arrays.asList(
                new ProfileNameCorrelation(),
                new TransactionAmountCorrelation(),
                new TransactionDateCorrelation(),
                new TransactionDateCorrelation(),
                new TransactionDescriptionCorrelation(),
                new TransactionIdCorrelation(),
                new TransactionNarrativeCorrelation(),
                new TransactionTypeCorrelation(),
                new WalletReferenceCorrelation());

        CalculateCorrelation calculateCorrelation = new CalculateCorrelation(operations);
        CorrelationService correlationService = new CorrelationService(calculateCorrelation);

        MockMultipartFile multipartFile = new MockMultipartFile("afile", getTransactions().getContentAsByteArray());
       
        // Calculates the average correlation
        assertThat(correlationService.correlation(getTransactionA(), multipartFile))
        .usingRecursiveComparison()
        .isEqualTo(expectedTransactionList());
    }

    private ClassPathResource getTransactions() {
		return new org.springframework.core.io.ClassPathResource("./transactionsCorrelation.csv");
	}

    public Transaction getTransactionA() {
        Transaction ta = new Transaction();
        ta.setProfileName("Card Campaign");
        ta.setTransactionDate(LocalDateTime.ofInstant(Instant.parse("2014-01-11T22:27:44.00Z"), ZoneOffset.UTC));
        ta.setTransactionAmount(-20000l);
        ta.setTransactionNarrative("*MOLEPS ATM25             MOLEPOLOLE    BW");
        ta.setTransactionDescription("DEDUCT");
        ta.setTransactionID(584011808649511l);
        ta.setTransactionType(1);
        ta.setWalletReference("P_NzI2ODY2ODlfMTM4MjcwMTU2NS45MzA5");

        return ta;
    }

    public Transaction getTransactionB() {
        Transaction tb = new Transaction();
        tb.setProfileName("Card Campaign");
        tb.setTransactionDate(LocalDateTime.ofInstant(Instant.parse("2014-01-11T22:39:11.00Z"), ZoneOffset.UTC));
        tb.setTransactionAmount(-10000l);
        tb.setTransactionNarrative("*MOGODITSHANE2            MOGODITHSANE  BW");
        tb.setTransactionDescription("DEDUCT");
        tb.setTransactionID(584011815513406l);
        tb.setTransactionType(1);
        tb.setWalletReference("P_NzI1MjA1NjZfMTM3ODczODI3Mi4wNzY5");

        return tb;
    }

    public Transaction getTransactionC() {
        Transaction tc = new Transaction();
        tc.setProfileName("Card Campaign");
        tc.setTransactionDate(LocalDateTime.ofInstant(Instant.parse("2014-01-11T23:28:11.00Z"), ZoneOffset.UTC));
        tc.setTransactionAmount(-5000l);
        tc.setTransactionNarrative("CAPITAL BANK              MOGODITSHANE  BW");
        tc.setTransactionDescription("DEDUCT");
        tc.setTransactionID(464011844938429l);
        tc.setTransactionType(1);
        tc.setWalletReference("P_NzI0NjE1NzhfMTM4NzE4ODExOC43NTYy");

        return tc;
    }

    public List<Transaction> expectedTransactionList() {
        Transaction tb = getTransactionB();
        tb.setCorrelation(0.3157823f);

        Transaction tc =getTransactionC();
        tc.setCorrelation(0.5073061f);
        
        List<Transaction> tlist = new ArrayList<>();
        tlist.add(tb);
        tlist.add(tc);

        return tlist;
    }

}
