package com.paymentology.reconciliation.services.correlations.operations;

import org.springframework.stereotype.Service;

import com.paymentology.reconciliation.entities.Transaction;

@Service
public class TransactionIdCorrelation extends AbstractStringDiffOpeartion {

    /**
     * Constructor for the TransactionIdCorrelation class.
     */
    public TransactionIdCorrelation() {
        super(0.30f);
    }

    @Override
    public float correlation (Transaction a, Transaction b) {
        return super.correlation(a.getTransactionID().toString(), b.getTransactionID().toString());
    }

}
