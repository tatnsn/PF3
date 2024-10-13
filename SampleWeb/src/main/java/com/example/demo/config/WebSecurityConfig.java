package com.example.demo.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.constant.UlrConst;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    // MessageSourceはコンストラクタインジェクションにするか、フィールドでの注入が可能です
    private final MessageSource messageSource;  

    public WebSecurityConfig(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    // PasswordEncoderをBeanとして定義
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    
    // SecurityFilterChainの定義
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(UlrConst.NO_AUTHENTICATION).permitAll()  // 認証不要なURLの設定
                .requestMatchers("/accounts/**").permitAll()  // accountsエンドポイントを許可
                .requestMatchers("/templates/**").permitAll()  // テンプレートファイルへのアクセスを許可
                .anyRequest().authenticated()  // それ以外は認証を要求
            )
            .formLogin(login -> login
                .loginPage(UlrConst.LOGIN)
                .usernameParameter("loginId")
                .defaultSuccessUrl(UlrConst.MENU)
            );

        return http.build();
    }

    // DaoAuthenticationProviderの定義
    @Bean
    public AuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        provider.setMessageSource(messageSource);
        return provider;
    }
}
