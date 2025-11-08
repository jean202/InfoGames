package com.infogames.domain.user.controller;

import com.infogames.domain.user.dto.UserResponse;
import com.infogames.domain.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 로그인 페이지
     */
    @GetMapping("/login")
    public String loginForm() {
        return "user/login";
    }

    /**
     * 회원가입 페이지
     */
    @GetMapping("/signup")
    public String signupForm() {
        return "user/signup";
    }

    /**
     * 아이디 찾기 페이지
     */
    @GetMapping("/find-id")
    public String findIdForm() {
        return "user/find-id";
    }

    /**
     * 비밀번호 찾기 페이지
     */
    @GetMapping("/find-password")
    public String findPasswordForm() {
        return "user/find-password";
    }

    /**
     * 마이페이지
     */
    @GetMapping("/mypage")
    public String mypage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        
        if (userId == null) {
            return "redirect:/user/login";
        }

        UserResponse user = userService.getUserById(userId);
        model.addAttribute("user", user);
        
        return "user/mypage";
    }

    /**
     * 회원정보 수정 페이지
     */
    @GetMapping("/edit")
    public String editForm(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        
        if (userId == null) {
            return "redirect:/user/login";
        }

        UserResponse user = userService.getUserById(userId);
        model.addAttribute("user", user);
        
        return "user/edit";
    }

    /**
     * 비밀번호 변경 페이지
     */
    @GetMapping("/change-password")
    public String changePasswordForm(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        
        if (userId == null) {
            return "redirect:/user/login";
        }
        
        return "user/change-password";
    }

    /**
     * 회원 탈퇴 페이지
     */
    @GetMapping("/delete")
    public String deleteForm(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        
        if (userId == null) {
            return "redirect:/user/login";
        }
        
        return "user/delete";
    }
}
