package com.example.be_datn.model.response;

import com.example.be_datn.entity.Fabric;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class FabricResponse {
    private int id;
    private String name;
    private int status;
    private Date createdAt;
    private Date updatedAt;

    public static FabricResponse from(Fabric chatLieuVai) {
        var response = new FabricResponse();
        response.setId(chatLieuVai.getId());
        response.setName(chatLieuVai.getName());
        response.setStatus(chatLieuVai.getStatus());
        response.setCreatedAt(chatLieuVai.getCreatedAt());
        response.setUpdatedAt(chatLieuVai.getUpdatedAt());
        return response;
    }
}