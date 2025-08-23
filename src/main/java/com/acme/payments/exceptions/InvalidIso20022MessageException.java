package com.acme.payments.exceptions;

import lombok.Getter;

@Getter
public class InvalidIso20022MessageException extends RuntimeException {
    private final String errorCode;

    public InvalidIso20022MessageException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public InvalidIso20022MessageException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

}
