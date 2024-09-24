package com.example.be_datn.utils;


import com.example.be_datn.exception.BusinessErrorCode;

public class ErrorCode {
    public static final BusinessErrorCode CONFIG_NOT_FOUND =
            new BusinessErrorCode("4003", "config not found", 404);
    public static final BusinessErrorCode CHAT_LIEU_VAI_EXIST =
            new BusinessErrorCode("1001", "Chat luong vai exist", 404);
    public static final BusinessErrorCode CHAT_LIEU_VAI_NOT_EXIST =
            new BusinessErrorCode("1002", "Chat luong vai not exist", 404);
    public static final BusinessErrorCode INTERNAL_SERVER_ERROR =
            new BusinessErrorCode("5001", "internal server error", 500);
    public static final BusinessErrorCode INVALID_PARAMETERS =
            new BusinessErrorCode("4000", "invalid parameters", 400);
    public static final BusinessErrorCode UNAUTHORIZED =
            new BusinessErrorCode("4001", "You need to login to to access this resource", 401);
    public static final BusinessErrorCode FORBIDDEN =
            new BusinessErrorCode("4002", "You don't have permission to to access this resource", 403);

private ErrorCode() {throw new UnsupportedOperationException();}
}