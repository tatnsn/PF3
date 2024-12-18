package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Account;
import com.example.demo.repository.jpa.AccountRepository;

@Service
public class LikeService {

    @Autowired
    private AccountRepository accountRepository;

    // アカウントのいいね数を1増加させる
    public int likeAccount(Long accountId) {
        Account account = accountRepository.findById(accountId).orElse(null);
        if (account != null) {
            int currentLikes = account.getLikes();
            account.setLikes(currentLikes + 1);
            accountRepository.save(account);
            System.out.println("Updated likes for account ID: " + accountId + " to " + (currentLikes + 1));
            return account.getLikes(); // 更新後のいいね数を返す
        } else {
            throw new RuntimeException("Account not found with ID: " + accountId);
        }
    }

 // 管理者を除外した月ごとのいいねランキング
    public List<Account> findTopLikedAccountsForMonth(LocalDateTime startOfMonth, LocalDateTime endOfMonth) {
        return accountRepository.findTopLikedAccountsExcludingAdminsForMonth(startOfMonth, endOfMonth);
    }

    // 管理者を除外した年間のいいねランキング
    public List<Account> findTopLikedAccountsForPeriod(LocalDateTime startOfYear, LocalDateTime endOfYear) {
        return accountRepository.findTopLikedAccountsExcludingAdminsForPeriod(startOfYear, endOfYear);
    }
    public int getLikesForYear(Long userId, int year) {
        LocalDate startOfYear = LocalDate.of(year, 1, 1);
        LocalDateTime startOfYearDateTime = startOfYear.atStartOfDay();
        LocalDateTime endOfYearDateTime = startOfYear.withMonth(12).withDayOfMonth(31).atTime(23, 59, 59);

        List<Account> accounts = accountRepository.findLikesByUserAndPeriod(userId, startOfYearDateTime, endOfYearDateTime);
        return accounts.stream().mapToInt(Account::getLikes).sum();
    }

    public int getLikesForMonth(Long userId, int year, int month) {
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDateTime startOfMonthDateTime = startOfMonth.atStartOfDay();
        LocalDateTime endOfMonthDateTime = startOfMonth.with(TemporalAdjusters.lastDayOfMonth()).atTime(23, 59, 59);

        List<Account> accounts = accountRepository.findLikesByUserAndPeriod(userId, startOfMonthDateTime, endOfMonthDateTime);
        return accounts.stream().mapToInt(Account::getLikes).sum();
    }
}


