package com.infogames.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 댓글 작성 요청 (최상위 댓글)
 */
@Getter
@Setter
@NoArgsConstructor
public class CommentCreateRequest {

    @NotNull(message = "게시글 정보가 필요합니다")
    private Long postId;

    @NotBlank(message = "댓글 내용을 입력하세요")
    @Size(max = 1000, message = "댓글은 1000자 이내로 입력하세요")
    private String content;
}
