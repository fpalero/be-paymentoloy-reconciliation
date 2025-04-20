package com.paymentology.reconciliation.services.correlations.operations;

import org.springframework.stereotype.Service;

import com.paymentology.reconciliation.entities.Transaction;

@Service
public class WalletReferenceCorrelation extends AbstractStringDiffOpeartion {

    /**
     * Constructor for the WalletReferenceCorrelation class.
     */
    public WalletReferenceCorrelation() {
        super(0.5f);
    }

    @Override
    public float correlation(Transaction a, Transaction b) {
        return super.correlation(a.getWalletReference(), b.getWalletReference());
    }

}
