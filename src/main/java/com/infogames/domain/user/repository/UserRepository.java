package com.infogames.domain.user.repository;

import com.infogames.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * username으로 사용자 조회
     */
    Optional<User> findByUsername(String username);

    /**
     * email로 사용자 조회
     */
    Optional<User> findByEmail(String email);

    /**
     * nickname으로 사용자 조회
     */
    Optional<User> findByNickname(String nickname);

    /**
     * username 중복 체크
     */
    boolean existsByUsername(String username);

    /**
     * email 중복 체크
     */
    boolean existsByEmail(String email);

    /**
     * nickname 중복 체크
     */
    boolean existsByNickname(String nickname);

    /**
     * 이름과 이메일로 사용자 찾기 (아이디 찾기)
     */
    Optional<User> findByNameAndEmail(String name, String email);

    /**
     * username과 email로 사용자 찾기 (비밀번호 찾기)
     */
    Optional<User> findByUsernameAndEmail(String username, String email);

    /**
     * 활성 사용자만 조회
     */
    Optional<User> findByUsernameAndIsActiveTrue(String username);
}
