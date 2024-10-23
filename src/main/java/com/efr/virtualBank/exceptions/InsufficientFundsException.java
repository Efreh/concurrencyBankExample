package com.efr.virtualBank.exceptions;

/**
 * Исключение, выбрасываемое при недостатке средств на счете для перевода.
 */
public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
