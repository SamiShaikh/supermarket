package com.bnp.billing.exception;

public class BillingInputException extends RuntimeException {
    public BillingInputException(String message) {
        super(message);
    }

    public BillingInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
