package com.example.demo.form;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Account;
import com.example.demo.enums.Gender; // Gender 列挙型をインポート

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProfileForm {
	private Long id; // IDフィールド
	private boolean isDeleted = false;
    private MultipartFile profileImage; // プロフィール画像

    @NotBlank(message = "名前は必須です")
    @Size(max = 255, message = "名前は255文字以内で入力してください")
    private String name;

    @NotBlank(message = "ふりがなは必須です")
    @Size(max = 255, message = "ふりがなは255文字以内で入力してください")
    @Pattern(regexp = "^[ぁ-ん]+$", message = "ふりがなはひらがなのみ使用できます")
    private String furigana;
    
    private Gender gender; // Gender 列挙型を使用
    
    @Min(value = 0, message = "年齢は0以上で入力してください")
    @Max(value = 999, message = "年齢は999以下で入力してください")
    private Integer age; // 年齢
    @Size(max = 1500, message = "自己紹介は1500文字以内で入力してください")
    private String introduction; // 自己紹介

    

 // コンストラクタ
    public ProfileForm() {}

    public ProfileForm(Account account) {
        this.id = account.getId(); // IDをセット
        this.name = account.getName();
        this.furigana = account.getFurigana();
        this.gender = account.getGender();
        this.age = account.getAge();
        this.introduction = account.getIntroduction();
        this.isDeleted = account.isDeleted();
        
    }
    
    public String validateProfileImageSize() {
        if (profileImage != null && profileImage.getSize() > 2 * 1024 * 1024) { // 2MB制限
            return "プロフィール画像は2MB以内である必要があります。";
        }
        return null; // 問題がなければnullを返す
    }
    
    
}
