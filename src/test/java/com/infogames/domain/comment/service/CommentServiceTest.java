package com.infogames.domain.comment.service;

import com.infogames.domain.board.dto.PostCreateRequest;
import com.infogames.domain.board.entity.BoardType;
import com.infogames.domain.board.service.PostService;
import com.infogames.domain.comment.dto.CommentResponse;
import com.infogames.domain.comment.repository.CommentRepository;
import com.infogames.domain.user.dto.UserSignupRequest;
import com.infogames.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class CommentServiceTest {

    @Autowired CommentService commentService;
    @Autowired CommentRepository commentRepository;
    @Autowired UserService userService;
    @Autowired PostService postService;

    private Long userId;
    private Long otherUserId;
    private Long postId;

    @BeforeEach
    void setUp() {
        userId = userService.signup(UserSignupRequest.builder()
                .username("writer01").password("Passw0rd!").passwordConfirm("Passw0rd!")
                .name("작성자").email("writer@test.com").nickname("작성자")
                .birth(LocalDate.of(1990, 1, 1)).gender("MALE").build()).getId();

        otherUserId = userService.signup(UserSignupRequest.builder()
                .username("other01").password("Passw0rd!").passwordConfirm("Passw0rd!")
                .name("타인").email("other@test.com").nickname("타인")
                .birth(LocalDate.of(1991, 2, 2)).gender("FEMALE").build()).getId();

        postId = postService.createPost(userId, PostCreateRequest.builder()
                .boardType(BoardType.FREE).title("테스트 게시글")
                .content("댓글 테스트용 본문입니다.").build()).getId();
    }

    @Test
    @DisplayName("댓글 작성 → 대댓글 작성 → 계층형 조회")
    void createCommentAndReplyAndTree() {
        CommentResponse parent = commentService.createComment(postId, userId, "최상위 댓글");
        commentService.createReply(parent.getId(), otherUserId, "대댓글1");
        commentService.createReply(parent.getId(), userId, "대댓글2");
        commentService.createComment(postId, otherUserId, "또 다른 최상위 댓글");

        List<CommentResponse> tree = commentService.getComments(postId);

        assertThat(tree).hasSize(2);                       // 최상위 2개
        CommentResponse first = tree.get(0);
        assertThat(first.getContent()).isEqualTo("최상위 댓글");
        assertThat(first.getDepth()).isEqualTo(0);
        assertThat(first.getChildren()).hasSize(2);        // 대댓글 2개
        assertThat(first.getChildren().get(0).getDepth()).isEqualTo(1);
        assertThat(first.getChildren().get(0).getParentId()).isEqualTo(first.getId());
        assertThat(commentService.countComments(postId)).isEqualTo(4);
    }

    @Test
    @DisplayName("댓글 수정은 작성자만 가능")
    void updateOnlyByAuthor() {
        CommentResponse c = commentService.createComment(postId, userId, "원본");

        CommentResponse updated = commentService.updateComment(c.getId(), userId, "수정됨");
        assertThat(updated.getContent()).isEqualTo("수정됨");

        assertThatThrownBy(() -> commentService.updateComment(c.getId(), otherUserId, "침범"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("부모 댓글 삭제 시 대댓글까지 함께 삭제된다")
    void deleteCascadesToChildren() {
        CommentResponse parent = commentService.createComment(postId, userId, "부모");
        commentService.createReply(parent.getId(), userId, "자식1");
        commentService.createReply(parent.getId(), userId, "자식2");
        assertThat(commentRepository.count()).isEqualTo(3);

        commentService.deleteComment(parent.getId(), userId);

        assertThat(commentRepository.count()).isZero();
        assertThat(commentService.getComments(postId)).isEmpty();
    }
}
