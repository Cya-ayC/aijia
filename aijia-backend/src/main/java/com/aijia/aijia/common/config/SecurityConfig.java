package com.aijia.aijia.common.config;

import com.aijia.aijia.common.filter.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // 开启注解控制权限（如 @PreAuthorize）
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 使用 BCrypt 加密工具，系统会自动匹配你数据库里 $2a$10 开头的密文
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. 禁用 CSRF (前后端分离项目不需要)
                .csrf(csrf -> csrf.disable())
                // 2. 禁用 Session (因为我们用 JWT 保持登录状态)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 3. 配置接口放行规则
                .authorizeHttpRequests(auth -> auth
                        // 🚀 核心改动：把注册接口也加入白名单
                        .requestMatchers("/bus/user/login", "/sys/user/register").permitAll()

                        // 如果你有其他公共接口，比如“查询小区简介”，也可以放这里
                        .requestMatchers("/doc.html", "/webjars/**", "/v3/api-docs/**").permitAll()

                        .anyRequest().authenticated()
                );

        // --- 关键：在验证用户名密码之前，先跑 JWT 过滤器 ---
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
