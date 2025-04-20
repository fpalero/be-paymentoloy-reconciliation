package com.paymentology.reconciliation.services.correlations.operations;

import org.springframework.stereotype.Service;

import com.paymentology.reconciliation.entities.Transaction;

/**
 * Calculates the correlation between the transaction amounts of two
 * transactions.
 */

@Service
public class TransactionAmountCorrelation extends AbstractNumberDiffOpeartion implements CorrelationOperation {

    @Override
    public float calculate(Transaction a, Transaction b) {
        return super.correlation(a.getTransactionAmount().floatValue(), b.getTransactionAmount().floatValue());
    }

}
