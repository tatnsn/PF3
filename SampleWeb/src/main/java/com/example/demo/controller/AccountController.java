package com.example.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Account;
import com.example.demo.enums.Status;
import com.example.demo.form.AccountForm;
import com.example.demo.repository.AccountRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    private final String uploadDir = "path/to/save/images/"; // アップロードするディレクトリ

    // アカウント一覧表示 (5件毎のページネーション)
    @GetMapping
    public String listAccounts(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<Account> accountPage = accountRepository.findByIsDeletedFalse(PageRequest.of(page, 5));
        model.addAttribute("accounts", accountPage.getContent());
        model.addAttribute("totalPages", accountPage.getTotalPages());
        model.addAttribute("currentPage", page);
        return "account/list";
    }

    // アカウント追加画面表示
    @GetMapping("/add")
    public String showAddAccountForm(Model model) {
        model.addAttribute("accountForm", new AccountForm());
        return "account/add";
    }

    // アカウント追加処理
    @PostMapping("/add")
    public String addAccount(@Valid @ModelAttribute("accountForm") AccountForm accountForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "account/add";
        }

        Account account = new Account();
        mapFormToAccount(accountForm, account);

        // プロフィール画像の保存処理
        MultipartFile profileImage = accountForm.getProfileImage();
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                // ディレクトリが存在しない場合は作成
                if (!Files.exists(Paths.get(uploadDir))) {
                    Files.createDirectories(Paths.get(uploadDir));
                }

                // 一意のファイル名を生成
                String originalFileName = profileImage.getOriginalFilename();
                String fileName = UUID.randomUUID().toString() + "_" + originalFileName;
                Path filePath = Paths.get(uploadDir + fileName);
                Files.copy(profileImage.getInputStream(), filePath);

                // 保存したファイルのファイル名をアカウントに設定
                account.setProfileImage(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("uploadError", "プロフィール画像のアップロードに失敗しました。");
                return "account/add";
            }
        }

        accountRepository.save(account);
        return "redirect:/accounts";
    }

    // アカウント編集画面表示
    @GetMapping("/edit/{id}")
    public String showEditAccountForm(@PathVariable Long id, Model model) {
        Account account = accountRepository.findById(id).orElseThrow();
        AccountForm accountForm = new AccountForm(account);
        model.addAttribute("accountForm", accountForm);
        return "account/edit";
    }

    // アカウント編集処理
    @PostMapping("/edit/{id}")
    public String editAccount(@PathVariable Long id, @Valid @ModelAttribute("accountForm") AccountForm accountForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "account/edit";
        }

        Account account = accountRepository.findById(id).orElseThrow();
        mapFormToAccount(accountForm, account);

        // プロフィール画像の更新処理
        MultipartFile profileImage = accountForm.getProfileImage();
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                // ディレクトリが存在しない場合は作成
                if (!Files.exists(Paths.get(uploadDir))) {
                    Files.createDirectories(Paths.get(uploadDir));
                }

                // 一意のファイル名を生成
                String originalFileName = profileImage.getOriginalFilename();
                String fileName = UUID.randomUUID().toString() + "_" + originalFileName;
                Path filePath = Paths.get(uploadDir + fileName);
                Files.copy(profileImage.getInputStream(), filePath);

                // 保存したファイルのファイル名をアカウントに設定
                account.setProfileImage(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("uploadError", "プロフィール画像のアップロードに失敗しました。");
                return "account/edit";
            }
        }

        accountRepository.save(account);
        return "redirect:/accounts";
    }

    // アカウント削除 (論理削除)
    @PostMapping("/delete/{id}")
    public String deleteAccount(@PathVariable Long id) {
        Account account = accountRepository.findById(id).orElseThrow();
        account.setDeleted(true);
        accountRepository.save(account);
        return "redirect:/accounts";
    }

    // ステータス切り替え (アクセス許可⇔アクセス禁止)
    @PostMapping("/toggleStatus/{id}")
    public String toggleStatus(@PathVariable Long id) {
        Account account = accountRepository.findById(id).orElseThrow();
        account.setStatus(account.getStatus() == Status.ACCESS_GRANTED ? Status.ACCESS_DENIED : Status.ACCESS_GRANTED);
        accountRepository.save(account);
        return "redirect:/accounts";
    }

    // 削除されたアカウント一覧表示
    @GetMapping("/deleted")
    public String listDeletedAccounts(Model model) {
        List<Account> deletedAccounts = accountRepository.findByIsDeletedTrue();
        model.addAttribute("deletedAccounts", deletedAccounts);
        return "account/deleted";
    }

    // アカウント復元
    @PostMapping("/restore/{id}")
    public String restoreAccount(@PathVariable Long id) {
        Account account = accountRepository.findById(id).orElseThrow();
        account.setDeleted(false);
        accountRepository.save(account);
        return "redirect:/account/deleted";
    }

    // 完全削除
    @PostMapping("/deletePermanent/{id}")
    public String deletePermanentAccount(@PathVariable Long id) {
        accountRepository.deleteById(id);
        return "redirect:/account/deleted";
    }

    // フォームからエンティティにマッピングする共通メソッド
    private void mapFormToAccount(AccountForm form, Account account) {
        account.setName(form.getName());
        account.setEmail(form.getEmail());
        account.setPassword(form.getPassword());
        account.setStatus(form.getStatus());
        account.setFurigana(form.getFurigana());
        account.setGender(form.getGender());
        account.setAge(form.getAge());
        account.setIntroduction(form.getIntroduction());

        // プロフィール画像のフィールドを設定
        if (form.getProfileImage() != null && !form.getProfileImage().isEmpty()) {
            account.setProfileImage(form.getProfileImage().getOriginalFilename());
        }
    }
}
