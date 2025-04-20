package com.paymentology.reconciliation;

import com.paymentology.reconciliation.entities.Transaction;
import com.paymentology.reconciliation.services.correlations.operations.CorrelationOperation;

public class TestCorrelationOperation implements CorrelationOperation {
    private float testValue;

    public TestCorrelationOperation(float testValue) {
        this.testValue = testValue;
    }

    @Override
    public float calculate(Transaction a, Transaction b) {
        return Math.abs(a.getTransactionAmount() - b.getTransactionAmount()) - testValue;
    }

}
