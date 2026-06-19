package com.infogames.domain.comment.dto;

import com.infogames.domain.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 댓글 응답 DTO. 계층 구조를 children으로 표현하며, depth는 화면 들여쓰기에 사용한다.
 * (레거시의 comment_level 대체)
 */
@Getter
@Builder
public class CommentResponse {

    private Long id;
    private Long postId;
    private Long parentId;
    private int depth;
    private String content;

    private Long authorId;
    private String authorUsername;
    private String authorNickname;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<CommentResponse> children;

    public static CommentResponse from(Comment comment, int depth, List<CommentResponse> children) {
        return CommentResponse.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .depth(depth)
                .content(comment.getContent())
                .authorId(comment.getAuthor().getId())
                .authorUsername(comment.getAuthor().getUsername())
                .authorNickname(comment.getAuthor().getNickname())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .children(children)
                .build();
    }
}
