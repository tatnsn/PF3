package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

// LoginController.java
@Controller
public class LoginController {

	@GetMapping("/login")
    public String showLoginForm(Model model, @RequestParam(value = "error", required = false) String error) {
        if (error != null) {
            model.addAttribute("error", "ログインに失敗しました。メールアドレスまたはパスワードが無効、またはアクセスが拒否されています。");
        }
        return "login";
    }
}
