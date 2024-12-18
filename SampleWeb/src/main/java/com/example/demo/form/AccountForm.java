package com.example.demo.form;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Account;
import com.example.demo.enums.Gender;
import com.example.demo.enums.Status;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AccountForm {
    private Long id; // IDフィールド
    private boolean adminflag; // 管理者フラグ

    @NotBlank(message = "名前を入力してください")
    @Size(max = 255, message = "名前は255文字以内で入力してください")
    private String name;

    @NotBlank(message = "メールアドレスを入力してください")
    @Email(message = "正しいメールアドレスの形式で入力してください")
    @Size(max = 255, message = "メールアドレスは255文字以内で入力してください")
    @Pattern(regexp = ".*\\..*", message = "正しいメールアドレスの形式で入力してください")
    private String email;

    @NotBlank(message = "パスワードを入力してください")
    @Size(min = 8, max = 32, message = "パスワードは8〜32文字の間で入力してください")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "パスワードは半角英数字と_-のみ使用可能です")
    private String password;

    private Status status; // ステータス

    private MultipartFile profileImage; // プロフィール画像

    @Size(max = 255, message = "ふりがなは255文字以内で入力してください")
    @Pattern(regexp = "^[ぁ-ん]+$", message = "ふりがなはひらがなのみ使用できます")
    private String furigana;

    private Gender gender;

    @Min(value = 0, message = "年齢は0以上で入力してください")
    @Max(value = 999, message = "年齢は999以下で入力してください")
    private Integer age;

    @Size(max = 1500, message = "自己紹介は1500文字以内で入力してください")
    private String introduction;

    @NotBlank(message = "役割を入力してください") // 役割のバリデーション
    private String role;

    // 管理者専用フィールド
    @NotBlank(message = "名前を入力してください")
    private String adminname;

    @NotBlank(message = "メールアドレスを入力してください")
    @Email(message = "正しいメールアドレスの形式で入力してください")
    private String adminemail;

    @NotBlank(message = "パスワードを入力してください")
    private String adminpassword;

    @NotBlank(message = "管理者ステータスを入力してください") // adminstatusのバリデーション
    private String adminstatus;  // 追加

    // コンストラクタ
    public AccountForm() {}

    public AccountForm(Account account) {
        this.id = account.getId(); // IDをセット
        this.name = account.getName();
        this.email = account.getEmail();
        this.password = account.getPassword();
        this.status = account.getStatus();
        this.furigana = account.getFurigana();
        this.gender = account.getGender();
        this.age = account.getAge();
        this.introduction = account.getIntroduction();
        this.role = account.getRole(); // 役割をセット
     // 管理者情報も必要であれば設定
        if (account.isAdminstatus()) {
            this.adminname = account.getName(); // 役割に応じたフィールドを追加
            this.adminemail = account.getEmail();
            this.adminpassword = account.getPassword(); // 初期化したい場合
            this.adminstatus = account.isAdminstatus() ? "true" : "false"; // boolean を String に変換
        }
    }
}
