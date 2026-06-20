package com.infogames.global.config;

import com.infogames.global.security.RestAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final RestAuthenticationEntryPoint authenticationEntryPoint;

    // UserService가 주입받는 비밀번호 인코더 (BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AJAX 로그인에서 직접 인증할 때 사용
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // 인증 결과(SecurityContext)를 세션에 저장/복원
    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           SecurityContextRepository securityContextRepository) throws Exception {
        http
            .securityContext(ctx -> ctx.securityContextRepository(securityContextRepository))
            .authorizeHttpRequests(auth -> auth
                // 정적 리소스 / 기본 경로
                .requestMatchers("/", "/health", "/error", "/favicon.ico").permitAll()
                .requestMatchers("/css/**", "/js/**", "/images/**", "/uploads/**", "/ckeditor/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                // 회원 인증 관련 공개 페이지
                .requestMatchers("/user/login", "/user/signup", "/user/find-id", "/user/find-password").permitAll()
                // 회원 인증 관련 공개 API
                .requestMatchers("/api/user/signup", "/api/user/login", "/api/user/logout",
                        "/api/user/check-username", "/api/user/check-nickname",
                        "/api/user/find-id", "/api/user/find-password").permitAll()
                // 게시판/댓글/첨부파일 "읽기"는 비로그인 허용
                .requestMatchers(HttpMethod.GET, "/board/*/list", "/board/*/view/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/board/**", "/api/comments", "/api/file/**").permitAll()
                // 그 외는 인증 필요 (글쓰기/수정/삭제, 댓글 작성, 마이페이지, 업로드 등)
                .anyRequest().authenticated()
            )
            .exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint))
            .csrf(csrf -> csrf
                // h2-console, 그리고 CKEditor 이미지 업로드(iframe 폼 전송이라 CSRF 헤더를
                // 실을 수 없음 — 인증은 필요)는 CSRF 예외 처리
                .ignoringRequestMatchers(
                        new AntPathRequestMatcher("/h2-console/**"),
                        new AntPathRequestMatcher("/api/file/image"))
            )
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            )
            // 로그인/로그아웃은 API 컨트롤러에서 처리하므로 기본 폼 로그인은 비활성
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())
            .logout(logout -> logout.disable());

        return http.build();
    }
}
