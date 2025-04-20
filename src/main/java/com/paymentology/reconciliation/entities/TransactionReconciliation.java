package com.paymentology.reconciliation.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionReconciliation {
    private Reconciliation comparationA;
    private Reconciliation comparationB;

}
