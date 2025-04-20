package com.paymentology.reconciliation.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.web.multipart.MultipartFile;

import com.paymentology.reconciliation.entities.Transaction;
import com.paymentology.reconciliation.exceptions.ReconciliationException;

public abstract class AbstractReconciliaitionService {

    /**
     * convert a MultipartFile to list and remove the csv first line, which contians
     * the columns header.
     * 
     * @param file
     * @return return a List where each entry is a line from the file.
     */
    protected List<Transaction> fileToTrasactions(MultipartFile file) {
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            // The first line is the header, it is not necessary to keep it.
            reader.readLine();
            transactions = reader.lines().map(line -> Transaction.newInstance(line)).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return transactions;
    }

    /**
     * convert a MultipartFile to list and remove the csv first line, which contians
     * the columns header.
     * 
     * @param file
     * @return return a List where each entry is a line from the file.
     */
    protected List<String> fileToList(MultipartFile file) {
        List<String> transactions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            // The first line is the header, it is not necessary to keep it.
            reader.readLine();
            transactions = reader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            throw new ReconciliationException("Wrong file format, it is not possible to read the file " + file.getOriginalFilename(), e);
        }

        return transactions;
    }

    /**
     * Cleans each line from the file removing white spaces, making all
     * characters lowercase and trim special characters.
     * 
     * @param file
     * @return return a Set where each entry is a cleaned line from the file.
     */
    protected Set<String> cleanList(List<String> list) {

        return list.stream()
                .map(this::cleanLine)
                .collect(Collectors.toSet());

    }

    protected String cleanLine(String element) {
        return element.trim().toLowerCase().replaceAll("\\s+", "");
    }
}
