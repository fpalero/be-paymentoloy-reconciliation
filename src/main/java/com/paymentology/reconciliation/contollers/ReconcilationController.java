package com.paymentology.reconciliation.contollers;

/**
 * Entry point for the Reconciliation Application.
 * This class initializes and runs the Spring Boot application.
 */

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.paymentology.reconciliation.entities.Transaction;
import com.paymentology.reconciliation.entities.TransactionReconciliation;
import com.paymentology.reconciliation.exceptions.ReconciliationException;
import com.paymentology.reconciliation.services.correlations.CorrelationService;
import com.paymentology.reconciliation.services.reconciliations.ReconciliationService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/reconciliations")
@AllArgsConstructor
public class ReconcilationController {

    private final ReconciliationService compareTransactionsService;
    private final CorrelationService correlationService;

    @PostMapping(path = "/compares", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }, produces = "application/json")
    public ResponseEntity<TransactionReconciliation> compares(
            @RequestParam(name = "afile") MultipartFile afile,
            @RequestParam(name = "bfile") MultipartFile bfile) {
        return new ResponseEntity<>(compareTransactionsService.transactionReconciliation(afile, bfile), HttpStatus.OK);
    }

    @PostMapping(path = "/correlations", consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE }, produces = "application/json")
    public ResponseEntity<List<Transaction>> correlations(
            @ModelAttribute(name = "transaction") Transaction transaction,
            @RequestParam(name = "file") MultipartFile file) {

        return new ResponseEntity<>(correlationService.correlation(transaction, file), HttpStatus.OK);
    }
    
}
