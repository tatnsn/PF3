package com.example.demo.controller;   

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.Category;
import com.example.demo.entity.Contact;
import com.example.demo.repository.jpa.CategoryRepository;
import com.example.demo.repository.jpa.ContactRepository;

import jakarta.persistence.EntityNotFoundException;

@Controller
@RequestMapping("/send") // ルートを変更
public class SendContactController {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private CategoryRepository categoryRepository; // カテゴリリポジトリを注入

    @Autowired
    private JavaMailSender emailSender; // メール送信サービスを注入

    @GetMapping
    public String showSendContactForm(Model model) {
        System.out.println("GET /send が呼び出されました");
        try {
            model.addAttribute("contact", new Contact());

            // カテゴリリストを取得
            List<Category> categories = categoryRepository.findAll();
            System.out.println("カテゴリ件数: " + categories.size());
            model.addAttribute("categories", categories);
        } catch (Exception e) {
            System.out.println("エラー発生: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorMessage", "カテゴリデータの取得に失敗しました");
            return "error/500"; // カスタムエラーページ
        }

        return "send";
    }


    @PostMapping("") 
    public String sendContact(@ModelAttribute Contact contact, Model model) {
        // 送信されるデータを確認するためのログ出力
        System.out.println("送信されるカテゴリー ID: " + (contact.getCategory() != null ? contact.getCategory().getId() : "なし"));
        System.out.println("送信される本文: " + contact.getBody());

        // ステータスを日本語に設定
        contact.setStatus("未対応");
        contact.setCreated_date(LocalDateTime.now());

        // カテゴリが選択されているか確認
        if (contact.getCategory() != null && contact.getCategory().getId() != null) {
            // カテゴリをデータベースから取得
            Category category = categoryRepository.findById(contact.getCategory().getId())
                .orElseThrow(() -> new EntityNotFoundException("カテゴリが見つかりません"));
            contact.setCategory(category); // 選択されたカテゴリを設定

            // メール送信の準備
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("tatsujack@icloud.com"); // 送信元アドレスを設定
            message.setTo("tatsujack@icloud.com"); // 送信先のメールアドレス
            message.setSubject("新しいお問い合わせ: " + category.getName()); // カテゴリ名を件名に設定
            message.setText("お問い合わせ内容:\n" + contact.getBody());

            // メール送信処理
            try {
                emailSender.send(message); // メールを送信
            } catch (Exception e) {
                model.addAttribute("errorMessage", "メール送信に失敗しました: " + e.getMessage());
                System.out.println("メール送信エラー: " + e.getMessage()); // エラーメッセージを表示
                return "send"; // 送信画面に戻る
            }
        } else {
            model.addAttribute("errorMessage", "カテゴリを選択してください。");
            return "send"; // エラーメッセージを表示して送信画面に戻る
        }

        contactRepository.save(contact); // データベースに保存

        return "redirect:/"; // 送信後にhome画面へリダイレクト
    }





}
