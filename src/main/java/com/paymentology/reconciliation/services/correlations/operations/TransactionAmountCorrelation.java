package com.paymentology.reconciliation.services.correlations.operations;

import org.springframework.stereotype.Service;

import com.paymentology.reconciliation.entities.Transaction;

/**
 * Calculates the correlation between the transaction amounts of two
 * transactions.
 */

@Service
public class TransactionAmountCorrelation extends AbstractNumberDiffOpeartion {


    public TransactionAmountCorrelation() {
        super(0.10f);
    }

    @Override
    public float correlation(Transaction a, Transaction b) {
        return super.correlation(a.getTransactionAmount().floatValue(), b.getTransactionAmount().floatValue());
    }

}
