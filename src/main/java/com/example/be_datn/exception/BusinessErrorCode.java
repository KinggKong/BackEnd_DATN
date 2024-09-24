package com.example.be_datn.exception;

import lombok.Value;

@Value
public class BusinessErrorCode {
    String code;
    String message;
    int httpStatus;
}
