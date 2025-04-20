package com.paymentology.reconciliation.exceptions;

public class ReconciliationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ReconciliationException(String message) {
        super(message);
    }

    public ReconciliationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReconciliationException(Throwable cause) {
        super(cause);
    }

}
