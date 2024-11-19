package com.example.demo.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByIsDeletedFalse(); // 論理削除されていないカテゴリを取得
}
