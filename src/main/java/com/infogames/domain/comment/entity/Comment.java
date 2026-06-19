package com.infogames.domain.comment.entity;

import com.infogames.domain.board.entity.Post;
import com.infogames.domain.user.entity.User;
import com.infogames.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * 댓글 엔티티
 *
 * <p>레거시 COMMENTID 테이블을 대체한다. 레거시는 COMMENT_PARENT(0 = 최상위)와
 * Oracle CONNECT BY로 계층을 표현했으나, JPA에서는 자기참조 연관관계(parent/children)로
 * 모델링한다. 최상위 댓글은 parent == null 이다.</p>
 */
@Entity
@Table(name = "comments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    /** 부모 댓글 (최상위 댓글이면 null) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    /** 자식 댓글 (대댓글). 부모 삭제 시 자식까지 함께 삭제(레거시 CONNECT BY 삭제와 동일) */
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> children = new ArrayList<>();

    @Column(nullable = false, length = 1000)
    private String content;

    public void update(String content) {
        this.content = content;
    }

    public boolean isAuthor(User user) {
        return this.author.getId().equals(user.getId());
    }

    public boolean isReply() {
        return this.parent != null;
    }
}
