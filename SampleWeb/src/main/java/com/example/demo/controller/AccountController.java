package com.example.demo.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Account;
import com.example.demo.enums.Status;
import com.example.demo.form.AdminAccountForm;
import com.example.demo.form.UserAccountForm;
import com.example.demo.repository.jpa.AccountRepository;
import com.example.demo.service.AccountService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/accounts")
public class AccountController {

	@Autowired 
	private AccountRepository accountRepository;
	
	@Autowired
    private AccountService accountService; // 依存性注入


	@GetMapping("/add")
	@PreAuthorize("hasRole('ROLE_ADMIN')")  // ROLE_ADMINのみアクセス可能
	public String showAddAccountForm(Model model) {
	    model.addAttribute("userAccountForm", new UserAccountForm());
	    model.addAttribute("adminAccountForm", new AdminAccountForm());
	    return "account/add";
	}

	@PostMapping("/add")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String addAccount(
	        @RequestParam("role") String role,
	        @Valid @ModelAttribute("userAccountForm") UserAccountForm userAccountForm,
	        BindingResult userResult,
	        @Valid @ModelAttribute("adminAccountForm") AdminAccountForm adminAccountForm,
	        BindingResult adminResult,
	        Model model) {

	    if ("ROLE_USER".equals(role)) {
	        // 一般ユーザーの登録処理
	        if (userResult.hasErrors()) {
	            model.addAttribute("userAccountForm", userAccountForm);
	            model.addAttribute("adminAccountForm", new AdminAccountForm());
	            model.addAttribute("errorMessage", "入力内容にエラーがあります。");
	            return "account/add";
	        }

	        // プロフィール画像の保存処理
	        String profileImageFileName = null;
	        try {
	            if (userAccountForm.getProfileImage() != null && !userAccountForm.getProfileImage().isEmpty()) {
	                // サービスを利用して画像を保存
	                profileImageFileName = accountService.saveProfileImage(userAccountForm.getProfileImage());
	            }
	        } catch (IOException e) {
	            userResult.rejectValue("profileImage", "error.profileImage", "画像の保存中にエラーが発生しました");
	            model.addAttribute("userAccountForm", userAccountForm);
	            model.addAttribute("adminAccountForm", new AdminAccountForm());
	            model.addAttribute("errorMessage", "画像の保存中にエラーが発生しました: " + e.getMessage());
	            e.printStackTrace(); // スタックトレースをログに出力
	            return "account/add";
	        } catch (IllegalArgumentException e) {
	            userResult.rejectValue("profileImage", "error.profileImage", e.getMessage());
	            model.addAttribute("userAccountForm", userAccountForm);
	            model.addAttribute("adminAccountForm", new AdminAccountForm());
	            model.addAttribute("errorMessage", e.getMessage());
	            return "account/add";
	        }

	        // Account エンティティのインスタンスを作成し、一般ユーザー情報を設定
	        Account userAccount = new Account();
	        userAccount.setName(userAccountForm.getName());
	        userAccount.setEmail(userAccountForm.getEmail());
	        userAccount.setPassword(userAccountForm.getPassword());
	        userAccount.setProfileImage(profileImageFileName); // ファイル名を設定
	        userAccount.setFurigana(userAccountForm.getFurigana());
	        userAccount.setGender(userAccountForm.getGender());
	        userAccount.setAge(userAccountForm.getAge());
	        userAccount.setIntroduction(userAccountForm.getIntroduction());
	        userAccount.setStatus(userAccountForm.getStatus());
	        userAccount.setRole("ROLE_USER"); // ロールを "ROLE_USER" に設定
	        userAccount.setAdminstatus(false); // 一般ユーザーのため `adminstatus` を `false` に設定

	        accountRepository.save(userAccount);

	    } else if ("ROLE_ADMIN".equals(role)) {
	        // 管理者の登録処理
	        if (adminResult.hasErrors()) {
	            model.addAttribute("userAccountForm", new UserAccountForm());
	            model.addAttribute("adminAccountForm", adminAccountForm);
	            model.addAttribute("errorMessage", "管理者情報にエラーがあります。");
	            return "account/add";
	        }

	        // Account エンティティのインスタンスを作成し、管理者情報を設定
	        Account adminAccount = new Account();
	        adminAccount.setName(adminAccountForm.getAdminname());
	        adminAccount.setEmail(adminAccountForm.getAdminemail());
	        adminAccount.setPassword(adminAccountForm.getAdminpassword());
	        adminAccount.setStatus(adminAccountForm.getStatus());
	        adminAccount.setRole("ROLE_ADMIN"); // ロールを "ROLE_ADMIN" に設定
	        adminAccount.setAdminstatus(true); // 管理者のため `adminstatus` を `true` に設定

	        accountRepository.save(adminAccount);
	    } else {
	        // ロールが無効な場合
	        model.addAttribute("errorMessage", "無効なロールです。");
	        return "account/add";
	    }

	    return "redirect:/accounts";
	}





    @GetMapping
    public String listAccounts(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<Account> accountPage = accountRepository.findByIsDeletedFalse(PageRequest.of(page, 5));
        model.addAttribute("accounts", accountPage.getContent());
        model.addAttribute("totalPages", accountPage.getTotalPages());
        model.addAttribute("currentPage", page);
        return "account/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditAccountForm(@PathVariable Long id, Model model) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));

        if ("ROLE_USER".equals(account.getRole())) {
            UserAccountForm userAccountForm = new UserAccountForm(account);
            model.addAttribute("userAccountForm", userAccountForm);
            model.addAttribute("adminAccountForm", new AdminAccountForm());
        } else if ("ROLE_ADMIN".equals(account.getRole())) {
            AdminAccountForm adminAccountForm = new AdminAccountForm(account);
            model.addAttribute("adminAccountForm", adminAccountForm);
            model.addAttribute("userAccountForm", new UserAccountForm());
        }

        model.addAttribute("account", account);
        return "account/edit";
    }


    @PostMapping("/edit/{id}")
    public String editAccount(
            @PathVariable Long id,
            @RequestParam("role") String role,
            @Valid @ModelAttribute("userAccountForm") UserAccountForm userAccountForm,
            BindingResult userResult,
            @Valid @ModelAttribute("adminAccountForm") AdminAccountForm adminAccountForm,
            BindingResult adminResult,
            Model model) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // 一般ユーザーの更新処理
        if ("ROLE_USER".equals(role)) {
            if (userResult.hasErrors()) {
                model.addAttribute("userAccountForm", userAccountForm);
                model.addAttribute("adminAccountForm", new AdminAccountForm());
                model.addAttribute("account", account);
                return "account/edit";
            }

            String profileImageFileName = account.getProfileImage();
            try {
                if (userAccountForm.getProfileImage() != null && !userAccountForm.getProfileImage().isEmpty()) {
                    profileImageFileName = accountService.saveProfileImage(userAccountForm.getProfileImage());
                }
            } catch (IllegalArgumentException | IOException e) {
                userResult.rejectValue("profileImage", "error.profileImage", "画像の保存中にエラーが発生しました: " + e.getMessage());
                model.addAttribute("userAccountForm", userAccountForm);
                model.addAttribute("adminAccountForm", new AdminAccountForm());
                model.addAttribute("account", account);
                model.addAttribute("errorMessage", e.getMessage());
                return "account/edit";
            }

            account.setName(userAccountForm.getName());
            account.setEmail(userAccountForm.getEmail());
            account.setFurigana(userAccountForm.getFurigana());
            account.setGender(userAccountForm.getGender());
            account.setAge(userAccountForm.getAge());
            account.setIntroduction(userAccountForm.getIntroduction());
            account.setStatus(userAccountForm.getStatus());
            account.setProfileImage(profileImageFileName);
        }

        // 管理者の更新処理
        else if ("ROLE_ADMIN".equals(role)) {
            if (adminResult.hasErrors()) {
                model.addAttribute("userAccountForm", new UserAccountForm());
                model.addAttribute("adminAccountForm", adminAccountForm);
                model.addAttribute("account", account);
                return "account/edit";
            }

            account.setName(adminAccountForm.getAdminname());
            account.setEmail(adminAccountForm.getAdminemail());
            account.setPassword(adminAccountForm.getAdminpassword());
            account.setStatus(adminAccountForm.getStatus());
        }

        // ロールが無効な場合
        else {
            model.addAttribute("errorMessage", "無効なロールです。");
            return "account/edit";
        }

        // アカウント情報を保存
        accountRepository.save(account);

        // 成功時リダイレクト
        return "redirect:/accounts";
    }






    @GetMapping("/deleted")
    public String listDeletedAccounts(Model model) {
        List<Account> deletedAccounts = accountRepository.findByIsDeletedTrue();
        model.addAttribute("deletedAccounts", deletedAccounts);
        return "account/deleted";
    }

    @PostMapping("/delete/{id}")
    public String deleteAccount(@PathVariable Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
        account.setDeleted(true);
        account.setDeletedDate(LocalDateTime.now()); // 現在の日時を削除日時に設定
        accountRepository.save(account);
        return "redirect:/accounts";
    }

    @PostMapping("/deletePermanent/{id}")
    public String deletePermanentAccount(@PathVariable Long id) {
        accountRepository.deleteById(id);
        return "redirect:/accounts/deleted";
    }

    @PostMapping("/restore/{id}")
    public String restoreAccount(@PathVariable Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
        account.setDeleted(false);
        accountRepository.save(account);
        return "redirect:/accounts/deleted";
    }

    @PostMapping("/toggleStatus/{id}")
    public String toggleStatus(@PathVariable Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
        account.setStatus(account.getStatus() == Status.ACCESS_GRANTED ? Status.ACCESS_DENIED : Status.ACCESS_GRANTED);
        accountRepository.save(account);
        return "redirect:/accounts";
    }

   

}
    
