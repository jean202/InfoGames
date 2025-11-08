package com.infogames.domain.file.entity;

import com.infogames.domain.board.entity.Post;
import com.infogames.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "files")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class File extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false, length = 255)
    private String originalName;

    @Column(nullable = false, length = 255)
    private String savedName;

    @Column(nullable = false, length = 500)
    private String filePath;

    @Column(nullable = false)
    private Long fileSize;

    @Column(nullable = false, length = 100)
    private String contentType;

    @Column(nullable = false)
    @Builder.Default
    private Integer fileOrder = 0;

    public String getFullPath() {
        return filePath + "/" + savedName;
    }

    public boolean isImage() {
        return contentType.startsWith("image/");
    }
}
