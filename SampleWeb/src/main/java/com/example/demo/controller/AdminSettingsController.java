package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.Account;
import com.example.demo.form.AdminAccountForm;
import com.example.demo.service.AccountService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminSettingsController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/settings")
    public String showSettings(Model model) {
        Account currentUser = accountService.getCurrentAdminUser(); // 現在の管理者ユーザーを取得
        AdminAccountForm adminAccountForm = new AdminAccountForm(currentUser); // 現在の情報でフォームを初期化
        model.addAttribute("adminAccountForm", adminAccountForm);
        return "admin/settings"; // admin/settings.htmlを返す
    }

    @PostMapping("/settings")
    public String updateSettings(@Valid @ModelAttribute("adminAccountForm") AdminAccountForm adminAccountForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/settings"; // エラーがある場合は再表示
        }

        // 更新処理
        Account accountToUpdate = accountService.getCurrentAdminUser(); // 現在のアカウントを取得
        accountToUpdate.setName(adminAccountForm.getAdminname());
        accountToUpdate.setEmail(adminAccountForm.getAdminemail());
        accountToUpdate.setPassword(adminAccountForm.getAdminpassword());

        // ログ出力
        System.out.println("Updating account: " + accountToUpdate);

        // ここでサービスを使用してデータを更新
        accountService.updateAccount(accountToUpdate);

        // セッション情報を更新
        UserDetails updatedUser = User.withUsername(accountToUpdate.getEmail())
                .password(accountToUpdate.getPassword())
                .roles(accountToUpdate.getRole().replace("ROLE_", "")) // "ROLE_" プレフィックスを除去
                .build();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(updatedUser, updatedUser.getPassword(), updatedUser.getAuthorities()));

        return "redirect:/admin/dashboard"; // 更新後のリダイレクト
    }
}
