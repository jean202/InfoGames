package com.infogames.domain.board.dto;

import com.infogames.domain.board.entity.BoardType;
import com.infogames.domain.board.entity.Post;
import com.infogames.domain.board.entity.ReviewPost;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponse {

    private Long id;
    private BoardType boardType;
    private String title;
    private String content;
    private Integer viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 작성자 정보
    private Long authorId;
    private String authorUsername;
    private String authorNickname;

    // ReviewBoard용
    private Integer rating;

    public static PostResponse from(Post post) {
        PostResponseBuilder builder = PostResponse.builder()
                .id(post.getId())
                .boardType(post.getBoardType())
                .title(post.getTitle())
                .content(post.getContent())
                .viewCount(post.getViewCount())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .authorId(post.getAuthor().getId())
                .authorUsername(post.getAuthor().getUsername())
                .authorNickname(post.getAuthor().getNickname());

        // ReviewPost인 경우 rating 추가
        if (post instanceof ReviewPost) {
            builder.rating(((ReviewPost) post).getRating());
        }

        return builder.build();
    }
}
