package com.infogames.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityRulesTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper om;

    private String json(Object o) throws Exception {
        return om.writeValueAsString(o);
    }

    @Test
    @DisplayName("공개 경로(홈/목록)는 비로그인 접근 200")
    void publicPaths() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk());
        mockMvc.perform(get("/board/free/list")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("보호 페이지는 비로그인 시 로그인으로 리다이렉트")
    void protectedPageRedirects() throws Exception {
        mockMvc.perform(get("/user/mypage"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/login"));
    }

    @Test
    @DisplayName("보호 API는 비로그인 시 401")
    void protectedApiReturns401() throws Exception {
        mockMvc.perform(post("/api/comments").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of("postId", 1, "content", "x"))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("회원가입→로그인 후 세션으로 보호 페이지 접근 200")
    void loginThenAccessProtected() throws Exception {
        var signup = Map.of(
                "username", "secuser1", "password", "Passw0rd!", "passwordConfirm", "Passw0rd!",
                "name", "보안유저", "email", "sec1@test.com", "nickname", "보안유저",
                "birth", "1990-01-01", "gender", "MALE");
        mockMvc.perform(post("/api/user/signup").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON).content(json(signup)))
                .andExpect(status().isOk());

        HttpSession session = mockMvc.perform(post("/api/user/login").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of("username", "secuser1", "password", "Passw0rd!"))))
                .andExpect(status().isOk())
                .andReturn().getRequest().getSession();

        mockMvc.perform(get("/user/mypage").session((org.springframework.mock.web.MockHttpSession) session))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("잘못된 비밀번호 로그인은 400")
    void loginBadCredentials() throws Exception {
        var signup = Map.of(
                "username", "secuser2", "password", "Passw0rd!", "passwordConfirm", "Passw0rd!",
                "name", "보안유저2", "email", "sec2@test.com", "nickname", "보안유저2",
                "birth", "1990-01-01", "gender", "FEMALE");
        mockMvc.perform(post("/api/user/signup").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON).content(json(signup)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/user/login").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of("username", "secuser2", "password", "WrongPass1!"))))
                .andExpect(status().isBadRequest());
    }
}
