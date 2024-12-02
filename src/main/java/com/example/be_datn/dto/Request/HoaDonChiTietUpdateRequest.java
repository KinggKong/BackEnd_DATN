package com.example.be_datn.dto.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HoaDonChiTietUpdateRequest implements Serializable {
    @JsonProperty("idSanPhamChiTiet")
    Long idSanPhamChiTiet;
    Long idHoaDon;
    String method;
    Integer soLuong;
}
