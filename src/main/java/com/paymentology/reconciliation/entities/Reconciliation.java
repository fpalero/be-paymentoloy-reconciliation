package com.paymentology.reconciliation.entities;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Reconciliation {
    private String fileName;
    private long totalRecords;
    private long matchedRecords;
    private long unmatchedRecords;
    private List<Transaction> unmatchedList;
}
