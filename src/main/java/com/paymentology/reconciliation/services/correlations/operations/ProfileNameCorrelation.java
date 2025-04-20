package com.paymentology.reconciliation.services.correlations.operations;

import org.springframework.stereotype.Service;

import com.paymentology.reconciliation.entities.Transaction;

@Service
public class ProfileNameCorrelation extends AbstractStringDiffOpeartion {

    /**
     * Constructor for the ProfileNameCorrelation class.
     */
    public ProfileNameCorrelation() {
        super(0.5f);
    }

    @Override
    public float correlation (Transaction a, Transaction b) {
        return this.correlation(a.getProfileName(), b.getProfileName());
    }

}
