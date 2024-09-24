package com.example.be_datn.exception;

import lombok.Getter;

@Getter
public class ValidateException extends RuntimeException {
    private final BusinessErrorCode errorCode;
    public ValidateException(BusinessErrorCode errorCode) {
        this.errorCode = errorCode;
    }
    public ValidateException(String message, BusinessErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
