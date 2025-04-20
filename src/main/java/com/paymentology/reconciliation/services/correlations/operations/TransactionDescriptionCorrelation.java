package com.paymentology.reconciliation.services.correlations.operations;

import org.springframework.stereotype.Service;

import com.paymentology.reconciliation.entities.Transaction;

@Service
public class TransactionDescriptionCorrelation extends AbstractStringDiffOpeartion{

    /**
     * Constructor for the TransactionDescriptionCorrelation class.
     */
    public TransactionDescriptionCorrelation() {
        super(0.10f);
    }

    @Override
    public float correlation(Transaction a, Transaction b) {
        return this.correlation(a.getTransactionDescription(), b.getTransactionDescription());
    }

}
