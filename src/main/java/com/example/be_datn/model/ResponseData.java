package com.example.be_datn.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseData<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private int status;
    private String error;
    private String timestamp;
    private String soaErrorCode;
    private String soaErrorDesc;
    private String path;
    private T data;

    public static <T> ResponseData<T> ok(T data, String path) {
        return restResult(data, HttpStatus.OK, null, null, path);
    }

    public static <T> ResponseData<T> ok (String path) {
        return restResult(null, HttpStatus.OK, null, null, path);
    }

    public static <T> ResponseData<T> failed(HttpStatus httpStatus, String soaErrorCode, String soaErrorDesc, T data, String path) {
        return restResult(data, httpStatus, soaErrorCode, soaErrorDesc, path);
    }

    private static <T> ResponseData<T> restResult(T data, HttpStatus httpStatus, String soaErrorCode, String soaErrorDesc, String path) {
        ResponseData<T> apiResult = new ResponseData<>();
        apiResult.setSoaErrorCode(soaErrorCode);
        apiResult.setSoaErrorDesc(soaErrorDesc);
        apiResult.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE));
        apiResult.setData(data);
        apiResult.setStatus(httpStatus.value());
        apiResult.setError(httpStatus.getReasonPhrase());
        apiResult.setPath(path);
        return apiResult;
    }
}
