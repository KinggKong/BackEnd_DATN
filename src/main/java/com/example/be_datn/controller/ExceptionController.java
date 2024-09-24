package com.example.be_datn.controller;

import com.example.be_datn.exception.BusinessException;
import com.example.be_datn.model.ResponseData;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletionException;

@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionController {
    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;

    @ExceptionHandler(CompletionException.class)
    public final ResponseEntity<Object> handleCompletionEx(CompletionException e) {
        if (e.getCause() instanceof BusinessException ex) {
            return handleBusinessException(ex);
        }
        return handleAllException(e);
    }

    @ExceptionHandler(BusinessException.class)
    public final ResponseEntity<Object> handleBusinessException(BusinessException e) {
      return  handle(HttpStatus.valueOf(e.getErrorCode().getHttpStatus()), e.getErrorCode().getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllException(Exception exception) {
      return handle(HttpStatus.INTERNAL_SERVER_ERROR, String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), exception.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return handle(HttpStatus.INTERNAL_SERVER_ERROR, String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), exception.getMessage(), null);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<Object> handleExceptionAccessDeniedException( AccessDeniedException exception) {
        return handle(HttpStatus.UNAUTHORIZED, String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), exception.getMessage(), null);
    }

    private ResponseEntity<Object> handle(HttpStatus httpStatus, String soaErrorCode, String soaErrorDesc, Object data) {
        ResponseData<Object> responseData = new ResponseData<>();
        responseData.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE));
        responseData.setStatus(httpStatus.value());
        responseData.setData(data);
        responseData.setError(httpStatus.getReasonPhrase());
        responseData.setSoaErrorDesc(soaErrorDesc);
        responseData.setSoaErrorCode(soaErrorCode);
        responseData.setPath(request.getContextPath());
        return new ResponseEntity<>(responseData, null, httpStatus);
    }
}
