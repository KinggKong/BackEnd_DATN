package com.example.be_datn.exception;

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
    TEN_MAUSAC_INVALID(1004,"Tên màu sắc cần nhiều hơn 2 ký tự !"),
    TRANGTHAI_MAUSAC_INVALID(1004,"Trạng thái không hợp lệ !"),
    CHATLIEUDE_NOT_FOUND(1005,"Chất liệu đế không tồn tại !"),
    CHATLIEUDE_ALREADY_EXISTS(1006,"Chất liệu đế đã tồn tại !"),
    TEN_CHATLIEUDE_INVALID(1007,"Tên chất liệu đế cần nhiều hơn 3 ký tự !"),
    KICHTHUOC_ALREADY_EXISTS(1008, "KichThuoc đã tồn tại!"),
    KICHTHUOC_NOT_FOUND(1009, "KichThuoc không tồn tại!"),
    HANG_ALREADY_EXISTS(1010,"DanhMuc da ton tai!"),
    HANG_NOT_FOUND(1011,"DanhMuc khong ton tai"),
    THUONGHIEU_ALREADY_EXISTS(1012,"Thuong hieu da ton tai !"),
    THUONGHIEU_NOT_FOUND(1013,"Thuong hieu khong ton tai !");

    private int code;
    private String message;
}
