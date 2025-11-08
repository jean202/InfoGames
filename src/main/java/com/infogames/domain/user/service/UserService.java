package com.infogames.domain.user.service;

import com.infogames.domain.user.dto.*;
import com.infogames.domain.user.entity.User;
import com.infogames.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     */
    @Transactional
    public UserResponse signup(UserSignupRequest request) {
        log.info("회원가입 시도: {}", request.getUsername());

        // 비밀번호 확인
        if (!request.isPasswordMatch()) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }

        // 중복 체크
        validateDuplicate(request);

        // User 엔티티 생성
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .email(request.getEmail())
                .nickname(request.getNickname())
                .birth(request.getBirth())
                .gender(request.getGender())
                .isActive(true)
                .build();

        User savedUser = userRepository.save(user);
        log.info("회원가입 완료: {} (ID: {})", savedUser.getUsername(), savedUser.getId());

        return UserResponse.from(savedUser);
    }

    /**
     * 중복 체크
     */
    private void validateDuplicate(UserSignupRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다");
        }
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다");
        }
    }

    /**
     * 아이디 중복 체크
     */
    public boolean checkUsernameDuplicate(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * 닉네임 중복 체크
     */
    public boolean checkNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    /**
     * 로그인 (비밀번호 검증)
     */
    public UserResponse login(UserLoginRequest request) {
        log.info("로그인 시도: {}", request.getUsername());

        User user = userRepository.findByUsernameAndIsActiveTrue(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원이거나 탈퇴한 회원입니다"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }

        log.info("로그인 성공: {} (ID: {})", user.getUsername(), user.getId());
        return UserResponse.from(user);
    }

    /**
     * 회원 정보 조회
     */
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다"));
        return UserResponse.from(user);
    }

    /**
     * 회원 정보 수정
     */
    @Transactional
    public UserResponse updateUser(Long userId, UserUpdateRequest request) {
        log.info("회원정보 수정 시도: ID {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다"));

        // 닉네임 중복 체크 (본인 제외)
        if (!user.getNickname().equals(request.getNickname()) &&
                userRepository.existsByNickname(request.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다");
        }

        user.updateProfile(request.getName(), request.getEmail(), request.getNickname());
        log.info("회원정보 수정 완료: ID {}", userId);

        return UserResponse.from(user);
    }

    /**
     * 비밀번호 변경
     */
    @Transactional
    public void updatePassword(Long userId, String currentPassword, String newPassword) {
        log.info("비밀번호 변경 시도: ID {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다"));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다");
        }

        user.updatePassword(passwordEncoder.encode(newPassword));
        log.info("비밀번호 변경 완료: ID {}", userId);
    }

    /**
     * 회원 탈퇴
     */
    @Transactional
    public void deleteUser(Long userId) {
        log.info("회원 탈퇴 시도: ID {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다"));

        user.deactivate();
        log.info("회원 탈퇴 완료: ID {}", userId);
    }

    /**
     * 아이디 찾기
     */
    public String findUsername(String name, String email) {
        User user = userRepository.findByNameAndEmail(name, email)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 회원 정보가 없습니다"));
        return user.getUsername();
    }

    /**
     * 비밀번호 찾기 (임시 비밀번호 발급)
     */
    @Transactional
    public void resetPassword(String username, String email) {
        log.info("비밀번호 재설정 시도: {}", username);

        User user = userRepository.findByUsernameAndEmail(username, email)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 회원 정보가 없습니다"));

        // 임시 비밀번호 생성 (실제로는 이메일 발송)
        String tempPassword = generateTempPassword();
        user.updatePassword(passwordEncoder.encode(tempPassword));

        log.info("비밀번호 재설정 완료: {} (임시 비밀번호: {})", username, tempPassword);
        // TODO: 이메일 발송 기능 추가
    }

    private String generateTempPassword() {
        // 간단한 임시 비밀번호 생성 (실제로는 더 복잡하게)
        return "Temp" + System.currentTimeMillis();
    }
}
