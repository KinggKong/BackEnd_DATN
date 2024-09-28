package com.example.be_datn.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "UNCATEGORIZED ERROR"),
    INVALID_KEY(1001, "INVALID MESSAGE KEY"),
    MAUSAC_NOT_FOUND(1002,"Mausac Not Found"),
    MAUSAC_ALREADY_EXISTS(1003,"Mausac đã tồn tại !"),
    TEN_MAUSAC_INVALID(1004,"Tên màu sắc cần nhiều hơn 3 ký tự !"),
    CHATLIEUDE_NOT_FOUND(1005,"Chất liệu đế không tồn tại !"),
    CHATLIEUDE_ALREADY_EXISTS(1006,"Chất liệu đế đã tồn tại !"),
    TEN_CHATLIEUDE_INVALID(1007,"Tên chất liệu đế cần nhiều hơn 3 ký tự !"),

    KICHTHUOC_ALREADY_EXISTS(1010, "Kich Thuoc đã tồn tại!"),
    KICHTHUOC_NOT_FOUND(1011, "Kich Thuoc không tồn tại!");

    ;
    private int code;
    private String message;
}
