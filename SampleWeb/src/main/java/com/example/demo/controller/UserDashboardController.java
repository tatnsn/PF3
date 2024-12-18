package com.example.demo.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.Account;
import com.example.demo.service.AccountService;
import com.example.demo.service.LikeService;

@Controller
@RequestMapping("/user/dashboard")
public class UserDashboardController {
	
	@Autowired
    private AccountService accountService;
	
	@Autowired
    private LikeService likeService;
	
	@GetMapping
    public String showDashboard(Model model) {
        Account loggedInUser = accountService.getCurrentUser();

        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();

        int totalLikes = likeService.getLikesForYear(loggedInUser.getId(), year);
        int monthlyLikes = likeService.getLikesForMonth(loggedInUser.getId(), year, month);

        model.addAttribute("totalLikes", totalLikes);
        model.addAttribute("monthlyLikes", monthlyLikes);

        return "user/dashboard";
    }

    @GetMapping("/settings")
    public String showUserSettings(Model model) {
        model.addAttribute("settings", getSettings());
        return "user/settings"; // user/settings.html
    }

    private String getSettings() {
        return "Settings Data"; // 仮の設定データ
    }

    @GetMapping("/profile")
    public String showUserProfile(Model model) {
        model.addAttribute("profile", getProfile());
        return "user/profile"; // user/profile.html
    }

    private String getProfile() {
        return "Profile Data"; // 仮のプロファイルデータ
    }
    
    @GetMapping("/contacts")
    public String showUserContacts() {
        return "redirect:/user/contacts/list"; // お問い合わせ一覧ページへリダイレクト
    }
    
}