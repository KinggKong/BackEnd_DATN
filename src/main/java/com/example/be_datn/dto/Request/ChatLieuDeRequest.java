package com.example.be_datn.dto.Request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatLieuDeRequest {
    @NotNull(message = "TEN_CHATLIEUDE_INVALID")
    @Size(min = 2, message = "TEN_CHATLIEUDE_INVALID")
    String tenChatLieu;
    @Min(value = 0,message = "TRANGTHAI_CHATLIEUDE_INVALID")
    @Max(value = 1,message = "TRANGTHAI_CHATLIEUDE_INVALID")
    int trangThai;
}
