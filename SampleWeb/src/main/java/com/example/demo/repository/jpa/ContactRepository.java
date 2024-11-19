package com.example.demo.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByIsDeletedFalse(); // 論理削除されていないお問い合わせを取得
}
