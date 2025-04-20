package com.paymentology.reconciliation.services.correlations.operations;

import com.paymentology.reconciliation.entities.Transaction;

public interface CorrelationOperation {

    /**
     * Calculates the correlation between 2 fields. It means how similar are the fields.
     * @param a field of type Transaction
     * @param b field of type    Transaction
     * @return values between 0 and 1. The value 0 is the highest similariy and the value 1 the lowest similarity
     */
    float calculate(Transaction a, Transaction b);
}
