/*package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.update.PasswordUpdateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PasswordUpdateController {

    private final PasswordUpdateService passwordUpdateService;

    // 一時的にエンドポイントを作成
    @GetMapping("/update-passwords")
    public String updatePasswords() {
        passwordUpdateService.encodeAndSavePasswords(); // パスワードをエンコード
        return "Passwords updated successfully!";
    }
}*/
