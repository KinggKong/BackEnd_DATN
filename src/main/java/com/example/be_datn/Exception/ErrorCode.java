package com.example.be_datn.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "UNCATEGORIZED ERROR"),
    INVALID_KEY(1001, "INVALID MESSAGE KEY"),
    MAUSAC_NOT_FOUND(1002,"Mausac Not Found"),
    MAUSAC_ALREADY_EXISTS(1003,"Mausac đã tồn tại !"),
    TEN_MAUSAC_INVALID(1004,"Tên màu sắc cần nhiều hơn 3 ký tự !"),
    HANG_ALREADY_EXISTS(1005,"DanhMuc da ton tai!"),
    HANG_NOT_FOUND(1006,"DanhMuc khong ton tai"),
    THUONGHIEU_ALREADY_EXISTS(1007,"Thuong hieu da ton tai !"),
    THUONGHIEU_NOT_FOUND(1008,"Thuong hieu khong ton tai !");
    private int code;
    private String message;
}
