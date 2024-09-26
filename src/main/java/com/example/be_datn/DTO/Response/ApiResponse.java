package com.example.be_datn.DTO.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> implements Serializable {
    int code = 1000;
    String message;
    T data;

    public static <T> ApiResponse<T> ok(T data) {
        return restResult(data, HttpStatus.OK);
    }

    private static <T> ApiResponse<T> restResult(T data, HttpStatus httpStatus) {
        ApiResponse<T> apiResult = new ApiResponse<>();
        apiResult.setCode(httpStatus.value());
        apiResult.setData(data);
        return apiResult;
    }
}
