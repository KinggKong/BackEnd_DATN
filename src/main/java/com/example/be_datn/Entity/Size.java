package com.example.be_datn.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "size")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Size extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @jakarta.validation.constraints.Size(min = 2, message = "TEN_SIZE_INVALID")
    @Column(name = "ten_size")
    private String tenSize;

    @Column(name = "trang_thai")
    Integer trangThai;


}


