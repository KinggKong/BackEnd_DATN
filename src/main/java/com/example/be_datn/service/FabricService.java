package com.example.be_datn.service;

import com.example.be_datn.model.request.FabricRequest;
import com.example.be_datn.model.response.FabricResponse;
import org.springframework.data.domain.Page;

public interface FabricService {
    Page<FabricResponse> gets(int page, int size);
    FabricResponse get(int id);
    void create(FabricRequest request);
    void update(FabricRequest request, int id);
    void delete(int id);
}