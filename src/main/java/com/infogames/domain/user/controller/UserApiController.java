package com.infogames.domain.user.controller;

import com.infogames.domain.user.dto.*;
import com.infogames.domain.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody UserSignupRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        try {
            UserResponse user = userService.signup(request);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginRequest request, 
                                    BindingResult bindingResult,
                                    HttpSession session) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        try {
            UserResponse user = userService.login(request);
            
            // 세션에 사용자 정보 저장
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("nickname", user.getNickname());
            
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("message", "로그아웃 성공"));
    }

    /**
     * 아이디 중복 체크
     */
    @GetMapping("/check-username")
    public ResponseEntity<?> checkUsername(@RequestParam String username) {
        boolean exists = userService.checkUsernameDuplicate(username);
        return ResponseEntity.ok(Map.of(
            "exists", exists,
            "message", exists ? "이미 사용 중인 아이디입니다" : "사용 가능한 아이디입니다"
        ));
    }

    /**
     * 닉네임 중복 체크
     */
    @GetMapping("/check-nickname")
    public ResponseEntity<?> checkNickname(@RequestParam String nickname) {
        boolean exists = userService.checkNicknameDuplicate(nickname);
        return ResponseEntity.ok(Map.of(
            "exists", exists,
            "message", exists ? "이미 사용 중인 닉네임입니다" : "사용 가능한 닉네임입니다"
        ));
    }

    /**
     * 회원정보 수정
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserUpdateRequest request,
                                        BindingResult bindingResult,
                                        HttpSession session) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("error", "로그인이 필요합니다"));
        }

        try {
            UserResponse user = userService.updateUser(userId, request);
            
            // 세션 정보 업데이트
            session.setAttribute("nickname", user.getNickname());
            
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 비밀번호 변경
     */
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> request,
                                           HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("error", "로그인이 필요합니다"));
        }

        String currentPassword = request.get("currentPassword");
        String newPassword = request.get("newPassword");

        try {
            userService.updatePassword(userId, currentPassword, newPassword);
            return ResponseEntity.ok(Map.of("message", "비밀번호가 변경되었습니다"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("error", "로그인이 필요합니다"));
        }

        try {
            userService.deleteUser(userId);
            session.invalidate();
            return ResponseEntity.ok(Map.of("message", "회원 탈퇴가 완료되었습니다"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 아이디 찾기
     */
    @PostMapping("/find-id")
    public ResponseEntity<?> findId(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        String email = request.get("email");

        try {
            String username = userService.findUsername(name, email);
            return ResponseEntity.ok(Map.of("username", username));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 비밀번호 찾기 (임시 비밀번호 발급)
     */
    @PostMapping("/find-password")
    public ResponseEntity<?> findPassword(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String email = request.get("email");

        try {
            userService.resetPassword(username, email);
            return ResponseEntity.ok(Map.of("message", "임시 비밀번호가 이메일로 발송되었습니다"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
