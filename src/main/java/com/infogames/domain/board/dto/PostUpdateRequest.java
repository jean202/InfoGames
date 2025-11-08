package com.infogames.domain.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostUpdateRequest {

    @NotBlank(message = "제목은 필수입니다")
    @Size(min = 2, max = 100, message = "제목은 2-100자 사이여야 합니다")
    private String title;

    @NotBlank(message = "내용은 필수입니다")
    @Size(min = 10, message = "내용은 최소 10자 이상이어야 합니다")
    private String content;

    // ReviewBoard용
    private Integer rating;
}
