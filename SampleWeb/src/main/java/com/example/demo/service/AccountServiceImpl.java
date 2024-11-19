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
            return null;
        }

        // ファイル名を生成
        String fileName = UUID.randomUUID().toString() + "_" + profileImage.getOriginalFilename();
        // 保存先ディレクトリ
        String uploadDir = "C:/ForDevelop/workspace/PF3/SampleWeb/src/main/resources/static/images";
        File dir = new File(uploadDir);

        // ディレクトリが存在しない場合は作成
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // ファイルを保存
        File destinationFile = new File(uploadDir, fileName);
        profileImage.transferTo(destinationFile);

        // ファイル名のみを返す
        return fileName;
    }
}
