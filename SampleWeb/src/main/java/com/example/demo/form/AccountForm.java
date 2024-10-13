package com.example.demo.form;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Account;
import com.example.demo.enums.Gender;
import com.example.demo.enums.Status;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class AccountForm {

    @NotBlank(message = "名前を入力してください")
    @Size(max = 255, message = "名前は255文字以内で入力してください")
    private String name;

    @NotBlank(message = "メールアドレスを入力してください")
    @Email(message = "正しいメールアドレスの形式で入力してください")
    @Size(max = 255, message = "メールアドレスは255文字以内で入力してください")
    private String email;

    @NotBlank(message = "パスワードを入力してください")
    @Size(min = 8, max = 32, message = "パスワードは8〜32文字の間で入力してください")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "パスワードは半角英数字と_-のみ使用可能です")
    private String password;

    @NotNull(message = "ステータスを選択してください")
    private Status status;

    // 一般ユーザー専用フィールド（MultipartFile型に変更）
    private MultipartFile profileImage;

    @Size(max = 255, message = "ふりがなは255文字以内で入力してください")
    @Pattern(regexp = "^[ぁ-ん]+$", message = "ふりがなはひらがなのみ使用できます")
    private String furigana;

    private Gender gender;

    @Min(value = 0, message = "年齢は0以上で入力してください")
    @Max(value = 999, message = "年齢は999以下で入力してください")
    private Integer age;

    @Size(max = 1500, message = "自己紹介は1500文字以内で入力してください")
    private String introduction;

    private String role;

    // コンストラクタ
    public AccountForm() {}

    public AccountForm(Account account) {
        this.name = account.getName();
        this.email = account.getEmail();
        this.password = account.getPassword();
        this.status = account.getStatus();
        this.profileImage = null; // アップロード処理でセットするためここではnull
        this.furigana = account.getFurigana();
        this.gender = account.getGender();
        this.age = account.getAge();
        this.introduction = account.getIntroduction();
    }

    // GetterとSetter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public MultipartFile getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(MultipartFile profileImage) {
        this.profileImage = profileImage;
    }

    public String getFurigana() {
        return furigana;
    }

    public void setFurigana(String furigana) {
        this.furigana = furigana;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
