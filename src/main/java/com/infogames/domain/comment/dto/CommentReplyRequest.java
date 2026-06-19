package com.infogames.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 대댓글(답글) 작성 요청. 게시글은 부모 댓글로부터 결정된다.
 */
@Getter
@Setter
@NoArgsConstructor
public class CommentReplyRequest {

    @NotNull(message = "부모 댓글 정보가 필요합니다")
    private Long parentId;

    @NotBlank(message = "댓글 내용을 입력하세요")
    @Size(max = 1000, message = "댓글은 1000자 이내로 입력하세요")
    private String content;
}
