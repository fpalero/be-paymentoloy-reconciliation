package com.paymentology.reconciliation.services.correlations.operations;

import com.paymentology.reconciliation.entities.Transaction;

public abstract class AbstractCorrelationOperation implements CorrelationOperation{
    private final float weight;

    /**
     * Constructor for the AbstractCorrelationOperation class.
     *
     * @param weight The weight of the correlation operation.
     */
    public AbstractCorrelationOperation(float weight) {
        this.weight = weight;  
    }

    public abstract float correlation(Transaction a, Transaction b);
   
    public  float calculate(Transaction a, Transaction b) {
        float correlation = correlation(a, b);
        return correlation * weight;
    }

}
