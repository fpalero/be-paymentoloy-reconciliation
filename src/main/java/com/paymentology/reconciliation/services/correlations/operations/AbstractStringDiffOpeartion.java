package com.paymentology.reconciliation.services.correlations.operations;

/**
 * Abstract class for calculating the correlation between two strings.
 * Provides a default implementation for cleaning strings before comparison.
 */

public abstract class AbstractStringDiffOpeartion extends AbstractDiffOpeartion<String> {

    public AbstractStringDiffOpeartion() {
        super((a, b) -> a.equals(b)?0f:1f);
    }

    /**
     * Remove special characters, whatspaces and convert all to lower case and after this calculates the correlation.
     * @param a String
     * @param b String
     * @return return 0 if is equal and 1 if not
     */
    public float correlation(String a, String b) {
        return super.correlation(cleanString(a),cleanString(b));
    }

    private String cleanString(String element) {
        return element.trim().toLowerCase().replaceAll("\\s+", "");
    }

}
