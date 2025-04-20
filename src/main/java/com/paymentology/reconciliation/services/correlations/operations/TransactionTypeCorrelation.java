package com.paymentology.reconciliation.services.correlations.operations;

import org.springframework.stereotype.Service;

import com.paymentology.reconciliation.entities.Transaction;

@Service
public class TransactionTypeCorrelation extends AbstractStringDiffOpeartion {

    /**
     * Constructor for the TransactionTypeCorrelation class.
     */
    public TransactionTypeCorrelation() {
        super(0.10f);
    }

    @Override
    public float correlation(Transaction a, Transaction b) {
        return super.correlation(a.getTransactionType().toString(), b.getTransactionType().toString());
    }

}
