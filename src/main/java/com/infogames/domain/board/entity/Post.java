package com.infogames.domain.board.entity;

import com.infogames.domain.user.entity.User;
import com.infogames.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// 상속 구분용 discriminator 는 boardType 과 별도 컬럼(post_type)을 사용한다.
// (둘을 같은 컬럼에 매핑하면 일반 Post 의 discriminator 'POST' 가 BoardType 으로
//  역변환되며 깨진다)
@DiscriminatorColumn(name = "post_type")
@DiscriminatorValue("POST")
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    // 게시판 종류(FREE/TIP/REVIEW). 실제 영속 컬럼으로 저장한다.
    @Enumerated(EnumType.STRING)
    @Column(name = "board_type", nullable = false)
    private BoardType boardType;

    @Column(nullable = false, length = 200)
    private String title;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    @Builder.Default
    private Integer viewCount = 0;

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

    public boolean isAuthor(User user) {
        return this.author.getId().equals(user.getId());
    }
}
