package com.paymentology.reconciliation.services.correlations.operations;

import org.springframework.stereotype.Service;

import com.paymentology.reconciliation.entities.Transaction;

/**
 * Calculates the correlation between the transaction amounts of two
 * transactions.
 */
@Service
public class TransactionNarrativeCorrelation extends AbstractStringDiffOpeartion {
    
    /**
     * Constructor for the TransactionNarrativeCorrelation class.
     */
    public TransactionNarrativeCorrelation() {
        super(0.15f);
    }


    @Override
    public float correlation(Transaction a, Transaction b) {
        return this.correlation(a.getTransactionNarrative(), b.getTransactionNarrative());
    }

}
