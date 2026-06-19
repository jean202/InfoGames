package com.infogames.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 댓글 수정 요청
 */
@Getter
@Setter
@NoArgsConstructor
public class CommentUpdateRequest {

    @NotBlank(message = "댓글 내용을 입력하세요")
    @Size(max = 1000, message = "댓글은 1000자 이내로 입력하세요")
    private String content;
}
