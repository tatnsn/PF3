package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "contacts")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // Categoryとの多対一リレーション
    @JoinColumn(name = "category_id", referencedColumnName = "id") // 外部キー
    private Category category; // カテゴリをCategory型に変更

    private String body;
    private String status; // ステータスを日本語で保持

    private LocalDateTime created_date; // 作成日
    private LocalDateTime updated_date; // 更新日
    private LocalDateTime deleted_date; // 削除日
    private Boolean isDeleted = false;  // 論理削除フラグ
}
