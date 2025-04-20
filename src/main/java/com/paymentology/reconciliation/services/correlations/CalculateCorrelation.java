package com.paymentology.reconciliation.services.correlations;

/**
 * Service for calculating the correlation between two transactions.
 * Uses a list of CorrelationOperation implementations to compute the correlation.
 */

import java.util.List;

import org.springframework.stereotype.Service;

import com.paymentology.reconciliation.entities.Transaction;
import com.paymentology.reconciliation.services.correlations.operations.CorrelationOperation;

@Service
public class CalculateCorrelation {
    List<CorrelationOperation> operations;

    public CalculateCorrelation(List<CorrelationOperation> operations) {
        this.operations = operations;
    }

    /**
     * Calculates the correlation between two transactions.
     *
     * @param ta The first transaction.
     * @param tb The second transaction.
     * @return A float value between 0 and 1 representing the correlation.
     *
     * The method works as follows:
     * 1. Each operation in the list calculates a correlation value between 0 and 1 for the given transactions.
     * 2. These values are summed up using a stream and the reduce function.
     * 3. The total sum is then divided by the number of operations to compute the average correlation,
     *    ensuring the final result is also between 0 and 1.
     */
    public float calculate(Transaction ta, Transaction tb) {
       
        // Calculate the sum of correlation values from all operations.
        Float correlation = operations.stream().map(operation -> operation.calculate(ta, tb)).reduce(0f, (a, b) -> a + b);

        return correlation;
    }
}
