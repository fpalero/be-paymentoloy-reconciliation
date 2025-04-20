package com.paymentology.reconciliation.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.paymentology.reconciliation.configurations.JacksonConfiguration;

import lombok.Data;

@Data
public class Transaction implements Serializable {

    /**
     * Creates a new Transaction instance from a JSON string.
     *
     * @param json A JSON string containing transaction data.
     * @return A new Transaction object.
     * @throws JsonMappingException    If the JSON cannot be mapped to a
     *                                 Transaction.
     * @throws JsonProcessingException If there is an error processing the JSON.
     */
    public static Transaction of(String json) throws JsonMappingException, JsonProcessingException {
        JsonMapper jsonMapper = (JsonMapper) JacksonConfiguration.objectMapperInstance();
        return jsonMapper.readValue(json, Transaction.class);
    }

    /**
     * Creates a new Transaction instance from a CSV line.
     *
     * @param line A CSV line containing transaction data.
     * @return A new Transaction object.
     */
    public static Transaction newInstance(String line) {
        String fileds[] = line.split(",");
        Transaction t = new Transaction();

        t.profileName = fileds[0];

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            t.transactionDate = LocalDateTime.parse(fileds[1], formatter);
        } catch (Exception e) {
            t.transactionDate = LocalDateTime.MIN; // Default value for transactionDate
        }

        try {
            t.transactionAmount = Long.parseLong(fileds[2]);
        } catch (Exception e) {
            t.transactionAmount = 0L; // Default value for transactionAmount
        }

        try {
            t.transactionNarrative = fileds[3];
        } catch (ArrayIndexOutOfBoundsException e) {
            t.transactionNarrative = ""; // Default value for transactionNarrative
        }

        try {
            t.transactionDescription = fileds[4];
        } catch (ArrayIndexOutOfBoundsException e) {
            t.transactionDescription = ""; // Default value for transactionDescription
        }

        try {
            t.transactionID = Long.parseLong(fileds[5]);
        } catch (Exception e) {
            t.transactionID = 0L; // Default value for transactionID
        }

        try {
            t.transactionType = Integer.parseInt(fileds[6]);
        } catch (Exception e) {
            t.transactionType = 0; // Default value for transactionType
        }
        
        try {
            t.walletReference = fileds[7];
        } catch (ArrayIndexOutOfBoundsException e) {
            t.walletReference = ""; // Default value for walletReference
        }

        return t;
    }

    private String profileName;
    private LocalDateTime transactionDate;
    private Long transactionAmount;
    private String transactionNarrative;
    private String transactionDescription;
    private Long transactionID;
    private Integer transactionType;
    private String walletReference;
    private Float correlation;
}
