package com.example.be_datn.dto.Request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatLieuVaiRequest {
    @NotNull(message = "TEN_CHATLIEUVAI_INVALID")
    @Size(min = 2, message = "TEN_CHATLIEUVAI_INVALID")
    String tenChatLieuVai;
    @Min(value = 0,message = "TRANGTHAI_CHATLIEUVAI_INVALID")
    @Max(value = 1,message = "TRANGTHAI_CHATLIEUVAI_INVALID")
    int trangThai;
}
