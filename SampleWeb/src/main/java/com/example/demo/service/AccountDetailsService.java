package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Account;
import com.example.demo.enums.Status;
import com.example.demo.repository.jpa.AccountRepository;

@Service
public class AccountDetailsService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Email not found: " + email));

        // アカウントが論理削除されている場合は例外をスロー
        if (account.isDeleted()) {
            throw new UsernameNotFoundException("The account is deactivated: " + email);
        }

        // ステータスが "ACCESS_DENIED" の場合は例外をスロー
        if (account.getStatus() == Status.ACCESS_DENIED) {
            throw new UsernameNotFoundException("Access Denied for user: " + email);
        }
        
        // role が null または空の場合を防ぐ
        String role = account.getRole();
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role must not be null or empty for user: " + email);
        }

        return new org.springframework.security.core.userdetails.User(
            account.getEmail(),
            account.getPassword(),
            List.of(new SimpleGrantedAuthority(account.getRole()))  // "ROLE_"のプレフィックスを省略
        );
    }
}
