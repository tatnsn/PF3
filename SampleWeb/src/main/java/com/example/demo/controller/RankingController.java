package com.example.demo.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.entity.Account;
import com.example.demo.service.LikeService;

@Controller
@RequestMapping("/public/ranking")
public class RankingController {

    @Autowired
    private LikeService likeService;

    // 月ごとのいいねランキングの表示
    @GetMapping
    public String showMonthlyLikes(Model model, @RequestParam(value = "month", defaultValue = "1") int month) {
        if (month < 1 || month > 12) {
            model.addAttribute("error", "無効な月の指定です。1から12の間の月を指定してください。");
            return "error";
        }

        LocalDate startOfMonth = LocalDate.of(LocalDate.now().getYear(), month, 1);
        LocalDateTime startOfMonthDateTime = startOfMonth.atStartOfDay();
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());
        LocalDateTime endOfMonthDateTime = endOfMonth.atTime(23, 59, 59, 999999999);

        List<Account> topLikedAccounts = likeService.findTopLikedAccountsForMonth(startOfMonthDateTime, endOfMonthDateTime);

        if (topLikedAccounts == null || topLikedAccounts.isEmpty()) {
            model.addAttribute("message", "指定した月のデータが見つかりませんでした。");
            model.addAttribute("accounts", List.of()); // 空のリストを追加してエラーを防止
        } else {
            model.addAttribute("accounts", topLikedAccounts);
        }
        
        model.addAttribute("month", month);
        return "public/ranking";
    }


    // 月ごとのランキングデータをJSON形式で取得
    @GetMapping("/data")
    @ResponseBody
    public List<Account> getMonthlyRankingData(@RequestParam(value = "month", defaultValue = "1") int month) {
        LocalDate startOfMonth = LocalDate.of(LocalDate.now().getYear(), month, 1);
        LocalDateTime startOfMonthDateTime = startOfMonth.atStartOfDay();
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());
        LocalDateTime endOfMonthDateTime = endOfMonth.atTime(23, 59, 59, 999999999);

        return likeService.findTopLikedAccountsForMonth(startOfMonthDateTime, endOfMonthDateTime);
    }

    // 非同期いいね更新エンドポイント
    @PostMapping("/account/{id}/like")
    @ResponseBody
    public Map<String, Object> likeAccount(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            int updatedLikes = likeService.likeAccount(id);
            response.put("likes", updatedLikes);
            System.out.println("Updated likes count in response: " + updatedLikes);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", "いいねの更新に失敗しました");
        }
        return response;
    }
}
