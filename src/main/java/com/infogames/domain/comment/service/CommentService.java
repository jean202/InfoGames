package com.infogames.domain.comment.service;

import com.infogames.domain.board.entity.Post;
import com.infogames.domain.board.repository.PostRepository;
import com.infogames.domain.comment.dto.CommentResponse;
import com.infogames.domain.comment.entity.Comment;
import com.infogames.domain.comment.repository.CommentRepository;
import com.infogames.domain.user.entity.User;
import com.infogames.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * 댓글 작성 (최상위)
     */
    @Transactional
    public CommentResponse createComment(Long postId, Long userId, String content) {
        Post post = findPost(postId);
        User author = findUser(userId);

        Comment comment = Comment.builder()
                .post(post)
                .author(author)
                .content(content)
                .build();

        Comment saved = commentRepository.save(comment);
        log.info("댓글 작성: commentId={}, postId={}, userId={}", saved.getId(), postId, userId);
        return CommentResponse.from(saved, 0, List.of());
    }

    /**
     * 대댓글(답글) 작성. 게시글은 부모 댓글로부터 결정된다.
     */
    @Transactional
    public CommentResponse createReply(Long parentId, Long userId, String content) {
        Comment parent = findComment(parentId);
        User author = findUser(userId);

        Comment reply = Comment.builder()
                .post(parent.getPost())
                .author(author)
                .parent(parent)
                .content(content)
                .build();

        Comment saved = commentRepository.save(reply);
        log.info("대댓글 작성: commentId={}, parentId={}, userId={}", saved.getId(), parentId, userId);
        return CommentResponse.from(saved, 0, List.of());
    }

    /**
     * 게시글의 댓글을 계층(트리) 형태로 조회
     */
    public List<CommentResponse> getComments(Long postId) {
        List<Comment> all = commentRepository.findByPostIdWithAuthor(postId);

        Map<Long, List<Comment>> childrenByParent = all.stream()
                .filter(c -> c.getParent() != null)
                .collect(Collectors.groupingBy(c -> c.getParent().getId()));

        return all.stream()
                .filter(c -> c.getParent() == null)
                .map(root -> toTree(root, childrenByParent, 0))
                .collect(Collectors.toList());
    }

    private CommentResponse toTree(Comment comment, Map<Long, List<Comment>> childrenByParent, int depth) {
        List<CommentResponse> children = childrenByParent.getOrDefault(comment.getId(), List.of()).stream()
                .map(child -> toTree(child, childrenByParent, depth + 1))
                .collect(Collectors.toList());
        return CommentResponse.from(comment, depth, children);
    }

    /**
     * 댓글 수정 (작성자만 가능)
     */
    @Transactional
    public CommentResponse updateComment(Long commentId, Long userId, String content) {
        Comment comment = findComment(commentId);
        validateAuthor(comment, userId);

        comment.update(content);
        log.info("댓글 수정: commentId={}, userId={}", commentId, userId);
        return CommentResponse.from(comment, 0, List.of());
    }

    /**
     * 댓글 삭제 (작성자만 가능). 자식 댓글까지 함께 삭제된다.
     */
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = findComment(commentId);
        validateAuthor(comment, userId);

        deleteRecursively(comment);
        log.info("댓글 삭제: commentId={}, userId={}", commentId, userId);
    }

    /**
     * 자식 댓글부터 삭제 후 자신을 삭제 (레거시 CONNECT BY 계층 삭제와 동일 효과).
     * 양방향 컬렉션의 메모리 상태에 의존하지 않고 FK 순서를 보장한다.
     */
    private void deleteRecursively(Comment comment) {
        for (Comment child : commentRepository.findByParentId(comment.getId())) {
            deleteRecursively(child);
        }
        commentRepository.delete(comment);
    }

    /**
     * 게시글의 댓글 개수
     */
    public long countComments(Long postId) {
        return commentRepository.countByPostId(postId);
    }

    // ===== 내부 helper =====

    private Post findPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다"));
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다"));
    }

    private void validateAuthor(Comment comment, Long userId) {
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new IllegalArgumentException("본인이 작성한 댓글만 수정/삭제할 수 있습니다");
        }
    }
}
