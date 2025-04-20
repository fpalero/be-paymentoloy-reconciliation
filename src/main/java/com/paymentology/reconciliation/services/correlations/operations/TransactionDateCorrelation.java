package com.paymentology.reconciliation.services.correlations.operations;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.paymentology.reconciliation.entities.Transaction;

@Service
public class TransactionDateCorrelation implements CorrelationOperation {
    private final static float DAY_MILLIS = TimeUnit.DAYS.toMillis(1);

    /**
     * Calculates the correlation between the transaction dates of two transactions.
     * If the difference exceeds one day, the correlation is set to 1 (low
     * similarity).
     *
     * @param a The first transaction.
     * @param b The second transaction.
     * @return A float value between 0 and 1 representing the correlation.
     */
    @Override
    public float calculate(Transaction a, Transaction b) {
        LocalDateTime dateA = a.getTransactionDate();
        LocalDateTime dateB = b.getTransactionDate();
        long millisDateA = dateA.atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
        long millisDateB = dateB.atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
        float timediff = Math.abs(millisDateA - millisDateB);

        // I am assiming that a maximum delay that could have a transaction is one day.
        // So, if the difference is bigger thant 1 day, the probability that is the same
        // transation is low.
        // The value of DAY_MILLIS can be changed for a more realistic.
        if (timediff >= DAY_MILLIS) {
            return 1.0f;
        }

        // This return a value between 0 and 1
        return timediff / DAY_MILLIS;
    }

}
