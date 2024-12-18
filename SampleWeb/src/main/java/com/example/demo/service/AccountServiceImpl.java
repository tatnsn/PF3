package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Account;
import com.example.demo.form.AdminAccountForm;
import com.example.demo.form.UserAccountForm;
import com.example.demo.repository.jpa.AccountRepository;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Account getCurrentAdminUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername(); // ユーザー名（メールアドレス）を取得
        
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin user not found")); 
    }
    
    @Override
    public Account getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();  // メールアドレスを取得
        
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    
    @Override
    @Transactional
    public void updateAccount(Account account) {
        accountRepository.save(account);
    }

    @Override
    @Transactional
    public Account createUserAccount(UserAccountForm form) {
        Account account = new Account();
        account.setEmail(form.getEmail());
        account.setPassword(form.getPassword());
        account.setRole("ROLE_USER");
        account.setAdminstatus(false);

        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public Account createAdminAccount(AdminAccountForm form) {
        Account account = new Account();
        account.setName(form.getAdminname());
        account.setEmail(form.getAdminemail());
        account.setPassword(form.getAdminpassword());
        account.setRole("ROLE_ADMIN");
        account.setAdminstatus(true);

        return accountRepository.save(account);
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    @Transactional
    public String saveProfileImage(MultipartFile profileImage) throws IOException {
        if (profileImage == null || profileImage.isEmpty()) {
            throw new IllegalArgumentException("プロフィール画像が空です。");
        }

        // ファイルサイズのバリデーション
        if (profileImage.getSize() > 2 * 1024 * 1024) {
            throw new IllegalArgumentException("プロフィール画像は2MB以内である必要があります。");
        }

        // ファイル名を生成
        String originalFilename = profileImage.getOriginalFilename();
        if (originalFilename != null) {
            originalFilename = originalFilename.replaceAll("[^a-zA-Z0-9.\\-_]", "_"); // 不正文字をサニタイズ
        }
        String filename = UUID.randomUUID().toString() + "_" + originalFilename;

        // 保存先ディレクトリ
        String uploadDir = "/home/yoshimura/project/PF3/SampleWeb/path/to/save/images";
        File dir = new File(uploadDir);

        // ディレクトリが存在しない場合は作成
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // ファイルを保存
        File destinationFile = new File(uploadDir, filename);
        try {
            profileImage.transferTo(destinationFile);
        } catch (IOException e) {
            throw new IOException("画像保存中にエラーが発生しました: " + e.getMessage(), e);
        }

        // データベースには相対パスを保存
        return "/path/to/save/images/" + filename;
    }

}
