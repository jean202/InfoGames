package com.infogames.domain.file.repository;

import com.infogames.domain.board.entity.Post;
import com.infogames.domain.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    /**
     * 게시글의 파일 목록 조회 (순서대로)
     */
    List<File> findByPostOrderByFileOrderAsc(Post post);

    /**
     * 게시글 ID로 파일 목록 조회
     */
    List<File> findByPostIdOrderByFileOrderAsc(Long postId);

    /**
     * 게시글의 파일 개수
     */
    long countByPost(Post post);

    /**
     * 게시글 삭제 시 파일도 삭제
     */
    void deleteByPost(Post post);

    /**
     * 게시글 ID로 파일 삭제
     */
    void deleteByPostId(Long postId);
}
