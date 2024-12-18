package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();  // パスワードのハッシュ化を無効にする（推奨されない）
    }

    @Bean
    public CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler(); // カスタム成功ハンドラーのビーンを定義
    }

 // "//" を許可するための StrictHttpFirewall 設定
    @Bean
    public HttpFirewall allowDoubleSlashHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedDoubleSlash(true); // "//" を許可
        return firewall;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        
            .authorizeHttpRequests(authz -> authz
            	.requestMatchers("/error").permitAll() // エラーページへのアクセスを許可
            	.requestMatchers("/send").permitAll() // /sendを公開にする
            	.requestMatchers("/login").permitAll()
                .requestMatchers("/public/**").permitAll() // /public以下のURLを公開にする
                .requestMatchers("/","/images/**", "/css/**", "/js/**", "/webjars/**").permitAll() // 静的リソースを公開にする
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/accounts/**").hasRole("ADMIN")
                .requestMatchers("/contacts/**").hasRole("ADMIN")
                .requestMatchers("/categories/**").hasRole("ADMIN")
                .requestMatchers("/user/**").hasRole("USER")
                
                .anyRequest().authenticated()
            		)
            .formLogin(login -> login
                    .loginPage("/login")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .successHandler(customAuthenticationSuccessHandler())  // カスタム成功ハンドラーを設定
                    .failureUrl("/login?error=true")
                    .permitAll()
                )
                .logout(logout -> logout
                    .logoutUrl("/logout")  // ログアウトURLの設定
                    .logoutSuccessUrl("/login?logout=true")  // ログアウト成功後の遷移先URL
                    .invalidateHttpSession(true)  // セッションの無効化
                    .permitAll()
                );

        // Firewall 設定を適用
     // Firewall 設定を適用
        http.setSharedObject(HttpFirewall.class, allowDoubleSlashHttpFirewall());

        return http.build();
    }
}
