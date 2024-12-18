package com.example.demo.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Account;
import com.example.demo.form.AdminAccountForm;
import com.example.demo.form.UserAccountForm;

public interface AccountService {
    Account getCurrentAdminUser(); // 現在の管理者ユーザーを取得
    Account getCurrentUser(); // 現在のユーザーを取得
    void updateAccount(Account account); // アカウントを更新
    Account createUserAccount(UserAccountForm form); // 一般ユーザーアカウントを作成
    Account createAdminAccount(AdminAccountForm form); // 管理者アカウントを作成
    List<Account> getAllAccounts(); // すべてのアカウントを取得
    
    String saveProfileImage(MultipartFile profileImage) throws IOException; // メソッドのシグネチャ
}
