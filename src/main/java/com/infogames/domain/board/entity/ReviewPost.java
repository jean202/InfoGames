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

    @Column(nullable = false)
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
