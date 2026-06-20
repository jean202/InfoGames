package com.infogames.domain.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FileUploadTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper om;

    private MockMultipartFile pngUpload() {
        return new MockMultipartFile("upload", "pic.png", "image/png", new byte[]{(byte) 0x89, 'P', 'N', 'G'});
    }

    @Test
    @DisplayName("비로그인 시 에디터 이미지 업로드는 401")
    void imageUploadRequiresLogin() throws Exception {
        mockMvc.perform(multipart("/api/file/image").file(pngUpload()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("로그인 후 이미지 업로드 시 CKEditor 형식 JSON(uploaded=1, url) 반환")
    void imageUploadReturnsCkeditorJson() throws Exception {
        var signup = Map.of(
                "username", "fileuser", "password", "Passw0rd!", "passwordConfirm", "Passw0rd!",
                "name", "파일유저", "email", "file@test.com", "nickname", "파일유저",
                "birth", "1990-01-01", "gender", "MALE");
        mockMvc.perform(post("/api/user/signup").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsString(signup)))
                .andExpect(status().isOk());

        HttpSession session = mockMvc.perform(post("/api/user/login").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(Map.of("username", "fileuser", "password", "Passw0rd!"))))
                .andExpect(status().isOk())
                .andReturn().getRequest().getSession();

        mockMvc.perform(multipart("/api/file/image").file(pngUpload())
                        .session((MockHttpSession) session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uploaded").value(1))
                .andExpect(jsonPath("$.url").value(org.hamcrest.Matchers.startsWith("/uploads/images/")));
    }
}
