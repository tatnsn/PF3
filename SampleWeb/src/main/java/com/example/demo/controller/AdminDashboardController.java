package com.example.demo.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.Account;
import com.example.demo.service.AccountService;
import com.example.demo.service.LikeService;

@Controller
@RequestMapping("/admin/dashboard")
public class AdminDashboardController {

    @Autowired
    private AccountService accountService;
    
    @Autowired
    private LikeService likeService;

    @GetMapping
    public String showDashboard(Model model) {
        List<Account> yearlyLikes = getYearlyLikes();
        List<Account> monthlyLikes = getMonthlyLikes();

        model.addAttribute("yearlyLikes", yearlyLikes);
        model.addAttribute("monthlyLikes", monthlyLikes);
        return "admin/dashboard";
    }

    private List<Account> getYearlyLikes() {
        LocalDate startOfYear = LocalDate.of(LocalDate.now().getYear(), 1, 1);
        LocalDateTime startDateTime = startOfYear.atStartOfDay();
        LocalDateTime endDateTime = LocalDate.now().atTime(23, 59, 59, 999999999);
        return likeService.findTopLikedAccountsForPeriod(startDateTime, endDateTime);
    }

    private List<Account> getMonthlyLikes() {
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDateTime startDateTime = startOfMonth.atStartOfDay();
        LocalDateTime endDateTime = LocalDate.now().atTime(23, 59, 59, 999999999);
        return likeService.findTopLikedAccountsForPeriod(startDateTime, endDateTime);
    }

  
    @GetMapping("/contacts/list")
    public String showContactsList(Model model) {
        model.addAttribute("contacts", getContacts());
        return "contacts/list"; // contacts/list.htmlというテンプレートを使用
    }

    @GetMapping("/account/list")
    public String showAccountList(Model model) {
        // アカウントリストを取得
        List<Account> accounts = accountService.getAllAccounts(); // 新しいメソッドを追加してアカウントリストを取得
        model.addAttribute("accounts", accounts);
        return "account/list"; // account/list.htmlというテンプレートを使用
    }

    @GetMapping("/admin/settings")
    public String showAdminSettings(Model model) {
        model.addAttribute("settings", getSettings());
        return "admin/settings"; // 正しいテンプレート名を指定
    }

  

    // contactsのデータを返すダミーデータ例
    private List<String> getContacts() {
        return List.of("Contact 1", "Contact 2", "Contact 3");
    }

    private String getSettings() {
        return "Settings Data"; // 仮のデータを返します
    }
}
