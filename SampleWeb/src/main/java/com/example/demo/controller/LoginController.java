package com.example.demo.controller;

import org.springframework.context.MessageSource;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.constant.ErrorMessageConst;
import com.example.demo.constant.UlrConst;
import com.example.demo.form.LoginForm;
import com.example.demo.service.LoginService;
import com.example.demo.util.AppUtil;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {

	private final LoginService service;
	
	private final MessageSource messageSource; 

	
	private final HttpSession session;
    
    @GetMapping(UlrConst.LOGIN)
    public String view(Model model,LoginForm form) {
       
    	return "login"; // ここでビュー名を返す
    }
    
    @GetMapping(value = UlrConst.LOGIN, params = "error")
    public String viewWithError(Model model, LoginForm form) {
        var errorInfo = (Exception) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        
        // null チェックを追加
        if (errorInfo != null) {
            model.addAttribute("errorMsg", errorInfo.getMessage());
        } else {
            model.addAttribute("errorMsg", "An unknown error occurred.");
        }
        
        return "login"; // ログイン画面へのビューを返す
    }

    
	@PostMapping(UlrConst.LOGIN)
	public String login(Model model ,LoginForm form) {
		var userInfo = service.searchUserById(form.getLoginId());
		var isCorrectUserAuth = userInfo.isPresent()
				&& form.getPassword().equals(userInfo.get().getPassword());
		if(isCorrectUserAuth) {
			return"redirect:/menu";
		}else {
			var errorMsg = AppUtil.getMessage(messageSource, ErrorMessageConst.LOGIN_WROND_INPUT);
			model.addAttribute("errorMsg",errorMsg);
			return"login";
		}
	}

}

