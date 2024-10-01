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
    THUONGHIEU_NOT_FOUND(1013,"Thuong hieu khong ton tai !"),
    SANPHAM_NOT_FOUND(1014,"Sản phẩm không tồn tại !"),
    TEN_SANPHAM_INVALID(1014,"Tên sản phẩm không hợp lệ phải nhiều hơn 8 kí tự !"),
    TEN_SANPHAM_EXIST(1014,"Tên sản phẩm không được trùng !"),
    DANHMUC_SANPHAM_INVALID(1014,"Danh mục không được để trống"),
    DANHMUC_NOT_FOUND(1014,"Danh mục không tồn tại !"),
    THUONGHIEU_SANPHAM_INVALID(1014,"Thương hiệu không được để trống"),
    DEGIAY_SANPHAM_INVALID(1014,"Đế giày không được để trống"),
    TRANGTHAI_SANPHAM_INVALID(1014,"Trạng thái không hợp lệ !"),
    LOAIVAI_SANPHAM_INVALID(1014,"Loại vải không được để trống"),
    CHATLIEUVAI_NOT_FOUND(1014,"Chất liệu vải không tồn tại !");

    private int code;
    private String message;
}
