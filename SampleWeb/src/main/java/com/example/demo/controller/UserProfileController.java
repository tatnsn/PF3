package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Account; // Accountエンティティをインポート
import com.example.demo.form.ProfileForm;
import com.example.demo.service.AccountService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserProfileController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/profile")
    public String showProfile(Model model) {
        Account currentUser = accountService.getCurrentAdminUser();
        ProfileForm profileForm = new ProfileForm(currentUser);
        model.addAttribute("profileForm", profileForm);
        return "user/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@Valid @ModelAttribute("profileForm") ProfileForm profileForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/profile"; // エラーがある場合は再表示
        }

        Account accountToUpdate = accountService.getCurrentAdminUser();
        accountToUpdate.setName(profileForm.getName());
        accountToUpdate.setFurigana(profileForm.getFurigana());
        accountToUpdate.setGender(profileForm.getGender());
        accountToUpdate.setAge(profileForm.getAge());
        accountToUpdate.setIntroduction(profileForm.getIntroduction());

        if (profileForm.getProfileImage() != null && !profileForm.getProfileImage().isEmpty()) {
            try {
                // ファイル名を取得
                String profileImageFileName = saveProfileImage(profileForm.getProfileImage());
                accountToUpdate.setProfileImage("/images/" + profileImageFileName);
            } catch (IllegalArgumentException e) {
                bindingResult.rejectValue("profileImage", "error.profileImage", e.getMessage());
                return "user/profile";
            } catch (IOException e) {
                bindingResult.rejectValue("profileImage", "error.profileImage", "画像の保存中にエラーが発生しました");
                return "user/profile";
            }
        }

        accountService.updateAccount(accountToUpdate);
        return "redirect:/user/dashboard";
    }

    private String saveProfileImage(MultipartFile profileImage) throws IOException {
        if (profileImage == null || profileImage.isEmpty()) {
            return null;
        }

        // ファイルサイズのバリデーション (2MB制限)
        if (profileImage.getSize() > 2 * 1024 * 1024) {
            throw new IllegalArgumentException("プロフィール画像は2MB以内である必要があります。");
        }

        // ランダムなファイル名を生成
        String filename = UUID.randomUUID().toString() + "_" + profileImage.getOriginalFilename();

        // 画像の保存先のディレクトリ
        String uploadDir = "C:/ForDevelop/workspace/PF3/SampleWeb/src/main/resources/static/images";
        File destinationFile = new File(uploadDir, filename);

        // ディレクトリが存在しない場合は作成
        if (!destinationFile.getParentFile().exists()) {
            destinationFile.getParentFile().mkdirs();
        }

        // 画像ファイルを保存
        profileImage.transferTo(destinationFile);

        // データベースには相対パスで保存
        return filename;
    }
}
