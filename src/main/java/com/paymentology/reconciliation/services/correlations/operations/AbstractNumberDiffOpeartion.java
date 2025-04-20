package com.paymentology.reconciliation.services.correlations.operations;

public abstract class AbstractNumberDiffOpeartion extends AbstractDiffOpeartion<Float> {

    public AbstractNumberDiffOpeartion() {
        super((a, b) -> Math.min(1, Math.abs(a-b) / Math.max(Math.abs(a), Math.abs(b))));
    }

    /**
     * Remove special characters, whatspaces and convert all to lower case and after this calculates the correlation.
     * @param a String
     * @param b String
     * @return return 0 if is equal and 1 if not
     */
    public float correlation(Float a, Float b) {
        return super.correlation(a, b);
    }

}
