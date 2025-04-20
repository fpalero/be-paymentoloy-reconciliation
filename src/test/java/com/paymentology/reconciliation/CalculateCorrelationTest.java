package com.paymentology.reconciliation;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;

import com.paymentology.reconciliation.entities.Transaction;
import com.paymentology.reconciliation.services.correlations.CalculateCorrelation;

import com.paymentology.reconciliation.services.correlations.operations.CorrelationOperation;
import com.paymentology.reconciliation.services.correlations.operations.TransactionDateCorrelation;

@SpringBootTest
public class CalculateCorrelationTest {

    @Test
    @Description("Sanity test")
    public void sanity() {
        assertThat(new CalculateCorrelation(null)).isNotNull();
    }

    @Test
    @Description("CalculateCorrelation receive 2 transactions as input." +
            "The ouput expected is a correlation value between 0 and 1 (Smaller values means more similarity)")
    public void calculateCorrelation() {

        // Add differnt correlation operations
        List<CorrelationOperation> operations = Arrays.asList(
                new TestCorrelationOperation(0.1f),
                new TestCorrelationOperation(0.2f),
                new TestCorrelationOperation(0.3f));

        CalculateCorrelation calculateCorrelation = new CalculateCorrelation(operations);

        Transaction ta = new Transaction();
        ta.setTransactionAmount(1l);
        Transaction tb = new Transaction();
        tb.setTransactionAmount(2l);

        // Calculates the average correlation
        assertThat(calculateCorrelation.calculate(ta, tb)).isEqualTo(2.4f);
    }

    @Test
    @Description("Check if the String correlation operation is calculating well the correlation")
    public void stringCorrelation() {
        TestStringCorrelationOperation stringCorrelation = new TestStringCorrelationOperation(100f);

        assertThat(stringCorrelation.correlation("hola", "hola")).isEqualTo(.0f);
        assertThat(stringCorrelation.correlation("hola", "adios")).isEqualTo(1.0f);
    }

    @Test
    @Description("Check if the Number correlation operation is calculating well the correlation")
    public void numberCorrelation() {
        TestNumberCorrelationOperation numberCorrelation = new TestNumberCorrelationOperation(1.0f);

        Transaction transactionA = new Transaction();
        transactionA.setTransactionAmount(-20000l);
        Transaction transactionB = new Transaction();
        transactionB.setTransactionAmount(-15000l);
        // It doesn't metter the value the order, the correlation should be the same
        assertThat(numberCorrelation.calculate(transactionA, transactionB)).isEqualTo(.25f);
        assertThat(numberCorrelation.calculate(transactionA, transactionB)).isEqualTo(.25f);
        // If the number is possible or negative should affecte the correlation.
        // If it is negetive then it is a Deduct and if it is possitive then it is a
        // Reversal, the correlation should be 1.
        Transaction transactionC = new Transaction();
        transactionC.setTransactionAmount(20000l);
        Transaction transactionD = new Transaction();
        transactionD.setTransactionAmount(15000l);
        assertThat(numberCorrelation.calculate(transactionC, transactionB)).isEqualTo(1f);
        assertThat(numberCorrelation.calculate(transactionD, transactionA)).isEqualTo(1f);
    }

    @Test
    @Description("Check if the Date correlation operation is calculating well the correlation")
    public void dateCorrelation() {
        TransactionDateCorrelation dateCorrelation = new TransactionDateCorrelation();
        Transaction transactionA = new Transaction();
        Transaction transactionB = new Transaction();

        transactionA
                .setTransactionDate(LocalDateTime.ofInstant(Instant.parse("2018-11-30T18:35:24.00Z"), ZoneOffset.UTC));
        transactionB
                .setTransactionDate(LocalDateTime.ofInstant(Instant.parse("2018-12-31T18:35:24.00Z"), ZoneOffset.UTC));
        // If the transactions have a difference bigger or equal than one they should
        // return correlation * weight
        // The weight is 0.15f, so the correlation should be 1 * 0.15f = 0.15f
        assertThat(dateCorrelation.calculate(transactionA, transactionB)).isEqualTo(0.15f);

        transactionA
                .setTransactionDate(LocalDateTime.ofInstant(Instant.parse("2018-11-30T18:35:24.00Z"), ZoneOffset.UTC));
        transactionB
                .setTransactionDate(LocalDateTime.ofInstant(Instant.parse("2018-11-30T10:35:24.00Z"), ZoneOffset.UTC));
        // If the transactions date have a difference lower than one day the correlation
        // should be lower than 0.15
        assertThat(dateCorrelation.calculate(transactionA, transactionB)).isEqualTo(0.050000004f);

        transactionA
                .setTransactionDate(LocalDateTime.ofInstant(Instant.parse("2018-11-30T18:35:24.00Z"), ZoneOffset.UTC));
        transactionB
                .setTransactionDate(LocalDateTime.ofInstant(Instant.parse("2018-11-30T18:35:24.00Z"), ZoneOffset.UTC));
        // If the transactions date is equal the correlation should be 0
        assertThat(dateCorrelation.calculate(transactionA, transactionB)).isEqualTo(0.0f);
    }

}
