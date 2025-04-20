package com.paymentology.reconciliation;

import com.paymentology.reconciliation.entities.Transaction;
import com.paymentology.reconciliation.services.correlations.operations.AbstractStringDiffOpeartion;

public class TestStringCorrelationOperation extends AbstractStringDiffOpeartion {

    public TestStringCorrelationOperation(float weight) {
        super(weight);
    }

    @Override
    public float correlation(Transaction a, Transaction b) {
        return this.correlation(a.getProfileName(), b.getProfileName());
    }

}
