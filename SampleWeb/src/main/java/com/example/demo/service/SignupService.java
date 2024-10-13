package com.example.demo.service;

import org.dozer.Mapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.UserInfo;
import com.example.demo.form.SignupForm;
import com.example.demo.repository.UserInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignupService {

    private final UserInfoRepository repository;
    private final Mapper mapper;
    private final PasswordEncoder passwordEncoder;  // PasswordEncoderを注入

    public UserInfo resistUserInfo(SignupForm form) {
        // フォームからエンティティにマッピング
        var userInfo = mapper.map(form, UserInfo.class);
        
        // パスワードをエンコード
        String encodedPassword = passwordEncoder.encode(form.getPassword());
        userInfo.setPassword(encodedPassword);  // エンコードされたパスワードをセット

        // エンコードされたパスワードを持つユーザー情報を保存
        return repository.save(userInfo);
    }
}
