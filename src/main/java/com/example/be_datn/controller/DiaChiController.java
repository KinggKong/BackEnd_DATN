package com.example.be_datn.controller;


import com.example.be_datn.dto.Request.diaChi.DiaChiCreationRequest;
import com.example.be_datn.dto.Request.diaChi.DiaChiUpdateRequest;
import com.example.be_datn.dto.Response.ApiResponse;
import com.example.be_datn.service.impl.DiaChiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/diaChi")
@RequiredArgsConstructor
public class DiaChiController {
    private final DiaChiService diaChiService;

    @GetMapping
    public ResponseEntity<?> getAllDiaChi() {
        ApiResponse<?> response= ApiResponse.builder()
                .code(200)
                .data(diaChiService.getDiaChiList())
                .message("Get All Dia Chi success")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDiaChiById(@PathVariable Long id) {
        ApiResponse<?> response= ApiResponse.builder()
                .data(diaChiService.getDiaChiById(id))
                .message("Get Dia Chi Success")
                .code(200)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody DiaChiCreationRequest request){
        ApiResponse<?> response= ApiResponse.builder()
                .code(201)
                .data(diaChiService.createDiaChi(request))
                .message("Create Dia Chi Success")
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody DiaChiUpdateRequest request){
        ApiResponse<?> response= ApiResponse.builder()
                .code(202)
                .data(diaChiService.updateDiaChi(id, request))
                .message("Update Dia Chi Success")
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        diaChiService.deleteDiaChiById(id);
        ApiResponse<?> response= ApiResponse.builder()
                .code(204)
                .message("Delete Dia Chi Success")
                .build();
        return ResponseEntity.ok(response);
    }
}
