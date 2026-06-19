package com.infogames.domain.comment.repository;

import com.infogames.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 특정 게시글의 모든 댓글을 작성자 정보와 함께 작성순으로 조회.
     * 트리 조립은 서비스에서 수행한다(작성자는 fetch join으로 N+1 방지).
     */
    @Query("SELECT c FROM Comment c JOIN FETCH c.author WHERE c.post.id = :postId ORDER BY c.createdAt ASC")
    List<Comment> findByPostIdWithAuthor(@Param("postId") Long postId);

    /**
     * 게시글의 댓글 개수
     */
    long countByPostId(Long postId);

    /**
     * 특정 댓글의 직속 자식(대댓글) 목록
     */
    List<Comment> findByParentId(Long parentId);
}
