package com.example.demo.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String redirectUrl;
        
        // ユーザーの権限に基づいてリダイレクト先を決定
        if (authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            redirectUrl = "/admin/dashboard";
        } else {
            redirectUrl = "/user/dashboard";
        }

        // リダイレクト
        response.sendRedirect(redirectUrl);
    }
}
