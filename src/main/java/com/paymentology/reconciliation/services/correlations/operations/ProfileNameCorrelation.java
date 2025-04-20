package com.paymentology.reconciliation.services.correlations.operations;

import org.springframework.stereotype.Service;

import com.paymentology.reconciliation.entities.Transaction;

@Service
public class ProfileNameCorrelation extends AbstractStringDiffOpeartion implements CorrelationOperation{

    @Override
    public float calculate(Transaction a, Transaction b) {
        return this.correlation(a.getProfileName(), b.getProfileName());
    }

}
