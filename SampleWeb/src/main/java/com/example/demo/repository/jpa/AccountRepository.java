package com.example.demo.repository.jpa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmail(String email);

    List<Account> findByIsDeletedFalse();  // 引数なしで削除されていないアカウントを取得

    Page<Account> findByIsDeletedFalse(Pageable pageable);

    List<Account> findByIsDeletedTrue();

    // ROLE_USERのアカウントのみ取得するメソッド
    List<Account> findByIsDeletedFalseAndRole(String role);

 // // 月ごとのいいねランキングを取得するクエリ
    @Query("SELECT a FROM Account a WHERE a.role <> 'ROLE_ADMIN' AND a.createdDate BETWEEN :start AND :end ORDER BY a.likes DESC")
    List<Account> findTopLikedAccountsExcludingAdminsForMonth(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT a FROM Account a WHERE a.role <> 'ROLE_ADMIN' AND a.createdDate BETWEEN :start AND :end ORDER BY a.likes DESC")
    List<Account> findTopLikedAccountsExcludingAdminsForPeriod(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    
    @Query("SELECT a FROM Account a WHERE a.id = :userId AND a.isDeleted = false AND a.createdDate BETWEEN :startDate AND :endDate")
    List<Account> findLikesByUserAndPeriod(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}
