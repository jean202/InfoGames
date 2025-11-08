package com.infogames.domain.board.dto;

import com.infogames.domain.board.entity.BoardType;
import com.infogames.domain.board.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostListResponse {

    private Long id;
    private BoardType boardType;
    private String title;
    private Integer viewCount;
    private LocalDateTime createdAt;

    // 작성자 정보
    private String authorNickname;

    public static PostListResponse from(Post post) {
        return PostListResponse.builder()
                .id(post.getId())
                .boardType(post.getBoardType())
                .title(post.getTitle())
                .viewCount(post.getViewCount())
                .createdAt(post.getCreatedAt())
                .authorNickname(post.getAuthor().getNickname())
                .build();
    }
}
