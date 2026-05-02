package com.aijia.aijia.common.filter;

import com.aijia.aijia.common.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. 获取 Header 中的 Authorization 字段
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // 2. 检查 Header 是否存在且格式正确 (兼容带 Bearer 前缀或纯 Token)
        if (StringUtils.hasText(authHeader)) {
            if (authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            } else {
                token = authHeader; // 兼容你之前直接传 Token 的情况
            }

            try {
                // 3. 验证 Token 是否有效并获取用户名
                if (!jwtUtils.isTokenExpired(token)) {
                    username = jwtUtils.getUsernameFromToken(token);
                }
            } catch (Exception e) {
                // 如果 Token 损坏或解析失败，打印日志，但不抛出异常，让请求继续走
                logger.error("JWT Token 解析失败: " + e.getMessage());
            }
        }

        // 4. 如果解析到了用户名，且当前安全上下文没有认证信息
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (userDetails != null) {
                // 封装认证对象
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 存入 Security 上下文，这步完成代表“刷卡”成功
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 5. 无论有没有 Token，最终都要放行到下一个过滤器 (Security 会根据配置决定是否拦截)
        filterChain.doFilter(request, response);
    }
}
