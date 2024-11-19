package com.example.demo.form;

import com.example.demo.entity.Account;
import com.example.demo.enums.Status;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminAccountForm {
    private Long id; // IDフィールド

    @NotBlank(message = "名前を入力してください")
    @Size(max = 255, message = "名前は255文字以内で入力してください")
    private String adminname;

    @NotBlank(message = "メールアドレスを入力してください")
    @Email(message = "正しいメールアドレスの形式で入力してください")
    @Size(max = 255, message = "メールアドレスは255文字以内で入力してください")
    @Pattern(regexp = ".*\\..*", message = "正しいメールアドレスの形式で入力してください")
    private String adminemail;

    @NotBlank(message = "パスワードを入力してください")
    @Size(min = 8, max = 32, message = "パスワードは8〜32文字の間で入力してください")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "パスワードは半角英数字と_-のみ使用可能です")
    private String adminpassword;

    private Status status;
    
    private boolean adminstatus;
    
    private boolean isDeleted = false; // 削除フラグ

    // コンストラクタ
    public AdminAccountForm() {}

    public AdminAccountForm(Account account) {
        this.id = account.getId(); // IDをセット
        this.adminname = account.getName(); // 名前をセット
        this.adminemail = account.getEmail(); // メールアドレスをセット
        this.adminpassword = account.getPassword(); // パスワードをセット
	    this.status = account.getStatus();  
        this.isDeleted = account.isDeleted(); // 削除フラグをセット
        this.adminstatus = account.isAdminstatus();
    }
}
