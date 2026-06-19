package com.infogames.domain.comment.controller;

import com.infogames.domain.comment.dto.CommentCreateRequest;
import com.infogames.domain.comment.dto.CommentReplyRequest;
import com.infogames.domain.comment.dto.CommentResponse;
import com.infogames.domain.comment.dto.CommentUpdateRequest;
import com.infogames.domain.comment.service.CommentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 댓글 REST API (레거시의 *.do AJAX 댓글 기능 대체)
 */
@Slf4j
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentApiController {

    private final CommentService commentService;

    /**
     * 게시글의 댓글 목록 (계층형)
     */
    @GetMapping
    public ResponseEntity<?> list(@RequestParam Long postId) {
        List<CommentResponse> comments = commentService.getComments(postId);
        return ResponseEntity.ok(Map.of(
                "comments", comments,
                "count", commentService.countComments(postId)
        ));
    }

    /**
     * 댓글 작성 (최상위)
     */
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CommentCreateRequest request,
                                    BindingResult bindingResult,
                                    HttpSession session) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        Long userId = loginUserId(session);
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("error", "로그인이 필요합니다"));
        }
        try {
            CommentResponse comment = commentService.createComment(request.getPostId(), userId, request.getContent());
            return ResponseEntity.ok(comment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 대댓글(답글) 작성
     */
    @PostMapping("/reply")
    public ResponseEntity<?> reply(@Valid @RequestBody CommentReplyRequest request,
                                   BindingResult bindingResult,
                                   HttpSession session) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        Long userId = loginUserId(session);
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("error", "로그인이 필요합니다"));
        }
        try {
            CommentResponse comment = commentService.createReply(request.getParentId(), userId, request.getContent());
            return ResponseEntity.ok(comment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 댓글 수정
     */
    @PutMapping("/{commentId}")
    public ResponseEntity<?> update(@PathVariable Long commentId,
                                    @Valid @RequestBody CommentUpdateRequest request,
                                    BindingResult bindingResult,
                                    HttpSession session) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        Long userId = loginUserId(session);
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("error", "로그인이 필요합니다"));
        }
        try {
            CommentResponse comment = commentService.updateComment(commentId, userId, request.getContent());
            return ResponseEntity.ok(comment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 댓글 삭제 (자식 댓글까지 함께 삭제)
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> delete(@PathVariable Long commentId, HttpSession session) {
        Long userId = loginUserId(session);
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("error", "로그인이 필요합니다"));
        }
        try {
            commentService.deleteComment(commentId, userId);
            return ResponseEntity.ok(Map.of("message", "댓글이 삭제되었습니다"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    private Long loginUserId(HttpSession session) {
        return (Long) session.getAttribute("userId");
    }
}
