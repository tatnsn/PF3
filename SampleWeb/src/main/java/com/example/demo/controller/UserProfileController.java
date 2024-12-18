package com.example.demo.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
            return "user/profile";
        }

        Account accountToUpdate = accountService.getCurrentAdminUser();
        accountToUpdate.setName(profileForm.getName());
        accountToUpdate.setFurigana(profileForm.getFurigana());
        accountToUpdate.setGender(profileForm.getGender());
        accountToUpdate.setAge(profileForm.getAge());
        accountToUpdate.setIntroduction(profileForm.getIntroduction());

        if (profileForm.getProfileImage() != null && !profileForm.getProfileImage().isEmpty()) {
            try {
                String profileImageFileName = accountService.saveProfileImage(profileForm.getProfileImage());
                accountToUpdate.setProfileImage(profileImageFileName);
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



    


}
