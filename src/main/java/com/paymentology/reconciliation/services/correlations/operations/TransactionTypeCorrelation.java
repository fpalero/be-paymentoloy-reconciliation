package com.paymentology.reconciliation.services.correlations.operations;

import org.springframework.stereotype.Service;

import com.paymentology.reconciliation.entities.Transaction;

@Service
public class TransactionTypeCorrelation extends AbstractStringDiffOpeartion implements CorrelationOperation{

    @Override
    public float calculate(Transaction a, Transaction b) {
        return super.correlation(a.getTransactionType().toString(), b.getTransactionType().toString());
    }

}
