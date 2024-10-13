package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    // 未削除のアカウントを取得する
    Page<Account> findByIsDeletedFalse(Pageable pageable);

    // 削除されたアカウントを取得する
    List<Account> findByIsDeletedTrue();
}
