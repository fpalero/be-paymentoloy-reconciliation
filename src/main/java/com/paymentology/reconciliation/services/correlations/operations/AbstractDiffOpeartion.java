package com.paymentology.reconciliation.services.correlations.operations;

/**
 * Abstract class for calculating the correlation between two values.
 * Uses a BiFunction to define the correlation logic.
 *
 * @param <T> The type of the values being compared.
 */

import java.util.function.BiFunction;

public abstract class AbstractDiffOpeartion<T> {

    private final BiFunction<T,T, Float> correlationfunc;

    public AbstractDiffOpeartion(BiFunction<T,T, Float> correlationfunc) {
        this.correlationfunc = correlationfunc;
    }

    /**
     * Remove special characters, whatspaces and convert all to lower case and after this calculates the correlation.
     * @param a String
     * @param b String
     * @return return 0 if is equal and 1 if not
     */
    public float correlation(T a, T b) {
        return correlationfunc.apply(a,b);
    }

}
