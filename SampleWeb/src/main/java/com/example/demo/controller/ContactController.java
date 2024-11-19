package com.example.demo.controller; 

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.Contact;
import com.example.demo.repository.jpa.ContactRepository;

import jakarta.persistence.EntityNotFoundException;

@Controller
@RequestMapping("/contacts")
public class ContactController {

    @Autowired
    private ContactRepository contactRepository;

    @GetMapping("/list")
    public String listContacts(Model model) {
        List<Contact> contacts = contactRepository.findByIsDeletedFalse(); // 論理削除されていないお問い合わせを取得
        model.addAttribute("contacts", contacts);
        return "contacts/list";
    }

    @GetMapping("/detail/{id}")
    public String contactDetail(@PathVariable Long id, Model model) {
        Contact contact = contactRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("お問い合わせが見つかりません"));
        model.addAttribute("contact", contact);
        return "contacts/detail";
    }
    
    @PostMapping("/update/{id}")
    public String updateContact(@PathVariable Long id, @ModelAttribute Contact contact) {
        // IDを取得して既存の情報を取得
        Contact existingContact = contactRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("お問い合わせが見つかりません"));

        // ステータスを日本語で保持
        String status = contact.getStatus();
        if ("未対応".equals(status) || "対応中".equals(status) || "対応済み".equals(status)) {
            existingContact.setStatus(status); // ステータスのみ更新
        } else {
            existingContact.setStatus("未対応"); // デフォルト値を設定
        }

        existingContact.setUpdated_date(LocalDateTime.now()); // 更新日を設定
        contactRepository.save(existingContact); // 更新
        return "redirect:/contacts/list"; // 更新後に一覧ページへリダイレクト
    }

    @PostMapping("/delete/{id}")
    public String deleteContact(@PathVariable Long id) {
        Contact contact = contactRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("お問い合わせが見つかりません"));
        contact.setDeleted_date(LocalDateTime.now()); // 削除日を設定
        contact.setIsDeleted(true); // 論理削除フラグを設定
        contactRepository.save(contact); // 更新して保存
        return "redirect:/contacts/list"; // 削除後に一覧ページへリダイレクト
    }
}
