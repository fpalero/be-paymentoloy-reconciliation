package com.paymentology.reconciliation.services.correlations.operations;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import de.uni_jena.cs.fusion.similarity.jarowinkler.JaroWinklerSimilarity;

/**
 * Abstract class for calculating the correlation between two strings.
 * Provides a default implementation for cleaning strings before comparison.
 */

public abstract class AbstractStringDiffOpeartion extends AbstractDiffOpeartion<String> {

    public AbstractStringDiffOpeartion(float weight) {
        // The JaroWinklerSimilarity algorithm is a string comparison algorithm
        // that calculates the similarity between two strings based on the number
        // of common characters and the order of those characters.
        // It is particularly useful for comparing strings that may have minor
        // typographical errors or variations in spelling.
        super((a, b) -> {
            List<String> terms = Arrays.asList(a);
            // prepare the JaroWinklerSimilarity instance
            JaroWinklerSimilarity<String> jws = JaroWinklerSimilarity.with(terms, 0.0);
            // calculate string distance
            Map<String, Double> similarStrings = jws.apply(b);
            // get the most similar string
            return (1f - similarStrings.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getValue)
                    .orElse(1.0).floatValue());
        },
        
        weight);
    }

    /**
     * Remove special characters, whatspaces and convert all to lower case and after
     * this calculates the correlation.
     * 
     * @param a String
     * @param b String
     * @return return 0 if is equal and 1 if not
     */
    public float correlation(String a, String b) {
        return super.correlation(cleanString(a), cleanString(b));
    }

    private String cleanString(String element) {
        return element.trim().toLowerCase().replaceAll("\\s+", "");
    }

}
