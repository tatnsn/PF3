package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String name;

    private LocalDateTime createdDate; // 作成日
    private LocalDateTime updatedDate; // 更新日
    private LocalDateTime deletedDate; // 削除日
    private Boolean isDeleted = false;  // 論理削除フラグ
}
