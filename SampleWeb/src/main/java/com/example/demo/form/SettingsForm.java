package com.example.demo.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SettingsForm {

    @NotBlank(message = "メールアドレスは必須です")
    @Email(message = "メールアドレスの形式が正しくありません")
    @Size(max = 255, message = "メールアドレスは255文字以内で入力してください")
    @Pattern(regexp = ".*\\..*", message = "メールアドレスの形式が正しくありません")
    private String email;

    @NotBlank(message = "パスワードは必須です")
    @Size(min = 8, max = 32, message = "パスワードは8〜32文字で入力してください")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "パスワードは半角英数字と_-のみ使用可能です")
    private String password;

    // ゲッターとセッターを追加
    public String getEmail() {
        return email;
    }

    // email の setter
    public void setEmail(String email) {
        this.email = email;
    }

    // password の getter
    public String getPassword() {
        return password;
    }

    // password の setter
    public void setPassword(String password) {
        this.password = password;
    }
}
