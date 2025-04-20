package com.paymentology.reconciliation.services.correlations;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.paymentology.reconciliation.entities.Transaction;
import com.paymentology.reconciliation.services.AbstractReconciliaitionService;

@Service
public class CorrelationService extends AbstractReconciliaitionService {

    private final CalculateCorrelation calculateCorrelation;

    public CorrelationService(CalculateCorrelation calculateCorrelation) {
        this.calculateCorrelation = calculateCorrelation;
    }

    /**
     * Calculates the correlation between a transaction and a list of transactions.
     *
     * @param transaction     The transaction to compare.
     * @param transactionFile The file containing the list of transactions to
     *                        compare against.
     * @return A sorted list of transactions based on their correlation values.
     */
    public List<Transaction> correlation(Transaction transaction, MultipartFile transactionFile) {
        List<Transaction> transationLists = fileToTrasactions(transactionFile);

        transationLists.forEach(t -> t.setCorrelation(calculateCorrelation.calculate(transaction, t)));
        Collections.sort(transationLists, new CorrelarionComparator());
        return transationLists;
    }

    private static class CorrelarionComparator implements java.util.Comparator<Transaction> {

        @Override
        public int compare(Transaction o1, Transaction o2) {
            return Math.round((o1.getCorrelation() - o2.getCorrelation()) * 100);
        }

    }

}
