package com.paymentology.reconciliation;

import com.paymentology.reconciliation.entities.Transaction;
import com.paymentology.reconciliation.services.correlations.operations.AbstractNumberDiffOpeartion;

public class TestNumberCorrelationOperation extends AbstractNumberDiffOpeartion {

    public TestNumberCorrelationOperation(float weight) {
        super(weight);
    }

    @Override
    public float correlation(Transaction a, Transaction b) {
        return this.correlation(a.getTransactionAmount().floatValue(), b.getTransactionAmount().floatValue());
    }

}
