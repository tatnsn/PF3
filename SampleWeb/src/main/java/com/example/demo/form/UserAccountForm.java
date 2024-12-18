
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
public class UserAccountForm {
    private Long id; // IDフィールド
    private boolean isDeleted = false; // 削除フラグ


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


    @Size(max = 255, message = "ふりがなは255文字以内で入力してください")
    @Pattern(regexp = "^[ぁ-ん]+$", message = "ふりがなはひらがなのみ使用できます")
    private String furigana;
    
    private MultipartFile profileImage;
    private Gender gender; // Gender 列挙型を使用
    
    @Min(value = 0, message = "年齢は0以上で入力してください")
    @Max(value = 999, message = "年齢は999以下で入力してください")
    private Integer age; // 年齢
    
    @Size(max = 1500, message = "自己紹介は1500文字以内で入力してください")
    private String introduction; // 自己紹介
    
    private Status status;
  

    // コンストラクタ
    public UserAccountForm() {}

    public UserAccountForm(Account account) {
    	this.id = account.getId(); // IDをセット
        this.name = account.getName();
        this.email = account.getEmail();
        this.password = account.getPassword();
        this.status = account.getStatus();
        this.isDeleted = account.isDeleted(); // 削除フラグをセット
        this.furigana = account.getFurigana();
        this.gender = account.getGender();
        this.age = account.getAge();
        this.introduction = account.getIntroduction();
        
        
    }
    
    public String validateProfileImageSize() {
        if (profileImage != null && profileImage.getSize() > 2 * 1024 * 1024) { // 2MB制限
            return "プロフィール画像は2MB以内である必要があります。";
        }
        return null; // 問題がなければnullを返す
    }
}
