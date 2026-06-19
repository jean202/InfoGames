package com.infogames.domain.board.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("REVIEW")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class ReviewPost extends Post {

    // SINGLE_TABLE 상속에서 하위 클래스 전용 컬럼은 DB 레벨에서 nullable 이어야 한다
    // (일반 Post 저장 시 RATING 이 null 이므로). 평점 필수 여부는 서비스/검증 로직에서 보장.
    @Column
    @Builder.Default
    private Integer rating = 0;

    public void updateWithRating(String title, String content, Integer rating) {
        super.update(title, content);
        this.rating = rating;
    }

    public void validateRating() {
        if (rating < 0 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 0 and 5");
        }
    }
}
