package com.paymentology.reconciliation.exceptions;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Configuration
public class ExceptionConfiguration {
    /**
     * Handle ReconciliationException and return a 400 Bad Request status.
     *
     * @param e the ReconciliationException
     */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ReconciliationException.class)
    public void exceptionHandling() {
        // Nothing to do
    }

}
