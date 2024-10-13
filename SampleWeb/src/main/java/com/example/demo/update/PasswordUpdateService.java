/*package com.example.demo.update;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.UserInfo;
import com.example.demo.repository.UserInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PasswordUpdateService {

    private final UserInfoRepository userInfoRepository;
    private final PasswordEncoder passwordEncoder;

    // 既存のユーザーパスワードをBCrypt形式に更新
    public void encodeAndSavePasswords() {
        List<UserInfo> users = userInfoRepository.findAll(); // すべてのユーザーを取得
        for (UserInfo user : users) {
            String encodedPassword = passwordEncoder.encode(user.getPassword()); // プレーンテキストのパスワードをエンコード
            user.setPassword(encodedPassword); // エンコードしたパスワードをセット
            userInfoRepository.save(user); // データベースに保存
        }
    }
}*/
