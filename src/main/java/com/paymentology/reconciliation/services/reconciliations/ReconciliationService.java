package com.paymentology.reconciliation.services.reconciliations;

import com.paymentology.reconciliation.entities.Reconciliation;
import com.paymentology.reconciliation.entities.Transaction;
import com.paymentology.reconciliation.entities.TransactionReconciliation;
import com.paymentology.reconciliation.services.AbstractReconciliaitionService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ReconciliationService extends AbstractReconciliaitionService {

    /**
     * transactionReconciliation method calculates first the reconciliation of file
     * A on file B and later the reconcilaion from file B on file A.
     * The reconsilation steps are:
     * 1- Clean the file lines, each line represents a transaction.
     * 2- Onces the lines are clean, the method checks which transactions are
     * matched or unmatched.
     * 3- Create a report with the reconciliation calcualted.
     * 
     * @param fileA contains a list of transactions
     * @param fileB contains a list of transactions
     * @return the calculate reconciliation from the files A and B.
     */
    public TransactionReconciliation transactionReconciliation(MultipartFile fileA, MultipartFile fileB) {
        List<String> transactionsA = fileToList(fileA);
        List<String> transactionsB = fileToList(fileB);

        Reconciliation reconciliationA = reconciliation(transactionsA, cleanList(transactionsB));
        Reconciliation reconciliationB = reconciliation(transactionsB, cleanList(transactionsA));

        TransactionReconciliation transactionReconciliation = new TransactionReconciliation();
        
        reconciliationA.setFileName(fileA.getOriginalFilename());
        reconciliationB.setFileName(fileB.getOriginalFilename());

        transactionReconciliation.setComparationA(reconciliationA);
        transactionReconciliation.setComparationB(reconciliationB);

        return transactionReconciliation;

    }

    private Reconciliation reconciliation(List<String> listTransactionsA, Set<String> cleanedTransactionsB) {
        Reconciliation reconciliation = new Reconciliation();

        List<Transaction> unmatchedTrasactionList = listTransactionsA.stream()
                .filter(transactionA -> !cleanedTransactionsB.contains(cleanLine(transactionA)))
                .map(transactionA -> Transaction.newInstance(transactionA))
                .collect(Collectors.toList());

        long totalUnmachedRecords = unmatchedTrasactionList.size();
        long totalRecords = listTransactionsA.size();

        reconciliation.setTotalRecords(totalRecords);
        reconciliation.setUnmatchedRecords(totalUnmachedRecords);
        reconciliation.setMatchedRecords(totalRecords - totalUnmachedRecords);
        reconciliation.setUnmatchedList(unmatchedTrasactionList);

        return reconciliation;
    }

}