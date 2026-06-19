package com.infogames.domain.board.service;

import com.infogames.domain.board.dto.PostCreateRequest;
import com.infogames.domain.board.dto.PostResponse;
import com.infogames.domain.board.entity.BoardType;
import com.infogames.domain.user.dto.UserSignupRequest;
import com.infogames.domain.user.service.UserService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired PostService postService;
    @Autowired UserService userService;
    @Autowired EntityManager em;

    private Long userId;

    @BeforeEach
    void setUp() {
        userId = userService.signup(UserSignupRequest.builder()
                .username("poster01").password("Passw0rd!").passwordConfirm("Passw0rd!")
                .name("작성자").email("poster@test.com").nickname("작성자")
                .birth(LocalDate.of(1990, 1, 1)).gender("MALE").build()).getId();
    }

    @Test
    @DisplayName("일반(FREE) 게시글은 저장 후 재조회 시 boardType 이 보존된다")
    void freePostBoardTypePersistedAfterReload() {
        Long postId = postService.createPost(userId, PostCreateRequest.builder()
                .boardType(BoardType.FREE).title("자유 글").content("본문 내용입니다.").build()).getId();

        // 영속성 컨텍스트를 비워 DB 에서 다시 읽도록 강제 (discriminator/boardType 왕복 검증)
        em.flush();
        em.clear();

        PostResponse loaded = postService.getPost(postId);
        assertThat(loaded.getBoardType()).isEqualTo(BoardType.FREE);
    }

    @Test
    @DisplayName("리뷰(REVIEW) 게시글은 rating 과 boardType 이 보존된다")
    void reviewPostPersistedAfterReload() {
        Long postId = postService.createPost(userId, PostCreateRequest.builder()
                .boardType(BoardType.REVIEW).title("리뷰 글").content("리뷰 본문입니다.").rating(4).build()).getId();

        em.flush();
        em.clear();

        PostResponse loaded = postService.getPost(postId);
        assertThat(loaded.getBoardType()).isEqualTo(BoardType.REVIEW);
        assertThat(loaded.getRating()).isEqualTo(4);
    }
}
