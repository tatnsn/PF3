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

import com.example.demo.entity.Account; // Accountエンティティをインポート
import com.example.demo.form.UserAccountForm;
import com.example.demo.service.AccountService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserSettingsController {

    @Autowired
    private AccountService accountService; // アカウントサービスのインスタンス

    @GetMapping("/settings")
    public String showUserSettings(Model model) {
        // 現在のユーザー情報を取得
        Account currentUser = accountService.getCurrentAdminUser(); // 現在のユーザーを取得
        UserAccountForm userAccountForm = new UserAccountForm(currentUser); // フォームを初期化
        model.addAttribute("userAccountForm", userAccountForm); // フォームをモデルに追加
        return "user/settings"; // user/settings.html
    }

    @PostMapping("/settings")
    public String updateUserSettings(@Valid @ModelAttribute("userAccountForm") UserAccountForm userAccountForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/settings"; // エラーがある場合は再表示
        }

        // ユーザー情報の更新処理
        Account accountToUpdate = accountService.getCurrentUser(); // 修正
        accountToUpdate.setEmail(userAccountForm.getEmail());
        accountToUpdate.setPassword(userAccountForm.getPassword());
       

        if (!userAccountForm.getPassword().isEmpty()) { // パスワードが入力されていれば更新
            accountToUpdate.setPassword(userAccountForm.getPassword());
        }

        accountService.updateAccount(accountToUpdate); // データベースを更新

        // セッション情報を更新
        UserDetails updatedUser = User.withUsername(accountToUpdate.getEmail())
                .password(accountToUpdate.getPassword())
                .roles(accountToUpdate.getRole().replace("ROLE_", "")) // "ROLE_" プレフィックスを除去
                .build();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(updatedUser, updatedUser.getPassword(), updatedUser.getAuthorities()));

        return "redirect:/user/dashboard"; // ダッシュボードへリダイレクト
    }
}
