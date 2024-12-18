package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.entity.Account;
import com.example.demo.enums.Gender;
import com.example.demo.repository.jpa.AccountRepository;
import com.example.demo.service.LikeService;

@Controller
@RequestMapping("/public")
public class PublicAccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LikeService likeService;

    // 一般アカウント紹介一覧
    @GetMapping("/account-list")
    public String showAccountList(Model model) {
        List<Account> accounts = accountRepository.findByIsDeletedFalseAndRole("ROLE_USER");
        model.addAttribute("accounts", accounts);
        return "public/accountList";
    }



    @GetMapping("/account/{id}")
    public String showAccountDetail(@PathVariable Long id, Model model) {
        Account account = accountRepository.findById(id).orElse(null);

        if (account != null) {
            // 性別を日本語表記に変換
            String genderJapanese = convertGenderToJapanese(account.getGender());
            model.addAttribute("genderJapanese", genderJapanese); // 日本語表記の性別をモデルに追加
        }

        model.addAttribute("account", account);
        return "public/accountDetail";
    }

    /**
     * 性別を日本語表記に変換するヘルパーメソッド
     */
    private String convertGenderToJapanese(Gender gender) {
        if (gender == Gender.MALE) {
            return "男性";
        } else if (gender == Gender.FEMALE) {
            return "女性";
        } else {
            return "その他";
        }
    }




 // 非同期いいね更新
    @PostMapping("/account/{id}/like")
    @ResponseBody
    public String likeAccount(@PathVariable Long id) {
        try {
            System.out.println("Received account ID: " + id); // IDをコンソールに出力
            likeService.likeAccount(id);
            return "success";
        } catch (Exception e) {
            e.printStackTrace(); // エラー内容をログに出力
            return "error: " + e.getMessage(); // エラー内容を返す
        }
    }




 // アカウント詳細をJSON形式で取得するAPIエンドポイント
 
    @GetMapping("/account/{id}/details")
    @ResponseBody
    public ResponseEntity<Account> getAccountDetails(@PathVariable Long id) {
        Account account = accountRepository.findById(id).orElse(null);
        
        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found
        }
        
        return ResponseEntity.ok(account); // 200 OK と共にアカウント情報を返す
    }



    // アカウント一覧をJSON形式で取得するAPIエンドポイント
    @GetMapping("/account/list")
    @ResponseBody
    public List<Account> getAllAccounts() {
        return accountRepository.findByIsDeletedFalseAndRole("ROLE_USER");
    }
    
 

}
