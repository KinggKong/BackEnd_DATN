package com.example.be_datn.service.impl;

import com.example.be_datn.Exception.AppException;
import com.example.be_datn.Exception.ErrorCode;
import com.example.be_datn.entity.Fabric;
import com.example.be_datn.model.request.FabricRequest;
import com.example.be_datn.model.response.FabricResponse;
import com.example.be_datn.repository.FabricRepository;
import com.example.be_datn.service.FabricService;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class FabricServiceImpl implements FabricService {
    private final FabricRepository fabricRepository;
    @Override
    public Page<FabricResponse> gets(int page, int size) {
        var pageable = PageRequest.of(page, size);
        return fabricRepository.findAll(pageable).map(FabricResponse::from);
    }

    @Override
    public FabricResponse get(int id) {
        return fabricRepository.findById(id).map(FabricResponse::from).orElse(null);
    }

    @Override
    public void create(FabricRequest request) {
        var chatLieuVaiOpt = fabricRepository.findByName(request.getName());
        if (chatLieuVaiOpt.isPresent()) {
            throw new AppException(ErrorCode.CHAT_LIEU_VAI_ALREADY_EXISTS);
        }
        var chatLieuVai = new Fabric()
                .setName(request.getName())
                .setStatus(1)
                .setCreatedAt(new Date());
        try {
            fabricRepository.save(chatLieuVai);
        } catch (PersistenceException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new AppException(ErrorCode.CHAT_LIEU_VAI_ALREADY_EXISTS);
            }
        }
    }

    @Override
    public void update(FabricRequest request, int id) {
        var chatLieuVai = fabricRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CHAT_LIEU_VAI_NOT_FOUND));
        chatLieuVai.setName(request.getName())
                .setStatus(request.getStatus())
                .setUpdatedAt(new Date());
        try {
            fabricRepository.save(chatLieuVai);
        } catch (Exception e) {
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        var chatLieuVai = fabricRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CHAT_LIEU_VAI_NOT_FOUND));
        chatLieuVai.setStatus(0);
        try {
            fabricRepository.save(chatLieuVai);
        } catch (Exception e) {
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}